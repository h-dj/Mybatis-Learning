#### MyBatis 深入学习项目

### 一、 #{} 与\${} 的区别？

- \#{} 是预编译处理，\${}是字符串替换。
- Mybatis 在处理#{}时，会将 sql 中的#{}替换为?号，调用 PreparedStatement 的 set 方法来赋值；
- Mybatis 在处理 ${}时，就是把 ${}替换成变量的值。
- 使用#{}可以有效的防止 SQL 注入，提高系统安全性。

### 二、 MyBatis 与 Hibernate 有哪些不同？

- MyBatis 是基于 JBDC 的封装， 减少使用 JDBC 繁琐的 API， 可以快速转换查询结果集为实体类。
- Hibernate 通过 HQL 屏蔽了底层数据库 sql 差异，增强了程序的可移植性
- 两者都支持懒加载(都是采用代理的方式实现)
- 都支持一级，二级缓存

### 三、 Mapper 接口方法是否能重载，工作原理是什么?

- 接口方法不能重载，每个方法及对应的 sql 是通过 Mapper.xml 中的命名空间(接口全限名)+方法名作为 key 唯一确定的
- 执行原理
  1. 创建接口的代理类， 代理采用 JDK 动态代理
  2. 执行方法， 触发代理方法执行， 这里会把执行的接口方法和对应的 SQL 脚本 Id 存到 MapperMethod 类中（这里会采用享元模式缓存 MapperMethod）
  3. 最后再执行 sql, 返回结果

* Mapper 接口注册图示

![](https://ftp.bmp.ovh/imgs/2020/07/a0867d588e2ad4c4.png)

- Mapper.xml MapperStatement 注册图示

![](https://ftp.bmp.ovh/imgs/2020/07/719ae0cfad62b352.png)

- Mapper 接口调用图示

![](https://ftp.bmp.ovh/imgs/2020/07/6c023df327b749ca.png)

### 四、 Mybatis 是如何进行分页的？分页插件的原理是什么？


### 五、Mybatis是如何将sql执行结果封装为目标对象并返回的？都有哪些映射形式？
- 第一种是使用 <resultMap> 标签，逐一定义数据库列名和对象属性名之间的映射关系。
- 第二种是使用sql列的别名功能，将列的别名书写为对象属性名。

- 以下了解结果集映射处理过程(待续...)

```
private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix) throws SQLException {
    final ResultLoaderMap lazyLoader = new ResultLoaderMap();
    Object rowValue = createResultObject(rsw, resultMap, lazyLoader, columnPrefix);
    if (rowValue != null && !hasTypeHandlerForResultObject(rsw, resultMap.getType())) {
      final MetaObject metaObject = configuration.newMetaObject(rowValue);
      boolean foundValues = this.useConstructorMappings;
      if (shouldApplyAutomaticMappings(resultMap, false)) {
        foundValues = applyAutomaticMappings(rsw, resultMap, metaObject, columnPrefix) || foundValues;
      }
      foundValues = applyPropertyMappings(rsw, resultMap, metaObject, lazyLoader, columnPrefix) || foundValues;
      foundValues = lazyLoader.size() > 0 || foundValues;
      rowValue = foundValues || configuration.isReturnInstanceForEmptyRow() ? rowValue : null;
    }
    return rowValue;
  }
```


### 六、如何执行批量插入?
1. 在代码中使用for循环单条入库
2. 使用<forearch> 标签批量插入
3. 在代码中for循环插入到内存中，再使用手动commit方式入库


### 七、在mapper中如何传递多个参数?
1. 使用注解@param
2. 将多个参数封装成map


### 八、Mybatis是否支持延迟加载？如果支持，它的实现原理是什么？
支持
原理： 本质上都是采用代理的形式实现。 MyBatis采用CGLib动态代理实现，在获取目标对象时，对其进行拦截返回代理类
并且先保存好对应的结果sql语句，而不是立即执行， 当最终请求获取结果时才进行查询并返回。

### 九、Mapper编写有哪几种方式？

### 十、简述Mybatis的插件运行原理，以及如何编写一个插件。


参考、
• https://juejin.im/post/5ce25058e51d45105773e63f