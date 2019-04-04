package com.hhf.common.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hhf.common.holder.RequestHolder;
import com.hhf.common.user.model.RbacUser;
import com.hhf.common.user.mapper.RbacUserMapper;
import com.hhf.common.user.service.RbacUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhf.common.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-19
 */
@Service
public class RbacUserServiceImpl extends ServiceImpl<RbacUserMapper, RbacUser> implements RbacUserService {

    @Autowired
    RbacUserMapper rbacUserMapper;

    @Override
    public RbacUser getByUserName(String userName) {
        QueryWrapper<RbacUser> queryWrapper = new QueryWrapper<RbacUser>();
        queryWrapper.eq("USER_LOGIN_CODE", userName);
        return rbacUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateUserLastLoginInfo(RbacUser user) {
        user.setLoginCount(user.getLoginCount()+1);
        user.setLastLoginIp(IpUtil.getRealIp(RequestHolder.getRequest()));
        user.setLastLoginTime(new Date());
        rbacUserMapper.updateById(user);
    }

    @Override
    public List<RbacUser> listByRoleId(Long roleId) {
        return rbacUserMapper.listByRoleId(roleId);
    }
}
