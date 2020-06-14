package com.Service;

import com.Dao.TagDao;
import com.Dao.CategoryDao;
import com.Model.Category;
import com.Model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TagService {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private CategoryDao categoryDao;

    public Tag getByName(String name){
        return tagDao.getByName(name);
    }

    public List<Tag> getAll(){
        return tagDao.selectAll();
    }

    public int addTag(Tag tag){
        return tagDao.insertTag(tag)>0?tag.getTagID():0;//插入成功则返回tagid，否则返回0
    }

    public int addCategory(Category category){
        return categoryDao.insertCategory(category);
    }

    public List<Tag> getTagByBlogID(int id){
        return categoryDao.getTagsByBlogID(id);
    }

    public int incNum(int TagID){
        return tagDao.incNum(TagID);
    }
}
