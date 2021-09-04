package com.zhangguo.bookstore;

import com.zhangguo.bookstore.entities.Book;
import com.zhangguo.bookstore.mapper.BookDAO;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {

        /*DataSource dataSource = getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession session=sqlSessionFactory.openSession();
        session.clearCache();
        BlogMapper blogMapper=session.getMapper(BlogMapper.class);
        Blog blog=blogMapper.selectBlog(1);*/

    }

    @org.junit.Test
    public void testBySpring(){
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
        BookDAO bookDAO=context.getBean(BookDAO.class);
        Book book1=bookDAO.getBookById(1);
        Book book2=bookDAO.getBookById(1);
        System.out.print(book1==book2);
    }
    /*public static DataSource getDataSource(){
        DruidDataSource druidDataSource=new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
        return druidDataSource;
    }*/
}
