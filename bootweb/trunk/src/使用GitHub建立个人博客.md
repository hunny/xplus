# 利用GitHub搭建个人博客

利用GitHub的Page功能和Hexo等静态博客来搭建个人博客

## 安装Node.js+Git

如果你安装了Homebrew，可在terminal中按照如下操作

```
brew update
brew install node
```

之后我们需要安装git来为部署做准备，具体安装方法可以见此链接，[Git and GitHub](http://sourabhbajaj.com/mac-setup/Git/README.html)

## 安装Hexo

创建一个新的文件夹，如blog，然后再该路径下执行如下命令：

```
$ npm install -g hexo-cli
```

使用淘宝镜像源：

```
$ npm --registry https://registry.npm.taobao.org install -g hexo-cli
```

然后我们可以用下列指令来初始化

```
$ hexo init
```

现在已完成hexo的初始化工作，接下来需要关联github。

## 关联Github建立Repository

仓库名必须为"your_name.github.io"，如我的就为"code7.github.io",这个写法是固定的。然后查看刚刚所建blog文件夹中有一个_config.yml文件，打开它将最下方的字段改成如下：

```
deploy:
type: git
repo: https://github.com/****/****.github.io.git
branch: master
```

repo后面的地址为你刚刚创建的Repository的clone地址

## 安装hexo部署插件

```
npm install hexo-deployer-git --save
```

接着执行

```
hexo deploy
```

就可以完成部署，在浏览器中输入 https：//.github.io 就能打开你的个人博客了。

整个过程简单易懂，而且hexo的写作方法是markdown，并且支持数学公式，十分方便。最后推荐几款好看的主题。

## hexo常用指令

hexo g 生成静态网页

hexo clean 删除缓存

hexo deploy 部署到git

hexo server 部署到本地（调试）

hexo new "文章名" 新建文章

## 模板推荐

### Next

https://github.com/iissnan/hexo-theme-next

预览：http://notes.iissnan.com/

### Casper

https://github.com/TryGhost/Casper

### Material

https://github.com/viosey/hexo-theme-material

预览：https://blog.viosey.com/

