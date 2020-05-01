package com.Service;

import com.Entity.Blog;

import java.util.List;

public interface BlogService {

    List<Blog> getBlogs(int page, int pageSize);

    boolean addBlog(Blog blog);

    Blog getBlog(int blogId);

    boolean deleteBlog(int blogId);

    boolean addBlogComment(Blog blog);

    boolean deleteComment(int commentId);

    Blog getBlogComment(int commentId);

    int getBlogsTotal();
}
