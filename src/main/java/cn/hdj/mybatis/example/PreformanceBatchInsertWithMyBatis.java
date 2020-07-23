package cn.hdj.mybatis.example;

import cn.hdj.mybatis.example.dao1.UserMapper;
import cn.hdj.mybatis.example.entity.User;
import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/23 上午11:44
 * @description: 测试 MyBatis 的批量插入
 */
@Slf4j
public class PreformanceBatchInsertWithMyBatis {


    @State(Scope.Benchmark)
    public static class MyState {
        /**
         * 测试从100 到 1000000 条的性能
         */
        @Param(value = {"1000", "10000", "100000"})
        private int length;

        private SqlSessionFactory sqlSessionFactory;

        @Setup
        public void init() throws IOException {
            //useServerPrepStmts=false&rewriteBatchedStatements=true&useCompression=true
            //构建数据源
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8");
            dataSource.setUser("root");
            dataSource.setPassword("123456");
            //事务工厂
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            //环境容器
            Environment environment = new Environment("development", transactionFactory, dataSource);
            //配置
            Configuration configuration = new Configuration(environment);
            //添加Mapper接口
            configuration.addMapper(UserMapper.class);
            //解析Mapper.xml文件
            //自定义xml文件位置
            String resource = "mapper/UserMapper.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            mapperParser.parse();
            //初始化SqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        }
    }

    @Benchmark
    public void ForInsertInCode(MyState state) {
        //创建SqlSession
        try (SqlSession session = state.sqlSessionFactory.openSession(true)) {
            //获取Mapper接口
            int flag = state.length;
            UserMapper mapper = session.getMapper(UserMapper.class);
            for (int i = 0; i < state.length; i++) {
                User user = new User();
                user.setName("insertInCode-" + flag + "-" + i);
                user.setAge(new Random().nextInt(100));
                mapper.insert(user);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Benchmark
    public void BatchInsertInCode(MyState state) {
        //创建SqlSession
        try (SqlSession session = state.sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            //获取Mapper接口
            int flag = state.length;
            UserMapper mapper = session.getMapper(UserMapper.class);
            for (int i = 0; i < state.length; i++) {
                User user = new User();
                user.setName("batch-insert-" + flag + "-" + i);
                user.setAge(new Random().nextInt(100));
                mapper.insert(user);
            }
            session.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }


    }

    @Benchmark
    public void BatchInsertWithForearch(MyState state) {
        //创建SqlSession
        try (SqlSession session = state.sqlSessionFactory.openSession(true)) {
            //获取Mapper接口
            int flag = state.length;
            List<User> list = new ArrayList<>(state.length);
            UserMapper mapper = session.getMapper(UserMapper.class);
            for (int i = 0; i < state.length; i++) {
                User user = new User();
                user.setName("forearch-" + flag + "-" + i);
                user.setAge(new Random().nextInt(100));
                list.add(user);
            }
            mapper.batchInsert(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(PreformanceBatchInsertWithMyBatis.class.getSimpleName())
                .threads(4)
                .forks(1)
                .mode(Mode.AverageTime)
                .warmupIterations(3).warmupTime(TimeValue.seconds(1))
                .measurementIterations(5).measurementTime(TimeValue.seconds(1))
                .timeUnit(TimeUnit.MILLISECONDS)
                .resultFormat(ResultFormatType.JSON)
                .result("mybatis-jmh.json")
                .build();
        new Runner(options).run();
    }
}
