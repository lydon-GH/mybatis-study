package com.test;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.entity.Blog;
import com.entity.Comment;
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

import java.util.HashMap;

/**
 * 嵌套查询示例
 *
 */
public class NestQueryTest {
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
        this.session=sqlSessionFactory.openSession();
        blogMapper=session.getMapper(BlogMapper.class);
    }

    @After
    public void over(){
        session.close();
    }

    @Test
    public void singleTest(){
        Blog blog=blogMapper.selectBlogById(1);
        System.out.println("博客名："+blog.getTitle());
        System.out.println("作者："+blog.getAuthor().getName());
        System.out.println("评论列表：");
        for(Comment comment:blog.getComments()){
//            System.out.println(comment.getUser().getName()+"在"+ DateUtil.formatDate(comment.getDate())+"评论了本博客："+comment.getContent());
            System.out.println(DateUtil.formatDate(comment.getDate())+"评论了本博客："+comment.getContent());
        }
        System.out.println(blog.getComments());
    }
}
