package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mrLi
 * @since 2022-03-11
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> queryList(User user);
}
