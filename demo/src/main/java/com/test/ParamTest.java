package com.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.entity.Blog;
import com.mapper.BlogMapper;
import com.query.BlogQuery;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.HashMap;

public class ParamTest {
    BlogMapper blogMapper;
    SqlSession session;
    @Before
    public void init(){
        DruidDataSource druidDataSource=new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
        druidDataSource.setValidationQuery("select 1");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, druidDataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession session=sqlSessionFactory.openSession();
         blogMapper=session.getMapper(BlogMapper.class);
    }

    @After
    public void over(){
        session.close();
    }

    @Test
    public void singleTest(){
        //blogMapper.selectBlog(1);
        //
//        HashMap<String,Integer> hashMap = new HashMap();
//        hashMap.put("id",1);
//        hashMap.put("name",1);
//        blogMapper.selectBlogByMap(hashMap);

        HashMap<String,Integer> hashMap = new HashMap();
        hashMap.put("id",1);
        hashMap.put("name",1);
        BlogQuery blogQuery=new BlogQuery();
        blogQuery.setName("张三");
        blogMapper.selectBlogByParamAndObject(1,blogQuery);



    }


}
