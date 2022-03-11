package com.example.demo;

import cn.hutool.core.util.DesensitizedUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        System.out.println(DesensitizedUtil.idCardNum("51343620000320711X", 1, 2));
    }
}
