package com.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.entity.Blog;
import com.mapper.BlogMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

public class CacheTest {

    private SqlSessionFactory sqlSessionFactory;
    @Before
    public void init(){
        DataSource dataSource = getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
    public static void main(String[] args) {
        DataSource dataSource = getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession session=sqlSessionFactory.openSession();
        session.clearCache();
        BlogMapper blogMapper=session.getMapper(BlogMapper.class);
        Blog blog=blogMapper.selectBlogById(1);

    }

    public static DataSource getDataSource(){
        DruidDataSource druidDataSource=new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
        druidDataSource.setValidationQuery("select 1");
        return druidDataSource;
    }

    @org.junit.Test
    public void testBySpring(){
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
        BlogMapper blogMapper=context.getBean(BlogMapper.class);
        DataSourceTransactionManager dataSourceTransactionManager= (DataSourceTransactionManager) context.getBean("transactionManager");
        //手动开启事务
        dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        Blog blog1=blogMapper.selectBlogById(1);
        Blog blog2=blogMapper.selectBlogById(1);
        System.out.println(blog1 == blog2);
    }

    @org.junit.Test
    public void testBySecondCache(){
        SqlSession session1=sqlSessionFactory.openSession();
        BlogMapper blogMapper=session1.getMapper(BlogMapper.class);
        Blog blog=blogMapper.selectBlogById(1);
        session1.commit();
        SqlSession session2=sqlSessionFactory.openSession();
        BlogMapper blogMapper2=session1.getMapper(BlogMapper.class);
        Blog blog2=blogMapper2.selectBlogById(1);
    }
}
