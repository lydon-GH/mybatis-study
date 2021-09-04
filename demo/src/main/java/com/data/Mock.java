package com.data;

import com.entity.Blog;
import com.entity.Comment;
import com.entity.User;

import java.util.ArrayList;
import java.util.Date;

public class Mock {
    public static Blog newBlog(){
        Blog blog=new Blog();
        blog.setTitle("张三的博客");
        ArrayList<Comment> comments = new ArrayList<>();
        Comment comment=new Comment();
        comment.setContent("到此一游");
        comment.setDate(new Date());
        comment.setUser(new User(1,"小辛庄",19));
        comments.add(comment);

        Comment comment2=new Comment();
        comment2.setContent("楼主流弊");
        comment2.setDate(new Date());
        comment2.setUser(new User(2,"小混蛋",23));
        comments.add(comment2);


        User user=new User(4,"测试一",29);
        blog.setAuthor(user);
        blog.setComments(comments);
        return blog;
    }
}
