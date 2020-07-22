#### MyBatis 深入学习项目


### 一、 #{} 与${} 的区别？
- \#{} 是预编译处理，${}是字符串替换。
- Mybatis在处理#{}时，会将sql中的#{}替换为?号，调用PreparedStatement的set方法来赋值；
- Mybatis在处理${}时，就是把${}替换成变量的值。
- 使用#{}可以有效的防止SQL注入，提高系统安全性。


### 二、 MyBatis与Hibernate有哪些不同？
- MyBatis 是基于JBDC的封装， 减少使用JDBC繁琐的API， 可以快速转换查询结果集为实体类。
- Hibernate 通过HQL屏蔽了底层数据库sql差异，增强了程序的可移植性
- 两者都支持懒加载(都是采用代理的方式实现)
- 都支持一级，二级缓存

### 三、 Mapper接口方法是否能重载，工作原理是什么?
- 接口方法不能重载，每个方法及对应的sql 是通过 Mapper.xml 中的命名空间(接口全限名)+方法名作为key唯一确定的
- 执行原理
    1. 创建接口的代理类， 代理采用JDK动态代理
    2. 执行方法， 触发代理方法执行， 这里会把执行的接口方法和对应的SQL脚本Id存到MapperMethod类中（这里会采用享元模式缓存MapperMethod）
    3. 最后再执行sql, 返回结果
 
 
 - Mapper接口注册图示
 
 ![](https://ftp.bmp.ovh/imgs/2020/07/a0867d588e2ad4c4.png)
 
 - Mapper.xml  MapperStatement注册图示
  
 ![](https://ftp.bmp.ovh/imgs/2020/07/719ae0cfad62b352.png)
 
 - Mapper接口调用图示

![](https://ftp.bmp.ovh/imgs/2020/07/6c023df327b749ca.png)
 
 
 
 ### 四、 Mybatis是如何进行分页的？分页插件的原理是什么？