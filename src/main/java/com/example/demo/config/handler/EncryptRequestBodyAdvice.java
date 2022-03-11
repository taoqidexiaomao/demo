package com.example.demo.config.handler;

import com.example.demo.annotation.Encrypt;
import com.example.demo.annotation.Secret;
import com.example.demo.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author 刘声凤 w_liushengfeng@kingsoft.com
 * @date 2022/3/10 21:38
 */
@ControllerAdvice
@Slf4j
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {
    @Value("${aes.key}")
    private String aesKey;
    @Value("${aes.iv}")
    private String aesIv;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return Objects.requireNonNull(methodParameter.getMethod()).getAnnotation(Encrypt.class) != null || methodParameter.getParameter().getAnnotation(Encrypt.class) != null;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        InputStream is = httpInputMessage.getBody();
        BufferedReader rs = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = rs.readLine()) != null) {
            sb.append(str);
        }
        try {
            //解密
           String newStr= new String(AESUtil.decrypt(sb.toString(),aesKey.getBytes()));
           return new EncryptHttpInputMessage(httpInputMessage,newStr);
        }catch (Exception e){
            log.error("body解密异常",e);
        }
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }
}
