package com.example.demo.config.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author 刘声凤 w_liushengfeng@kingsoft.com
 * @date 2022/3/10 21:58
 */
public class EncryptHttpInputMessage implements HttpInputMessage {
    private final HttpHeaders headers;
    private final InputStream body;
    public EncryptHttpInputMessage(HttpInputMessage httpInputMessage, String bodyStr) {
        this.headers = httpInputMessage.getHeaders();
        this.body=encode(bodyStr);
    }

    private InputStream encode(String bodyStr) {
        return new ByteArrayInputStream(bodyStr.getBytes());
    }
    @Override
    public InputStream getBody() {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
