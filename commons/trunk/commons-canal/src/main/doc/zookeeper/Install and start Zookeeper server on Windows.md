# Install and start Zookeeper server on Windows

These are some instructions to help you install and run Zookeeper Server on a Windows environment.

1. Download Zookeeper from https://zookeeper.apache.org/releases.html

2. Create zookeeper directory (e.g. c:\opt\zookeeper)

3. Extract contents of Zookeeper downloaded file to above directory

4. Create a 'data' directory for Zookeeper (e.g. c:\opt\zookeeper\data)

5. Copy <zookeeperDir>\conf\zoo_sample.cfg to <zookeeperDir>\conf\zoo.cfg

6. Edit zoo.cfg and set dataDir to the 'data' directory created above
   For instance (note forward slashes): 
     dataDir=C:/opt/zookeeper/data
 
7. Edit <zookeeperDir>\conf\log4j.properties:
    - Uncomment the following line by removing # in front: 
      log4j.rootLogger=DEBUG, CONSOLE, ROLLINGFILE 

8. Start Zookeper server by running <zookeeperDir>\bin\zkServer.cmd

9. You should see the server running and a zookeeper.log file in <zookeeperDir>