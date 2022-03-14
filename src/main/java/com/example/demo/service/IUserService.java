package com.example.demo.service;

import com.example.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mrLi
 * @since 2022-03-11
 */
public interface IUserService extends IService<User> {

    List<User> queryList(User user);
}
