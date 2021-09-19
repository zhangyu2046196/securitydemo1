package com.youyuan.securitydemo1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * 类名称：SecurityConfig <br>
 * 类描述： Security配置类用于通过配置类设置用户名密码 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/18 14:20<br>
 */
@Configuration
public class SecurityConfigTest extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    /**
     * 方法名: persistentTokenRepository <br>
     * 方法描述: 配置操作自动登录操作数据库的类 <br>
     *
     * @return {@link PersistentTokenRepository 返回自动登录操作数据库的实例 }
     * @date 创建时间: 2021/9/19 10:01 <br>
     * @author zhangyu
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // 赋值数据源
        jdbcTokenRepository.setDataSource(dataSource);
        // 自动创建表,第一次执行会创建，以后要执行就要删除掉！
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    /**
     * 方法名: configure <br>
     * 方法描述: 重写认证方法 <br>
     *
     * @date 创建时间: 2021/9/18 14:21 <br>
     * @author zhangyu
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //指定自定义实现类
        auth.userDetailsService(userDetailsService).passwordEncoder(password());
    }

    /**
     * 方法名: configure <br>
     * 方法描述: 自定义登录页面及设置指定地址不需要认证 <br>
     *
     * @param http http请求
     * @date 创建时间: 2021/9/18 18:31 <br>
     * @author zhangyu
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置用户注销地址
        http.logout().logoutUrl("/logout")  //注销地址 可以随便写 但是要和登录成功页面退出按钮地址一致
                .logoutSuccessUrl("/index.html").permitAll(); //注销成功之后跳转地址
        //配置自定义403页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");
        //配置认证、基于权限访问、基于角色访问
        http.formLogin() //自定义自己编写的登录页面
                .loginPage("/login.html")  //登录页面设置
                .loginProcessingUrl("/user/login")  //登录访问地址 (随意指定)
                .defaultSuccessUrl("/success.html").permitAll()  //登录成功后跳转路径
                .and().authorizeRequests()
                .antMatchers("/", "/user/login", "/test/hello").permitAll()  //指定哪些地址可以不经过 认证就能访问
                //1.hasAuthority基于用户指定权限访问,用户只有这一个权限时访问
//                .antMatchers("/test/index").hasAuthority("admins")  //用户只有admins权限才能访问
                //2.hasAnyAuthority用户有任何所指定的权限都可以方法
//                .antMatchers("/test/index").hasAnyAuthority("admins","manage")  //用户有admins或manage权限就能访问
                //3.hasRole用户只有指定角色时才能访问
//                .antMatchers("/test/index").hasRole("sale")   //用户只有sale角色时才能访问
                //4.hasAnyRole只要有任何一个角色就能访问
                .antMatchers("/test/index").hasAnyRole("admin", "sale")  //用户只要有任何一个角色就能访问
                .anyRequest().authenticated()
                //配置自动登录操作数据库的实体
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                //配置自动登录保留时间
                .tokenValiditySeconds(60)
                //配置自定义实现类
                .userDetailsService(userDetailsService)
                .and().csrf().disable();  //关闭csrf防护
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
