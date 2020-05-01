package com.Service.ServiceImp;

import com.Dao.BlogDao;
import com.Entity.Blog;
import com.Service.BlogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogDao blogDao;

    @Override
    public List<Blog> getBlogs(int page, int pageSize) {
        int startNum = page * pageSize;
        List<Blog> blogs = blogDao.getBlogs(startNum, pageSize);
        List<Integer> blogIds = blogs.stream().map(Blog::getId).collect(Collectors.toList());
        List<Blog> blogComments = blogDao.getBlogComments(blogIds);
        Map<Integer, List<Blog>> commentsMap = blogComments.stream().collect(Collectors.groupingBy(Blog::getBlogId));
        blogs.forEach(blog -> {
            int blogId = blog.getId();
            List<Blog> commentsList = commentsMap.get(blogId);
            if (commentsList != null) {
                commentsList.sort((a, b) -> (int) (a.getCreateTime() - b.getCreateTime()));
                blog.setComments(commentsList);
            }
        });
        return blogs;
    }

    @Override
    public boolean addBlog(Blog blog) {
        int res = blogDao.createBlog(blog);
        return res > 0;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteBlog(int blogId) {
        blogDao.deleteBlog(blogId);
        blogDao.deleteBlogComment(blogId);
        return true;
    }

    @Override
    public boolean addBlogComment(Blog blog) {
        return blogDao.addComment(blog) > 0;
    }

    @Override
    public boolean deleteComment(int commentId) {
        blogDao.deleteComment(commentId);
        return true;
    }

    @Override
    public Blog getBlogComment(int commentId) {
        return blogDao.getBlogComment(commentId);
    }

    @Override
    public Blog getBlog(int blogId) {
        return blogDao.getBlog(blogId);
    }

    @Override
    public int getBlogsTotal() {
        return blogDao.getBlogsTotal();
    }
}
