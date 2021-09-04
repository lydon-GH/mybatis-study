package com.test;

import com.data.Mock;
import com.entity.Blog;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;


public class MetaObjectTest {

    @Test
    public void test(){
        Blog blog= Mock.newBlog();
        Configuration configuration=new Configuration();
        MetaObject metaObject=configuration.newMetaObject(blog);
        metaObject.getValue("comments[0].user.name");
    }
}
