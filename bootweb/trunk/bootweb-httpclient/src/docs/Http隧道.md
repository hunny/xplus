先概括的说一下，HTTP tunnel是HTTP/1.1中引入的一个功能，主要为了解决明文的HTTP proxy无法代理跑在TLS中的流量（也就是https）的问题，同时提供了作为任意流量的TCP通道的能力。

其实在增加了HTTP tunnel功能之后，支持CONNECT报文的HTTP代理服务器和SOCKS5的代理服务器功能上已经几乎一样了（还是有不一样的地方的，详见后面）。由于CONNECT报文已经能支持任意类型TCP流量了，即使到HTTP/2也没有对它进行大的修改，基本保持了原有设计。


HTTP tunnel是哪里定义的？
tunnel以及tunnel相关的CONNECT报文最早主要定义在两个文件中：
    一个是 RFC2616 HTTP/1.1 中的5.1.1 Method，主要提到了HTTP1.1的所有报文中有一个CONNECT报文，但是写这个文件的时候CONNECT报文做什么还没有定下来。
    另一个是 RFC2817 HTTP Upgrade to TLS ，中的多个章节（5.2、5.3等多个地方）提到了CONNECT报文要解决的问题和具体实现。
    最后关于HTTP/2，在 RFC 7540 HTTP/2 中的8.3节对http2版本的connect报文做了定义，基本和之前的功能没有太大的出入。  

HTTP proxy是怎么工作的？
    在HTTP tunnel出来之前，HTTP proxy工作在中间人模式。
    也就是说在一次请求中，客户端（浏览器）明文的请求代理服务器。代理服务器明文去请求远端服务器（网站），拿到返回结果，再将返回结果返回给客户端。整个过程对代理服务器来说都是可见的，代理能看到你要请求的path，你请求中的header（包括例如auth头里面的用户名密码），代理也能看到网站返回给你的cookie。和中间人攻击的中间人是一种情况（笑）
    这种模式要求代理对请求进行适当的改写，RFC2616 5.1.2中要求请求代理的报文中Request-URI必须使用绝对路径，这样代理才能从中解出真正请求的目标，然后代理需要对这个请求进行改写再发送给远端服务器。

例如浏览器在不使用HTTP proxy时，发送的请求如下（省略了和示例无关的header，下同）：
GET / HTTP/1.1\r\n
Host: stackoverflow.com\r\n
Connection: keep-alive\r\n
\r\n
使用了代理后，发送的报文将变为：
GET http://stackoverflow.com/ HTTP/1.1\r\n
Host: stackoverflow.com\r\n
Proxy-Connection: keep-alive\r\n
\r\n
代理从请求的第一行中得知要请求的目标是stackoverflow.com，端口为默认端口（80），将第一行改写后，向网站服务器发送请求：
GET / HTTP/1.1\r\n
Host: stackoverflow.com\r\n
Connection: keep-alive\r\n
\r\n
顺带一提上面这个例子可以看到有一个Proxy-Connection的header也被改写了，这个是HTTP/1.1的一个黑历史，现在它已经不是标准header了，并在RFC7230中被建议不要使用。然而现在浏览器（比如chrome）仍然在发送这个header，因此代理服务器还是要对它做处理，处理方式就是当做Connection header改写后发送给远端。

为什么需要HTTP tunnel？
    从前一条可以看出，如果我们想在复用现有的HTTP proxy的传输方式来代理HTTPS流量，那么就会变成浏览器和代理握手跑TLS，代理拿到明文的请求报文，代理和网站握手跑TLS。
    但是代理没有，也不可能有网站的私钥证书，所以这么做会导致浏览器和代理之间的TLS无法建立，证书校验根本通不过。
    HTTP tunnel以及CONNECT报文解决了这个问题，代理服务器不再作为中间人，不再改写浏览器的请求，而是把浏览器和远端服务器之间通信的数据原样透传，这样浏览器就可以直接和远端服务器进行TLS握手并传输加密的数据。

HTTP tunnel的工作流程是什么样的？
    普通的一次HTTP请求，header部分以连续两组CRLF（\r\n）作为标记结束，如果后面还有内容，也就是content部分的话，需要在header里面加入Content-Length头，值为content部分有多长，通信的对方（无论是服务器，还是接收服务器返回结果时的客户端）会按照这个长度来读后面那么多个byte，数写错了就跑飞了（笑）
    对于CONNECT报文的请求，是没有content部分的，只有Request-Line和header。Request-Line和header均为仅供代理服务器使用的，不能传给远端服务器。请求的header部分一旦结束（连续的两组CRLF），后面所有的数据都被视为应该发给远端服务器（网站）的数据，代理需要把它们直接转发，而且不限长度，直到从客户端的TCP读通道关闭。
    对于CONNECT报文的返回值，代理服务器在和远端服务器成功建立连接后，可以（标准说的是可以，但是一般都会）向客户端（浏览器）返回任意一个2xx状态码，此时表示含义是和远端服务器建立连接成功，这个2xx返回报文的header部分一旦结束（连续的两组CRLF），后面所有的数据均为远端服务器返回的数据，同理代理会直接转发远端服务器的返回数据给客户端，直到从远端服务器的TCP读通道关闭。

HTTP tunnel与SOCKS5代理有什么关系和区别？
    其实HTTP代理服务器对于CONNECT报文的行为和SOCKS5代理服务器（RFC1928 ，这个很短）的行为已经非常想象了。

其中相同的地方：
都能在请求中指明要请求的目标和端口。
都会建立能传输任何流量的TCP通道，代理服务器对流量内容不关心。
都是在报文前部的描述信息结束后，将后续所有数据视为转发的客户端和服务端数据，直到通道关闭。
都可以进行客户端的身份验证，而且有多重身份验证协议可选。

不同的地方：
SOCKS5的报文无论请求还是返回，内容均是固定的。而CONNECT报文作为HTTP/1.1的报文之一，当然也包括了HTTP/1.1可以传输任何自定义header的功能，虽然一般代理不会响应CONNECT报文上的非标准header，但是自己实现一个客户端和服务器通过header传输一些其它数据也是符合标准的。
SOCKS5 request报文中的address，根据address type（ATYP）字节的值，可以显式声明为IPv4地址、IPv6地址、域名（domain）。而CONNECT报文中的Request-URI根据RFC要求必须为authority形式（即没有http://前缀，只包含host，以及可选的port，其中host和port要用":"分隔），也就是说CONNECT报文不会显式的区分IP地址和域名，均作为host传输。
由于CONNECT报文还是http，所以也可以跑在TLS里，也就是说客户端和代理跑一层TLS，这一层里面客户端和远端服务器再跑一层TLS。这样当代理服务器需要用户名密码验证，而验证方式又是Basic时，可以通过TLS来保护代理请求报文中的明文用户名密码。

最后吐一口槽……虽然HTTP代理原理并不复杂，但是相比 SOCKS5代理，实现HTTP代理不但RFC字多，RFC多，而且还有后面RFC覆盖前面或前面的一部分的情况，需要消耗大量时间读多个版本来确认各种corner case，以及到底要不要兼容历史情况…………（微笑）