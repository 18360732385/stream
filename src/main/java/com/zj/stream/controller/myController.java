/**
 * Copyright (C), 2019
 * FileName: myController
 * Author:   zhangjian
 * Date:     2019/8/30 19:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.zj.stream.controller;

import com.zj.stream.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/myController")
public class myController {

     @PostMapping(value = "/text")
     public Result testDecrypt(String name, @RequestParam(defaultValue = "18") int age,String id,String ok)  {
        System.out.println("controller业务逻辑");
//        int i =9/0;
        String data = "我的名字是"+name+",年龄"+age+",学号"+id+",ok="+ok;
        return Result.success(data);
    }
}
