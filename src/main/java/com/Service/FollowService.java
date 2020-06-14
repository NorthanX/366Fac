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
}
