package com.example.demo.config.handler;


import com.example.demo.util.AESUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * @author 李佳其
 * @date 2022-03-11
 */
@Slf4j
@Component
public class AesTypeHandler extends BaseTypeHandler<String> {

    @Value("${aes.key}")
    private String aesKey;

    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) {
        if (StringUtils.isBlank(parameter)) {
            return;
        }
        ps.setString(i, AESUtil.byteToHexString(AESUtil.encrypt(parameter, aesKey.getBytes(StandardCharsets.UTF_8))));
    }

    @SneakyThrows
    @Override
    public String getNullableResult(ResultSet rs, String columnName) {
        if(StringUtils.isBlank(rs.getString(columnName))){
            return rs.getString(columnName);
        }
        return new String(AESUtil.decrypt(rs.getString(columnName), aesKey.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) {
        if(StringUtils.isBlank(rs.getString(columnIndex))){
            return rs.getString(columnIndex);
        }
        return new String(AESUtil.decrypt(rs.getString(columnIndex), aesKey.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) {
        if(StringUtils.isBlank(cs.getString(columnIndex))){
            return cs.getString(columnIndex);
        }
        return new String(AESUtil.decrypt(cs.getString(columnIndex), aesKey.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}


