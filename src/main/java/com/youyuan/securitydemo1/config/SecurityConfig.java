package com.youyuan.securitydemo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 类名称：SecurityConfig <br>
 * 类描述： Security配置类用于通过配置类设置用户名密码 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/18 14:20<br>
 */
//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 方法名: configure <br>
     * 方法描述: 重写认证方法 <br>
     *
     * @date 创建时间: 2021/9/18 14:21 <br>
     * @author zhangyu
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //密码加密器,用于对密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123");
        auth.inMemoryAuthentication().withUser("lucy").password(password).roles("admin");
    }

    /**
     * 方法名: password <br>
     * 方法描述: 配置加密器类 <br>
     *
     * @return {@link PasswordEncoder 返回加密器 }
     * @date 创建时间: 2021/9/18 14:24 <br>
     * @author zhangyu
     */
    @Bean
    public PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }
}
