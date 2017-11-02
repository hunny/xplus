Git的基本操作

初始化操作
    $ git config -global user.name <name> #设置提交者名字
    $ git config -global user.email <email> #设置提交者邮箱
    $ git config -global core.editor <editor> #设置默认文本编辑器
    $ git config -global merge.tool <tool> #设置解决合并冲突时差异分析工具
    $ git config -list #检查已有的配置信息
创建新版本库
    $ git clone <url> #克隆远程版本库
    $ git init #初始化本地版本库
修改和提交
    $ git add . #添加所有改动过的文件
    $ git add <file> #添加指定的文件
    $ git mv <old> <new> #文件重命名
    $ git rm <file> #删除文件
    $ git rm -cached <file> #停止跟踪文件但不删除
    $ git commit -m <file> #提交指定文件
    $ git commit -m “commit message” #提交所有更新过的文件
    $ git commit -amend #修改最后一次提交
    $ git commit -C HEAD -a -amend #增补提交（不会产生新的提交历史纪录）
查看提交历史
    $ git log #查看提交历史
    $ git log -p <file> #查看指定文件的提交历史
    $ git blame <file> #以列表方式查看指定文件的提交历史
    $ gitk #查看当前分支历史纪录
    $ gitk <branch> #查看某分支历史纪录
    $ gitk --all #查看所有分支历史纪录
    $ git branch -v #每个分支最后的提交
    $ git status #查看当前状态
    $ git diff #查看变更内容
撤消操作
    $ git reset -hard HEAD #撤消工作目录中所有未提交文件的修改内容
    $ git checkout HEAD <file1> <file2> #撤消指定的未提交文件的修改内容
    $ git checkout HEAD. #撤消所有文件
    $ git revert <commit> #撤消指定的提交
分支与标签
    $ git branch #显示所有本地分支
    $ git checkout <branch/tagname> #切换到指定分支或标签
    $ git branch <new-branch> #创建新分支
    $ git branch -d <branch> #删除本地分支
    $ git tag #列出所有本地标签
    $ git tag <tagname> #基于最新提交创建标签
    $ git tag -d <tagname> #删除标签
合并与衍合
    $ git merge <branch> #合并指定分支到当前分支
    $ git rebase <branch> #衍合指定分支到当前分支
远程操作
    $ git remote -v #查看远程版本库信息
    $ git remote show <remote> #查看指定远程版本库信息
    $ git remote add <remote> <url> #添加远程版本库
    $ git fetch <remote> #从远程库获取代码
    $ git pull <remote> <branch> #下载代码及快速合并
    $ git push <remote> <branch> #上传代码及快速合并
    $ git push <remote> : <branch>/<tagname> #删除远程分支或标签
    $ git push -tags #上传所有标签
    
参考资料

[Pro Git（中文版）](http://git.oschina.net/progit/)
[简明使用指导](http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000)