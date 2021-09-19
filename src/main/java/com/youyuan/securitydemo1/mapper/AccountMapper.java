package com.youyuan.securitydemo1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youyuan.securitydemo1.entity.Account;
import org.springframework.stereotype.Repository;

/**
 * 类名称：AccountMapper <br>
 * 类描述： 用户mapper接口 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/18 17:53<br>
 */
@Repository
public interface AccountMapper extends BaseMapper<Account> {
}
