package com.Service;

import com.Dao.FollowDao;
import com.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    @Autowired
    FollowDao followDao;

    public void followBlogger(String FollowerId, String BloggerId){

        followDao.followBlogger(FollowerId,BloggerId);
    }

    public void unFollowBlogger(String FollowerId, String BloggerId){

        followDao.unfollow(FollowerId,BloggerId);
    }

    public List<User> MyFollowBlogger(String FollowerId){
        return followDao.MyFollowBlogger(FollowerId);
    }

    public boolean followed(String FollowerId, String BloggerId){
        return followDao.followed(FollowerId, BloggerId)==1;
    }
    public List<User> MyFollowers(String BloggerId){
        return followDao.MyFollowers(BloggerId);
    }
    //我的粉丝数
    public int getMyFollowerCount(String userId){
        return followDao.getFollowerCount(userId);
    }
    public int getMyBloggerCount(String userId){
        return followDao.getBloggerCount(userId);
    }

}
