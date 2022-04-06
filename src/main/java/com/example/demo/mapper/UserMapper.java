package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.annotation.DataScope;
import com.example.demo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mrLi
 * @since 2022-03-11
 */
/*@DataScope(type = DataScopeConfig.TEST_CLASS, value = {
        // 这里 DataScope 作用于整个类，优先级小于方法 DataScope 注解
        @DataColumn(name = "department_id")
})*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @DataScope(type = "USER", column = "create_dept")
    IPage<User> queryList(IPage<User> page, @Param("user") User user);
/*    @DataScope(type = DataScopeConfig.TEST, value = {
            @DataColumn(alias = "u", name = "age")
    })*/
    @DataScope(type = "USER", column = "create_dept")
    @Select("select * from user")
    List<User> ListTwo(String id, String name);
}
