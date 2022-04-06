package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mrLi
 * @since 2022-03-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> queryList(User user) {
        IPage<User> page = new Page<>(0, 10);
        IPage<User> pages=userMapper.queryList(page,user);
        String id="1";
        String name="张三";
        List<User> list= userMapper.ListTwo(id,name);
        return pages.getRecords();
    }
}
