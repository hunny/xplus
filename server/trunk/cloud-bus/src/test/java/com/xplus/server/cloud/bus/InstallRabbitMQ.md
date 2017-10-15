## Install RabbitMQ

```
Last login: Sat Oct  7 22:43:33 on ttys001
HunnyHu:~ hunnyhu$ brew update
^CHunnyHu:~ hunnyhu$ brew install rabbitmq
Updating Homebrew...
==> Downloading https://homebrew.bintray.com/bottles-portable/portable-ruby-2.3.3.leopard_64.bottle.1.tar.gz
######################################################################## 100.0%
==> Pouring portable-ruby-2.3.3.leopard_64.bottle.1.tar.gz
==> Auto-updated Homebrew!
Updated 2 taps (caskroom/cask, homebrew/core).
No changes to formulae.

==> Installing dependencies for rabbitmq: openssl, jpeg, libpng, libtiff, wxmac, erlang
==> Installing rabbitmq dependency: openssl
==> Downloading https://homebrew.bintray.com/bottles/openssl-1.0.2l.sierra.bott
######################################################################## 100.0%
==> Pouring openssl-1.0.2l.sierra.bottle.tar.gz
==> Caveats
A CA file has been bootstrapped using certificates from the SystemRoots
keychain. To add additional certificates (e.g. the certificates added in
the System keychain), place .pem files in
  /usr/local/etc/openssl/certs

and run
  /usr/local/opt/openssl/bin/c_rehash

This formula is keg-only, which means it was not symlinked into /usr/local,
because Apple has deprecated use of OpenSSL in favor of its own TLS and crypto libraries.

If you need to have this software first in your PATH run:
  echo 'export PATH="/usr/local/opt/openssl/bin:$PATH"' >> ~/.bash_profile

For compilers to find this software you may need to set:
    LDFLAGS:  -L/usr/local/opt/openssl/lib
    CPPFLAGS: -I/usr/local/opt/openssl/include
For pkg-config to find this software you may need to set:
    PKG_CONFIG_PATH: /usr/local/opt/openssl/lib/pkgconfig

==> Summary
🍺  /usr/local/Cellar/openssl/1.0.2l: 1,709 files, 12.2MB
==> Installing rabbitmq dependency: jpeg
==> Downloading https://homebrew.bintray.com/bottles/jpeg-9b.sierra.bottle.tar.
######################################################################## 100.0%
==> Pouring jpeg-9b.sierra.bottle.tar.gz
🍺  /usr/local/Cellar/jpeg/9b: 20 files, 724KB
==> Installing rabbitmq dependency: libpng
==> Downloading https://homebrew.bintray.com/bottles/libpng-1.6.32.sierra.bottl
######################################################################## 100.0%
==> Pouring libpng-1.6.32.sierra.bottle.tar.gz
🍺  /usr/local/Cellar/libpng/1.6.32: 26 files, 1.2MB
==> Installing rabbitmq dependency: libtiff
==> Downloading https://homebrew.bintray.com/bottles/libtiff-4.0.8_4.sierra.bot
######################################################################## 100.0%
==> Pouring libtiff-4.0.8_4.sierra.bottle.tar.gz
🍺  /usr/local/Cellar/libtiff/4.0.8_4: 245 files, 3.4MB
==> Installing rabbitmq dependency: wxmac
==> Downloading https://homebrew.bintray.com/bottles/wxmac-3.0.3.1_1.sierra.bot
######################################################################## 100.0%
==> Pouring wxmac-3.0.3.1_1.sierra.bottle.tar.gz
🍺  /usr/local/Cellar/wxmac/3.0.3.1_1: 810 files, 24.3MB
==> Installing rabbitmq dependency: erlang
==> Downloading https://homebrew.bintray.com/bottles/erlang-20.1.1.sierra.bottl
################################                                          45.0%
curl: (18) transfer closed with 45530960 bytes remaining to read
Error: Failed to download resource "erlang"
Download failed: https://homebrew.bintray.com/bottles/erlang-20.1.1.sierra.bottle.tar.gz
Warning: Bottle installation failed: building from source.
==> Installing dependencies for erlang: autoconf, automake, libtool
==> Installing erlang dependency: autoconf
==> Downloading https://homebrew.bintray.com/bottles/autoconf-2.69.sierra.bottl
######################################################################## 100.0%
==> Pouring autoconf-2.69.sierra.bottle.4.tar.gz
==> Caveats
Emacs Lisp files have been installed to:
  /usr/local/share/emacs/site-lisp/autoconf
==> Summary
🍺  /usr/local/Cellar/autoconf/2.69: 70 files, 3.0MB
==> Installing erlang dependency: automake
==> Downloading https://homebrew.bintray.com/bottles/automake-1.15.1.sierra.bot
######################################################################## 100.0%
==> Pouring automake-1.15.1.sierra.bottle.tar.gz
🍺  /usr/local/Cellar/automake/1.15.1: 131 files, 3.0MB
==> Installing erlang dependency: libtool
==> Downloading https://homebrew.bintray.com/bottles/libtool-2.4.6_1.sierra.bot
######################################################################## 100.0%
==> Pouring libtool-2.4.6_1.sierra.bottle.tar.gz
==> Caveats
In order to prevent conflicts with Apple's own libtool we have prepended a "g"
so, you have instead: glibtool and glibtoolize.
==> Summary
🍺  /usr/local/Cellar/libtool/2.4.6_1: 70 files, 3.7MB
==> Downloading https://github.com/erlang/otp/archive/OTP-20.1.1.tar.gz
==> Downloading from https://codeload.github.com/erlang/otp/tar.gz/OTP-20.1.1
######################################################################## 100.0%
==> ./otp_build autoconf
==> ./configure --disable-silent-rules --prefix=/usr/local/Cellar/erlang/20.1.1
==> make
^C^COne sec, just cleaning up

HunnyHu:~ hunnyhu$ brew install rabbitmq
Updating Homebrew...
==> Auto-updated Homebrew!
Updated 1 tap (homebrew/core).
No changes to formulae.

==> Installing dependencies for rabbitmq: erlang
==> Installing rabbitmq dependency: erlang
==> Downloading https://homebrew.bintray.com/bottles/erlang-20.1.1.sierra.bottl
######################################################################## 100.0%
==> Pouring erlang-20.1.1.sierra.bottle.tar.gz
==> Caveats
Man pages can be found in:
  /usr/local/opt/erlang/lib/erlang/man

Access them with `erl -man`, or add this directory to MANPATH.
==> Summary
🍺  /usr/local/Cellar/erlang/20.1.1: 7,112 files, 276.5MB
==> Installing rabbitmq
==> Downloading https://dl.bintray.com/rabbitmq/binaries/rabbitmq-server-generi
######################################################################## 100.0%
==> /usr/bin/unzip -qq -j /usr/local/Cellar/rabbitmq/3.6.12/plugins/rabbitmq_ma
==> Caveats
Management Plugin enabled by default at http://localhost:15672

Bash completion has been installed to:
  /usr/local/etc/bash_completion.d

To have launchd start rabbitmq now and restart at login:
  brew services start rabbitmq
Or, if you don't want/need a background service you can just run:
  rabbitmq-server
==> Summary
🍺  /usr/local/Cellar/rabbitmq/3.6.12: 199 files, 5.4MB, built in 12 seconds
HunnyHu:~ hunnyhu$ 
```

## 检查安装

- 使用命令rabbitmqctl start_app启动

```
HunnyHu:~ hunnyhu$ rabbitmqctl start_app
Starting node rabbit@localhost

              RabbitMQ 3.6.12. Copyright (C) 2007-2017 Pivotal Software, Inc.
  ##  ##      Licensed under the MPL.  See http://www.rabbitmq.com/
  ##  ##
  ##########  Logs: /usr/local/var/log/rabbitmq/rabbit@localhost.log
  ######  ##        /usr/local/var/log/rabbitmq/rabbit@localhost-sasl.log
  ##########
              Starting broker...
 completed with 9 plugins.
HunnyHu:~ hunnyhu$ 
```

- 访问地址：http://localhost:15672/

- 使用默念的用户和密码(guest/guest)登录。

## rabbitmq命令

- 查看状态
```
rabbitmqctl status
```

- 查看所有队列信息
```
rabbitmqctl list_queues
```

- 关闭应用

```
rabbitmqctl stop_app
```

- 启动应用，和上述关闭命令配合使用，达到清空队列的目的
```
rabbitmqctl start_app
```

- 清除所有队列
```
rabbitmqctl reset
```

- 更多用法及参数，可以执行如下命令查看
```
rabbitmqctl
```

- 常用操作命令

```
（1）关闭rabbitmq: rabbitmqctl stop_app
（2）还原： rabbitmqctl reset
（3）启动： rabbitmqctl start_app
（4）添加用户： rabbitmqctl add_user root root
（5）设置权限：rabbitmqctl set_permissions -p / root ".*" ".*" ".*"
（6）查看用户： rabbitmqctl list_users
```

## RabbitMQ常用操作知识

1. 用户管理

用户管理包括增加用户，删除用户，查看用户列表，修改用户密码。

相应的命令

(1) 新增一个用户

rabbitmqctl  add_user  Username  Password

(2) 删除一个用户

rabbitmqctl  delete_user  Username

(3) 修改用户的密码

rabbitmqctl  change_password  Username  Newpassword

(4) 查看当前用户列表

rabbitmqctl  list_users

2. 用户角色

按照个人理解，用户角色可分为五类，超级管理员, 监控者, 策略制定者, 普通管理者以及其他。

(1) 超级管理员(administrator)

可登陆管理控制台(启用management plugin的情况下)，可查看所有的信息，并且可以对用户，策略(policy)进行操作。

(2) 监控者(monitoring)

可登陆管理控制台(启用management plugin的情况下)，同时可以查看rabbitmq节点的相关信息(进程数，内存使用情况，磁盘使用情况等)

(3) 策略制定者(policymaker)

可登陆管理控制台(启用management plugin的情况下), 同时可以对policy进行管理。但无法查看节点的相关信息(上图红框标识的部分)。

与administrator的对比，administrator能看到这些内容

(4) 普通管理者(management)

仅可登陆管理控制台(启用management plugin的情况下)，无法看到节点信息，也无法对策略进行管理。

(5) 其他

无法登陆管理控制台，通常就是普通的生产者和消费者。

了解了这些后，就可以根据需要给不同的用户设置不同的角色，以便按需管理。

设置用户角色的命令为：

rabbitmqctl  set_user_tags  User  Tag

User为用户名， Tag为角色名(对应于上面的administrator，monitoring，policymaker，management，或其他自定义名称)。

也可以给同一用户设置多个角色，例如

rabbitmqctl  set_user_tags  hncscwc  monitoring  policymaker

3. 用户权限

用户权限指的是用户对exchange，queue的操作权限，包括配置权限，读写权限。配置权限会影响到exchange，queue的声明和删除。读写权限影响到从queue里取消息，向exchange发送消息以及queue和exchange的绑定(bind)操作。

例如： 将queue绑定到某exchange上，需要具有queue的可写权限，以及exchange的可读权限；向exchange发送消息需要具有exchange的可写权限；从queue里取数据需要具有queue的可读权限。详细请参考官方文档中"How permissions work"部分。

相关命令为：

(1) 设置用户权限

rabbitmqctl  set_permissions  -p  VHostPath  User  ConfP  WriteP  ReadP

(2) 查看(指定hostpath)所有用户的权限信息

rabbitmqctl  list_permissions  [-p  VHostPath]

(3) 查看指定用户的权限信息

rabbitmqctl  list_user_permissions  User

(4)  清除用户的权限信息

rabbitmqctl  clear_permissions  [-p VHostPath]  User

