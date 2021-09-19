package com.youyuan.securitydemo1.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youyuan.securitydemo1.entity.Account;
import com.youyuan.securitydemo1.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类名称：MyUserDetailService <br>
 * 类描述： 通过自定义实现类方式完成用户认证 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/18 14:53<br>
 */
@Service("userDetailsService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 方法名: loadUserByUsername <br>
     * 方法描述: 重写设置用户名和密码方式 <br>
     *
     * @param userName 用户名
     * @return {@link UserDetails 返回用户认证实体 }
     * @date 创建时间: 2021/9/18 14:54 <br>
     * @author zhangyu
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //构造查询wrapper
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name", userName);
        //根据用户名从数据库查询
        Account account = accountMapper.selectOne(wrapper);
        if (ObjectUtil.isEmpty(account)) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        //模拟通过角色获取权限列表
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admins," +
                "ROLE_admin,ROLE_sale");
        //设置用户名、密码、权限
        return new User(account.getUserName(), new BCryptPasswordEncoder().encode(account.getPassword()), authorities);
    }
}
