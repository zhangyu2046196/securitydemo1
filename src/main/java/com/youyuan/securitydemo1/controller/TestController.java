package com.youyuan.securitydemo1.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youyuan.securitydemo1.entity.Account;
import com.youyuan.securitydemo1.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：TestController <br>
 * 类描述： Spring Security入门案例信息 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/17 22:04<br>
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 方法名: hello <br>
     * 方法描述: 入门案例接口 <br>
     *
     * @return {@link String 返回指定字符串 }
     * @date 创建时间: 2021/9/17 22:05 <br>
     * @author zhangyu
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    /**
     * 方法名: index <br>
     * 方法描述: 登录成功跳转地址 <br>
     *
     * @return {@link String 返回登录成功文案 }
     * @date 创建时间: 2021/9/18 18:45 <br>
     * @author zhangyu
     */
    @GetMapping("/index")
    public String index() {
        return "hello index";
    }

    /**
     * 方法名: update <br>
     * 方法描述: 测试基于注解认证和授权 <br>
     *
     * @return {@link String 返回结果信息 }
     * @date 创建时间: 2021/9/18 20:47 <br>
     * @author zhangyu
     */
    @GetMapping("/update")
//    @Secured({"ROLE_admin", "ROLE_sale"}) //代表只用登录用户有admin或sale权限才能访问此方法
//    @PreAuthorize("hasAnyAuthority('admins','manage')")  //代表用户只要有admins或manages权限就可以访问且先校验权限在执行业务
    @PostAuthorize("hasAnyAuthority('admins','manage')")   //代表先执行业务然后在判断用户是否有admins或manages权限
    public String update() {
        System.out.println("hello update ......");
        return "hello update";
    }

    /**
     * 方法名: getAllUser <br>
     * 方法描述: 测试基于注解权限或角色控制 <br>
     *
     * @return {@link List<Account> 返回执行结果 }
     * @date 创建时间: 2021/9/18 21:11 <br>
     * @author zhangyu
     */
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_sale')")
    @PostFilter("filterObject.userName == 'jack'")  //权限验证之后对数据进行过滤 留下用户名是 jack 的数据  表达式中的 filterObject 引用的是方法返回值 List 中的某一个元素
    public List<Account> getAllUser() {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        List<Account> accounts = accountMapper.selectList(queryWrapper);
        return accounts;
    }
}
