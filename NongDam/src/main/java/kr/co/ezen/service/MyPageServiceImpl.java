package kr.co.ezen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ezen.entity.Free;
import kr.co.ezen.entity.Tr;
import kr.co.ezen.entity.User;
import kr.co.ezen.mapper.MyPageMapper;

@Service
public class MyPageServiceImpl implements MyPageService{

    @Autowired
    MyPageMapper myPageMapper;

    @Override
    public List<Tr> findByIdx(int user_idx) {
        List<Tr> li=myPageMapper.findByIdx(user_idx);
        return li;
    }

    @Override
    public List<Free> findByIdx2(int user_idx) {
        List<Free> li2=myPageMapper.findByIdx2(user_idx);
        return li2;
    }

    @Override
    public User findAll(int user_idx) {
        User uli = myPageMapper.findAll(user_idx);
        return uli;
    }

    @Override
    public void updateUserInfo(User user) {
        myPageMapper.updateUserInfo(user);

    }

    @Override
    public void deleteUserById(int user_idx) {
        myPageMapper.deleteUserById(user_idx);

    }



}