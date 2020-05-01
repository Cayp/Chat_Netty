package com.Controller;

import com.Entity.Blog;
import com.Entity.BlogsEntity;
import com.Service.BlogService;
import com.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {


    @Autowired
    Response response;

    @Resource(name = "blogServiceImpl")
    private BlogService blogService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Response addBlog(@RequestBody Blog blog, HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        blog.setUserId((int) userId);
        blog.setCreateTime(System.currentTimeMillis());
        boolean res = blogService.addBlog(blog);
        if (res) {
            return response.success("发布成功");
        } else {
            return response.error("发布失败");
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public Response deleteBlog(int blogId, HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        Blog blog = blogService.getBlog(blogId);
        if (blog != null && blog.getUserId() != userId) {
            return response.error("你不是该动态的发表者");
        } else {
            blogService.deleteBlog(blogId);
            return response.success("删除成功");
        }
    }

    @RequestMapping(value = "/blogs", method = RequestMethod.GET)
    public Response getBlogs(int pageNum, int pageSize) {
        List<Blog> blogs = blogService.getBlogs(pageNum, pageSize);
        int blogsTotal = blogService.getBlogsTotal();
        return response.successWithData("获取动态成功", new BlogsEntity(blogs, blogsTotal));
    }

    @RequestMapping(value = "/comment/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Response addComment(@RequestBody Blog blog, HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        blog.setUserId((int) userId);
        blog.setCreateTime(System.currentTimeMillis());
        boolean res = blogService.addBlogComment(blog);
        if (res) {
            return response.success("添加回复成功");
        } else {
            return response.error("添加回复失败");
        }
    }

    @RequestMapping(value = "/comment/delete", method = RequestMethod.DELETE)
    public Response deleteComment(int commentId, HttpSession httpSession) {
        long userId = (long) httpSession.getAttribute("userId");
        Blog blogComment = blogService.getBlogComment(commentId);
        if (blogComment != null && blogComment.getUserId() != userId) {
            return response.error("你不是回复的发表者");
        } else {
            blogService.deleteComment(commentId);
            return response.success("删除成功");
        }
    }
}
