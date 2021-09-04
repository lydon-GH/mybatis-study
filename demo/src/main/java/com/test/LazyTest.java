package com.test;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.entity.Blog;
import com.entity.Comment;
import com.mapper.BlogMapper;
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

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class LazyTest {
    BlogMapper blogMapper;
    SqlSession session;

    static Configuration configuration;
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

        //为了演示序列化反序列化对懒加载的影响，这里直接构建一个configurationFactory类，因为反序列化需要构建一套环境
        configuration.setConfigurationFactory(LazyTest.ConfigurationFactory.class);
        //调用属性的equals,clone,hashCode,toString和get方法都会触发懒加载，而idea调试会自动调用对象的toString方法，不利于我们查看流程
        //于是我们这里先把这些方法全部干掉
        configuration.setLazyLoadTriggerMethods(new HashSet<>());
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
    public void lazyTest(){
        Blog blog=blogMapper.selectBlogById(1);
        for(Comment comment:blog.getComments()){
            System.out.println(DateUtil.formatDate(comment.getDate())+"评论了本博客："+comment.getContent());
        }
        System.out.println(blog.getComments());
    }

    @Test
    public void lazySetTest(){
        Blog blog=blogMapper.selectBlogById(1);
        blog.setComments(new ArrayList<>());
        for(Comment comment:blog.getComments()){
            System.out.println(DateUtil.formatDate(comment.getDate())+"评论了本博客："+comment.getContent());
        }
        System.out.println(blog.getComments());
    }

    /**
     * 测试序列化和反序列化
     */
    @Test
    public void lasySerializableTest() throws IOException, ClassNotFoundException {
        Blog blog=blogMapper.selectBlogById(1);
        byte[] bytes=writeObject(blog);
        Blog newBlog= (Blog) readObject(bytes);
        System.out.println("反序列化完成");
        newBlog.getComments();
    }

    private static byte[] writeObject(Object object) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(object);
        return out.toByteArray();
    }

    private static Object readObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    public static class ConfigurationFactory{
        public static Configuration getConfiguration(){
            return configuration;
        }
    }
}
