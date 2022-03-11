package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.example.demo.mybaitsPlus.AesTypeHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author mrLi
 * @since 2022-03-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableField(value = "name",typeHandler = AesTypeHandler.class)
    private String name;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "sex")
    private Integer sex;


}
