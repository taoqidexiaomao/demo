package com.example.demo.controller;

import com.example.demo.annotation.Encrypt;
import com.example.demo.annotation.Secret;
import com.example.demo.entity.SensitiveInformation;
import com.example.demo.entity.User;
import com.example.demo.entity.file.Chunk;
import com.example.demo.service.BigFileService;
import com.example.demo.service.IUserService;
import com.example.demo.util.JsonResult;
import com.example.demo.util.ResultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DemoController {

    @Resource
    private IUserService userService;
    @Autowired
    private BigFileService bigFileService;
    /**
     * 脱敏
     * @return
     */
    @GetMapping("sensitive")
    public JsonResult<List<SensitiveInformation>> sensitive() {
        List<SensitiveInformation> list = new ArrayList<>();
        SensitiveInformation sensitiveInformation = new SensitiveInformation();
        sensitiveInformation
                .setUserid("n10001")
                .setChineseName("   ")
                .setAddress("北京市九宫")
                .setIdNumber("13023928819028321X")
                .setEmail("baidu@163.com")
                .setBankCard("131231321312314567")
                .setLandlineNumber("0310-68123987")
                .setPhoneNumber("13042928312")
                .setPassword("meTooYu")
                .setLicensePlate("京A888888");
        SensitiveInformation sensitiveInformationTwo = new SensitiveInformation();
        sensitiveInformationTwo
                .setUserid("n10002")
                .setChineseName("张志伟")
                .setAddress("北京市超标")
                .setIdNumber("23023928819028321X")
                .setEmail("360@163.com");
        list.add(sensitiveInformation);
        list.add(sensitiveInformationTwo);
        list.add(new SensitiveInformation().setUserid("n10003")
                .setChineseName("李家村"));
        return ResultTool.success(list);
    }
    /**
     * 加密
     * @param user
     * @return
     */
    @PostMapping("encrypt")
    //@Encrypt
    public JsonResult encrypt(@RequestBody User user) {
        List<User> list=userService.queryList(user);
        return  ResultTool.success(list);
    }

    /**
     * 断点续传
     * @return
     */
    @PostMapping("/file/upload")
    public  JsonResult fileUpload(@ModelAttribute Chunk chunk) throws Exception {
        String result= bigFileService.fileUploadPost(chunk);
        return ResultTool.success(result);
    }
    @GetMapping("save")
    @Encrypt
    public JsonResult<?> save(User user) {
        userService.save(user);
        return ResultTool.success(user);
    }
}

