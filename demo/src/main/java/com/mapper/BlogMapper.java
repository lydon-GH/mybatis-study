package com.mapper;

import com.entity.Blog;
import com.query.BlogQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface BlogMapper {
    //@Options(flushCache = Options.FlushCachePolicy.TRUE)
//    Blog selectBlog(int id);
    Blog selectBlogById(@Param("id") int id);
    Blog selectBlogByIdWithComplex(@Param("id") int id);
    Blog selectBlogByIdWithComplexLoop(@Param("id") int id);
    Blog selectBlogByMap(Map map);
    Blog selectBlogByObject(BlogQuery query);
    Blog selectBlogByParamAndObject(@Param("id") int id ,BlogQuery query);
}
