package com.hhf.common.user.service;

import com.hhf.common.user.model.RbacUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-19
 */
public interface RbacUserService extends IService<RbacUser> {

    /**
     * 通过用户名 查询用户
     * @param userName
     * @return
     */
    RbacUser getByUserName(String userName);

    /**
     *  设置上次登录信息
     * @param user
     */
    void updateUserLastLoginInfo(RbacUser user);

    List<RbacUser> listByRoleId(Long roleId);
}
