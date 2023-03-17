[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/xizixuejie/canal-spring/blob/master/LICENSE)

# Canal-Spring

便于使用的canal-springboot客户端

## 快速开始

0. 配置canal服务基本信息

    ```yaml
    canal:
      server: 127.0.0.1:11111
      destination: example
    ```

1. 在启动类添加注解 `@EnableCanalListener`

   `value`属性或者`basePackages` 属性指定扫描包路径。

2. 自定义CanalListener 实现 `io.xzxj.canal.core.listener.EntryListener` 接口
3. 实现类上增加注解 `@CanalListener(schemaName = "${database}",value = "${table_name}")` 

   `${database}` 监听数据库名

    `${table_name} ` 监听表名

   接口泛型是对应的实体类
4. 实现 `insert`  、 `update` 或者 `delete` 方法来监听你想做的操作



## 功能

打勾的是已实现的，未打勾的是以后会实现的

- [x] 实体类表名和表名自动转换
- [ ] 跟据jpa或者mp注解自动转换实体类属性
- [x] tcp模式
- [x] kafka模式
- [ ] rocketMQ模式
- [ ] rabbitMQ模式
- [ ] pulsarMQ模式

