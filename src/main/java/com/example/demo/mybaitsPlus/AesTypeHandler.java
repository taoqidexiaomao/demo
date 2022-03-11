package com.example.demo.mybaitsPlus;


import com.example.demo.util.AESUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Slf4j
@Component
public class AesTypeHandler extends BaseTypeHandler<String> {

    @Value("${aes.key}")
    private String aesKey;
    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) {
        ps.setString(i, Arrays.toString(AESUtil.encrypt(parameter, aesKey.getBytes())));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException{
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
        return cs.getString(columnIndex);
    }
}


