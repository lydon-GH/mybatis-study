package com.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.data.Mock;
import com.entity.Blog;
import com.entity.User;
import com.mapper.BlogMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * 动态sql的测试类
 */
public class BoundSqlTest {
    BlogMapper blogMapper;
    SqlSession session;
    Configuration configuration;
    @Before
    public void init(){
        DruidDataSource druidDataSource=new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
        druidDataSource.setValidationQuery("select 1");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, druidDataSource);
        configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        this.session=sqlSessionFactory.openSession();
        blogMapper=session.getMapper(BlogMapper.class);
    }

    @After
    public void over(){
        session.close();
    }

    @Test
    public void ognlTest(){
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Blog blog=Mock.newBlog();
        //访问属性
        boolean o1=evaluator.evaluateBoolean("id!=null&&author.name!=null",blog);
        System.out.println(o1);
        //调用方法
        boolean o2=evaluator.evaluateBoolean("comments!=null&&comments.size()>0||comments.isEmpty",blog);
        System.out.println(o2);

        //调用list或数组属性中的属性
        boolean o3=evaluator.evaluateBoolean("comments!=null&&comments[0].content!=null",blog);
        System.out.println(o3);
        boolean o4=evaluator.evaluateBoolean("comments!=null&&comments.get(0).content!=null",blog);
        System.out.println(o4);
        //遍历集合
        Iterable<?> comments=evaluator.evaluateIterable("comments",blog);
        for (Object comment : comments) {
            System.out.println(comment);
        }
    }

    @Test
    public void ifTest(){
        User user = new User();
        user.setName("哈哈");
        user.setId(123);
        user.setAge(11);
        DynamicContext dynamicContext = new DynamicContext(configuration,user);

        //以下会做示例演示动态sql节点解析过程
        //静态文本节点
        new StaticTextSqlNode("select * from user").apply(dynamicContext);

        IfSqlNode i1=new IfSqlNode(new StaticTextSqlNode("and id=#{id}"),"id!=null");
        IfSqlNode i2=new IfSqlNode(new StaticTextSqlNode("or name=#{name}"),"name!=null");
        MixedSqlNode mixedSqlNode=new MixedSqlNode(Arrays.asList(i1,i2));
        WhereSqlNode whereSqlNode=new WhereSqlNode(configuration,mixedSqlNode);
        whereSqlNode.apply(dynamicContext);
        System.out.println(dynamicContext.getSql());


    }
}
