# 小贷管家后端项目

这里是贷管网后端管理平台，主要为管理员提供配置和管理的便捷工具。

## 项目环境配置

开发机器上需要准备以下环境：

* gradle 2.4 +
* JDK 1.8 +
* Mysql 5.5.0+
* IDEA

## 开发约定
以下部分主要针对于后台系统开发过程中***提高开发效率***进行的约定

### 1. controller负责组织数据和页面，前端只负责将页面放到其合适的位置



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

### 4. 下载plugins.zip,放置到tiger-web-mng/src/main/resources/static目录下

```
	下载地址：teambition > 贷管网 > 文件库 > 3. 后台 > 后台管理系统 > 管理系统前端
```


### 5. run TigerMngMain.java

当你看到 console 报以下内容时候，项目就已经跑起来了：
