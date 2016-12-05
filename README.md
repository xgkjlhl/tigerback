# 小贷管家后端项目

![](https://img.shields.io/badge/tag-v0.3.1-yellow.svg)  ![](https://img.shields.io/badge/%E4%B8%9C%E5%8D%8A%E7%90%83%E6%9C%80%E5%A5%BD%E7%9A%84%E8%B4%B7%E6%AC%BE%E7%B3%BB%E7%BB%9F-building-blue.svg)

该项目是贷管网后台，主要为前端客户端提供稳定高可用的 API 服务。项目 API 严格按照 Rest 规范设计，方便维护和扩展。

## 项目环境配置

开发机器上需要准备以下环境：

* gradle 2.4 +
* JDK 1.8 +
* Mysql 5.5.0+
* IDEA


## 开发环境配置

### 1. 导入 gradle 项目到IDEA

### 2. 配置数据库 mysql.properties 和 gralde.properties 文件
* mysql.properties // 放在项目dal目录的resource文件夹中

```
# c3p0.X
c3p0.driverClassName=com.mysql.jdbc.Driver
c3p0.url=jdbc:mysql://localhost:3306/nevermore_dev?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8  //改我
c3p0.username=xxx  //改我
c3p0.password=xxx  //改我
c3p0.MaxPoolSize=20
c3p0.MinPoolSize=10


```

* gradle.properties //放在项目根目录下

```
flyway.user=xx  //改我
flyway.password=xx  //改我
flyway.url=jdbc:mysql://localhost:3306
flyway.schemas=nevermore_dev


```



### 3. 新建数据库，migration 数据

```
	gradle flywayMigrate -i //项目根目录执行
```

### 4. run APIMain.java

当你看到 console 报一下内容时候，项目就已经跑起来了：


```
 Http11NioProtocol: Initializing ProtocolHandler ["http-nio-8080"]
 Http11NioProtocol: Starting ProtocolHandler ["http-nio-8080"]
 NioSelectorPool: Using a shared selector for servlet write/read
 TomcatEmbeddedServletContainer: Tomcat started on port(s): 8080 (http)
 APIMain: Started APIMain in 28.597 seconds (JVM running for 29.326)
```

## 开发注意事项

* [代码规范](https://github.com/404Design/404-blog/blob/master/rules/java-code-style.md)
* [用RAML表达 API 需求](http://blog.guoyiliang.com/2015/04/23/raml-init/)
* 开放封闭原则（尽量扩展，而不是修改）
