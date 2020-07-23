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

1. Mybatis 使用 RowBounds 对象进行分页，它是针对 ResultSet 结果集执行的内存分页，而非物理分页。
2. 可以在 sql 内直接书写带有物理分页的参数来完成物理分页功能，也可以使用分页插件来完成物理分页。
3. 分页插件的基本原理是使用 Mybatis 提供的插件接口，实现自定义插件，在插件的拦截方法内拦截待执行的 sql，然后重写 sql，根据 dialect 方言，添加对应的物理分页语句和物理分页参数。
4. MyBatis 的分页使用例子

- 接口定义

```
public interface UserMapper {
   List<User> selectPage(RowBounds rowBounds);
}
```

- xml

```
<select id="selectPage" resultType="cn.hdj.mybatis.example.entity.User">
        select * from user
</select>
```

- 调用

```java
public class SqlSessionFactoryBuildWithXml {
    public static void main(String[] args) throws IOException {
        //读取配置文件
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //构造SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建SqlSession
        try (SqlSession session = sqlSessionFactory.openSession()) {
            //从第二条记录开始，取一条
            //RowBounds(int offset, int limit)
            List<User> list = mapper.selectPage(new RowBounds(1, 1));
            System.out.println(list);
        }
    }
}
```

- 内存分页源码

```
//内存分页源码
//org.apache.ibatis.executor.resultset.DefaultResultSetHandler
private void skipRows(ResultSet rs, RowBounds rowBounds) throws SQLException {
   if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY) {
     if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET) {
       rs.absolute(rowBounds.getOffset());
     }
   } else {
     for (int i = 0; i < rowBounds.getOffset(); i++) {
       if (!rs.next()) {
         break;
       }
     }
   }
}
```

### 五、Mybatis 是如何将 sql 执行结果封装为目标对象并返回的？都有哪些映射形式？

#### 1.映射形式

- 第一种是使用 <resultMap> 标签，逐一定义数据库列名和对象属性名之间的映射关系。
- 第二种是使用 sql 列的别名功能，将列的别名书写为对象属性名。

#### 2. 以下了解结果集映射处理过程

> 创建返回类型对象

- 1、如果有注册对应的 TypeHandler， 使用 TypeHandler 获取映射对象(实际上一般为基本数据类型及其包装类)
- 2、对于使用有参构造函数的映射对象
  Bean 对象定义

```java
@ToString
public class User2 {
    private Integer id;
    private String name;
    private Integer age;
    //需要使用@Param("id") 注解自定义参数名称, 与映射集中一致
    public User2(@Param("id") Integer id, @Param("name") String name, @Param("age") Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
```

Mapper.xml 文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.hdj.mybatis.example.dao1.UserMapper">

<!--定义映射-->
    <resultMap id="constructorMapper" type="cn.hdj.mybatis.example.entity.User2">
        <constructor>
            <idArg name="id" column="id" javaType="int"></idArg>
            <arg name="name" column="name" javaType="string"></arg>
            <arg name="age" column="age" javaType="int"></arg>
        </constructor>
    </resultMap>
<!--引用结果映射-->
    <select id="selectAll2" resultMap="constructorMapper">
        select * from user
    </select>
</mapper>
```

源码查看

```
//org/apache/ibatis/executor/resultset/DefaultResultSetHandler.java

  Object createParameterizedResultObject(ResultSetWrapper rsw, Class<?> resultType, List<ResultMapping> constructorMappings,
                                         List<Class<?>> constructorArgTypes, List<Object> constructorArgs, String columnPrefix) {
    boolean foundValues = false;
    //遍历从resultMap 中解析的构造函数参数
    for (ResultMapping constructorMapping : constructorMappings) {
      //获取参数类型
      final Class<?> parameterType = constructorMapping.getJavaType();
      //获取列名称
      final String column = constructorMapping.getColumn();
      final Object value;
      try {
        //内嵌查询Id
        if (constructorMapping.getNestedQueryId() != null) {
          //从数据库查询参数内嵌构造参数
          value = getNestedQueryConstructorValue(rsw.getResultSet(), constructorMapping, columnPrefix);
        } else if (constructorMapping.getNestedResultMapId() != null) {
          //处理内嵌的映射参数
          final ResultMap resultMap = configuration.getResultMap(constructorMapping.getNestedResultMapId());
          value = getRowValue(rsw, resultMap, getColumnPrefix(columnPrefix, constructorMapping));
        } else {
          //简单参数
          final TypeHandler<?> typeHandler = constructorMapping.getTypeHandler();
          value = typeHandler.getResult(rsw.getResultSet(), prependPrefix(column, columnPrefix));
        }
      } catch (ResultMapException | SQLException e) {
        throw new ExecutorException("Could not process result for mapping: " + constructorMapping, e);
      }
      constructorArgTypes.add(parameterType);
      constructorArgs.add(value);
      foundValues = value != null || foundValues;
    }
    //把得到的参数 使用ObjectFactory 通过反射创建对象
    return foundValues ? objectFactory.create(resultType, constructorArgTypes, constructorArgs) : null;
  }
```

- 3、接口或者无参构造函数，使用 ObjectFactory 反射创建对象
- 4、有参构造函数的自动映射

> 映射填充结果集到对象

使用反射把对应的属性设置对应的值

### 六、如何执行批量插入?

1. 在代码中使用 for 循环单条入库

```
//创建SqlSession
try (SqlSession session = state.sqlSessionFactory.openSession(true)) {
    //获取Mapper接口
    UserMapper mapper = session.getMapper(UserMapper.class);
    for (int i = 0; i < 10000; i++) {
        User user = new User();
        user.setName("insertInCode-" + flag + "-" + i);
        user.setAge(new Random().nextInt(100));
        mapper.insert(user);
    }
}
```

2. 使用<forearch> 标签批量插入(注意 sql 长度大小)

```
//创建SqlSession
try (SqlSession session = state.sqlSessionFactory.openSession(true)) {
    //获取Mapper接口
    int flag = state.length;
    List<User> list = new ArrayList<>(state.length);
    UserMapper mapper = session.getMapper(UserMapper.class);
    for (int i = 0; i < 10000; i++) {
        User user = new User();
        user.setName("forearch-" + flag + "-" + i);
        user.setAge(new Random().nextInt(100));
        list.add(user);
    }
    mapper.batchInsert(list);
}

<insert id="batchInsert">
    INSERT INTO user(name,age) VALUES
    <foreach collection="list" separator="," item="item">
        (#{item.name},#{item.age})
    </foreach>
</insert>
```

3. 使用批量模式执行器，手动提交方式

```
//创建SqlSession
try (SqlSession session = state.sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
    //获取Mapper接口
    UserMapper mapper = session.getMapper(UserMapper.class);
    for (int i = 0; i < 10000; i++) {
        User user = new User();
        user.setName("batch-insert-" + flag + "-" + i);
        user.setAge(new Random().nextInt(100));
        mapper.insert(user);
    }
    session.commit();
}
```

[代码测试 PreformanceBatchInsertWithMyBatis.java](src/main/java/cn/hdj/mybatis/example/PreformanceBatchInsertWithMyBatis.java)

### 七、在 mapper 中如何传递多个参数?

1. 使用注解@param
2. 将多个参数封装成 map

MyBatis 是使用 ParamNameResolver 解析参数。

> 解析参数名称

org.apache.ibatis.reflection.ParamNameResolver.ParamNameResolver

在绑定 Mapper 接口和 Mapper.xml 文件，实例化 ParamNameResolver 时，会遍历接口方法中的参数，以参数位置 index 为 key,

- 如果参数有注解@Param 则以定义的名称为 value 存入 TreeMap 中，
- 如果没有注解@Param，但是 Configuration 开启使用实际参数名称， 则以在方法中的参数名称为 value 存入 TreeMap 中
- 没有注解@Param，也没有开启使用实际参数名称， 则以参数下标作为 value

> 转换 Mapper 接口参数为对应的 Sql 命令参数

org.apache.ibatis.reflection.ParamNameResolver.getNamedParams

- 如果传入来的参数没有注解，数量只有一个，对参数进行包装
  - 如果参数的类型是集合类型，则创建一个 HashMap 保存，map.put("collection", object);
  - 参数类型是 List 类型，则 map.put("list", object);
  - 参数类型是数组类型，则 map.put("array", object);
  - 都不是，则返回原对象
- 否则，遍历事先保存在 TreeMap 的参数名，
  - 把对应的参数名和参数值存入 HashMap 中
  - 同时再往 HashMap 中添加通用参数名称(param1, param2, ...), param1 对应方法中第一个参数

从上面可以看出，接口方法的参数最终会被 MyBatis 包装为 HashMap 给 Mapper.xml 调用

### 八、Mybatis 是否支持延迟加载？如果支持，它的实现原理是什么？

支持
原理： 本质上都是采用代理的形式实现。 MyBatis 采用 CGLib 动态代理实现，在获取目标对象时，对其进行拦截返回代理类
并且先保存好对应的结果 sql 语句，而不是立即执行， 当最终请求获取结果时才进行查询并返回。

### 九、 MyBatis 的一级，二级缓存

- 图示

![一级缓存](https://s1.ax1x.com/2020/07/23/UL77Gt.png)

![二级缓存](https://s1.ax1x.com/2020/07/23/ULHseg.png)

- 一级缓存: 基于 PerpetualCache 的 HashMap 本地缓存，其存储作用域为 Session，当 Session flush 或 close 之后，该 Session 中的所有 Cache 就将清空，默认打开一级缓存。
- 二级缓存与一级缓存其机制相同，默认也是采用 PerpetualCache，HashMap 存储，不同在于其存储作用域为 Mapper(Namespace)，并且可自定义存储源，如 Ehcache。默认不打开二级缓存，要开启二级缓存，使用二级缓存属性类需要实现 Serializable 序列化接口(可用来保存对象的状态),可在它的映射文件中配置 <cache/> ；
- 对于缓存数据更新机制，当某一个作用域(一级缓存 Session/二级缓存 Namespaces)的进行了 C/U/D 操作后，默认该作用域下所有 select 中的缓存将被 clear。

### 十、简述 Mybatis 的插件运行原理，以及如何编写一个插件。

> 运行原理

- Mybatis 仅可以编写针对 ParameterHandler、ResultSetHandler、StatementHandler、Executor 这 4 种接口的插件
- Mybatis 使用 JDK 的动态代理，为需要拦截的接口生成代理对象以实现接口方法拦截功能，每当执行这 4 种接口对象的方法时，就会进入拦截方法，具体就是 InvocationHandler 的 invoke()方法当然，只会拦截那些你指定需要拦截的方法。

> 编写插件：

- 实现 Mybatis 的 Interceptor 接口并复写 intercept()方法

```java
@Slf4j
@Intercepts({
        //拦截StatementHandler 类中的query方法， args指定方法中的形参类型
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public class SlowSqlInterceptor implements Interceptor {
    private Integer limitSecond;
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        StatementHandler target = (StatementHandler) invocation.getTarget();
        try {
            return invocation.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long cost = end - start;
            if (cost > limitSecond * 1000) {
                BoundSql boundSql = target.getBoundSql();
                log.warn("慢SQL >>{}", boundSql.getSql());
            }
        }
    }
    //返回代理类
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    //设置属性
    @Override
    public void setProperties(Properties properties) {
        String limitSecond = (String) properties.get("limitSecond");
        this.limitSecond = Integer.parseInt(limitSecond);
    }
}
```

- 配置插件

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <plugins>
        <plugin interceptor="cn.hdj.mybatis.example.intercept.SlowSqlInterceptor">
            <property name="limitSecond" value="0"/>
        </plugin>
    </plugins>

   <!--省略-->
</configuration>
```

### 十一、 Spring 和 MyBatis 整合， 事务如何管理(待续....)


### 项目地址
https://github.com/h-dj/Mybatis-Learning

### 参考、

- https://juejin.im/post/5ce25058e51d45105773e63f
- https://juejin.im/post/5eec8ad26fb9a0585f0e9ecc#heading-21
- 《MyBatis3 源码深度解析》
