
## SpringBatch组成

| 名称 | 用途 |
| --- | --- |
| JobRepository | 用来注册Job的容器 |
| JobLauncher | 用来启动Job的接口 |
| Job | 实际要执行的任务，包含一个或多个Step |
| Step | Step步骤包含ItemReader、ItemProcessor和ItemWriter |
| ItemReader | 用来读取数据的接口 |
| ItemProcessor | 用来处理数据的接口 |
| ItemWriter | 用来输出数据的接口 |

