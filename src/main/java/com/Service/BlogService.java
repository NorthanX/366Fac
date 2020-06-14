package com.Service;

import com.Dao.BlogDao;
import com.Dao.CategoryDao;
import com.Model.Blog;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private CategoryDao categoryDao;

    public int addBlog(Blog blog) {
        return blogDao.insertBlog(blog);
    }

    public Blog getBlogById(int id) {
        return blogDao.getById(id);
    }

    public Page<Blog> getByTag(int TagId){
        return (Page<Blog>) blogDao.getByTag(TagId);
    }

    public List<Blog> getByTitle(String title){
        return blogDao.getByTitle(title);
    }

    public List<Blog> getByAuthor(String author){
        return blogDao.getByAuthor(author);
    }
    public List<Blog> getLatest(){
        return blogDao.getLatestBlogs();
    }
    public List<Blog> getByType(String type){
        return blogDao.getByType(type);
    }
    public int getBlogCount() {
        return blogDao.getCount();
    }

    public int getCommentCount(int BlogID){
        return blogDao.getCommentCount(BlogID);
    }
    /**
     * liked return true
     */
    public boolean UserLiked(String id,int blogId) {
        return blogDao.liked(id,blogId) != 0;
    }

    public void deleteById(int id){
        blogDao.deleteById(id);
    }

    /**
     * user like blog
     */
    public int like(String userId, int BlogID) {
        return blogDao.likeBlog(userId, BlogID);
    }

    public void updateBlog(String content,int blogId, String title){
        blogDao.updateBlog(content, blogId, title);
    }
    public List<Blog> search(String keywords){
        List<Blog> blogs=new ArrayList<>();
        blogs.addAll(blogDao.getByTitle(keywords));
        blogs.addAll(blogDao.getByTagName(keywords));
        return blogs;
    }
}
