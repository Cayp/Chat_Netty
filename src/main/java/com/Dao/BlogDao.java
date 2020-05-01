package com.Dao;


import com.Entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作动态圈表
 * @author ljp
 */
@Mapper
public interface BlogDao {

    List<Blog> getBlogs(@Param("startNum")int startNum, @Param("pageSize")int pageSize);

    List<Blog> getBlogComments(@Param("blogIds")List<Integer> blogIds);

    int createBlog(@Param("blog")Blog blog);

    int deleteBlog(@Param("id")int id);

    int deleteBlogComment(@Param("blogId")int blogId);

    int addComment(@Param("blogComment")Blog blog);

    int deleteComment(@Param("commentId")int commentId);

    Blog getBlogComment(@Param("commentId")int commentId);

    Blog getBlog(@Param("blogId")int blogId);

    int getBlogsTotal();

}
