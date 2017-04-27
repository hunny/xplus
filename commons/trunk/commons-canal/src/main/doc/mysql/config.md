#配置mysql

## 检查配置

+ 检查log-bin
```
mysql> show variables like 'log_bin';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| log_bin       | ON    |
+---------------+-------+
```

+ 检查binlog-format
```
mysql> show variables like 'binlog_format';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| binlog_format | ROW   |
+---------------+-------+
```

+ 检查server_id
```
mysql> show variables like 'server_id';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| server_id     | 3     |
+---------------+-------+
```

## 配置文件中设置

+ 配置mysqld服务端
```
[mysqld]  
log-bin=mysql-bin #添加这一行就ok  
binlog-format=ROW #选择row模式  
server_id=1 #配置mysql replaction需要定义，不能和canal的slaveId重复  
```

+ 创建用户
```
CREATE USER canal IDENTIFIED BY 'canal';    
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';  
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
SET PASSWORD FOR 'canal'@'%' = PASSWORD('canal');
FLUSH PRIVILEGES; 
```

+ 通过grants查询权限
```
mysql> show grants for 'canal';
+--------------------------------------------+
| Grants for canal@%                         |
+--------------------------------------------+
| GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' |
+--------------------------------------------+
```

+ 查看当前数据库的日志位点
```
mysql> show master status;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000001 |     1216 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
```

