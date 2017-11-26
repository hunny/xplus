# Markdown

## 目标

* 使用地址直接访问.md文件。
  - 例如：http://localhost:8080/Users/xxs/document/ok.md。
* 配置一个基本目录地址，扫描该目录下所有.md文件。
  - 例如：应用启动时，扫描该目录下的.md文件到内存数据库中方便检索。

## 实现方式

* 使用servlet配置拦截所有扩展名称为.md的请求。
* 使用servlet渲染出一个.md请求的基本页面。
  - 使用freemark模板，生成基本页面。
* 使用上述的基本页面请求加载.md文件的markdown转化文件。
* 异步渲染加载展示。
