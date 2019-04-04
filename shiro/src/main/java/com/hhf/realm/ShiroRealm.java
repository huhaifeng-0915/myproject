/**
 * MIT License
 * Copyright (c) 2018 yadong.zhang
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.hhf.realm;

import com.alibaba.fastjson.JSONObject;
import com.hhf.common.constant.RbacUserConstant;
import com.hhf.common.constant.UserTypeEnum;
import com.hhf.common.user.model.RbacResourcesEO;
import com.hhf.common.user.model.RbacRoleEO;
import com.hhf.common.user.model.RbacUser;
import com.hhf.common.user.service.RbacResourcesService;
import com.hhf.common.user.service.RbacRoleService;
import com.hhf.common.user.service.RbacUserService;
import com.hhf.common.utils.JacksonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.management.relation.Role;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Shiro-密码输入错误的状态下重试次数的匹配管理
 *
 * @author huhaifeng
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private RbacUserService rbacUserService;
    @Resource
    private RbacResourcesService rbacResourcesService;
    @Resource
    private RbacRoleService rbacRoleService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 提供账户信息返回认证信息（用户的角色信息集合）
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        RbacUser user = rbacUserService.getByUserName(username);
        if (user == null) {
            throw new UnknownAccountException("账号不存在！");
        }
        if (user.getUserStatus() != null && RbacUserConstant.UserStatusEnum.INVALID.getLable().equals(user.getUserStatus())) {
            throw new LockedAccountException("帐号已被锁定，禁止登录！");
        }

        // principal参数使用用户Id，方便动态刷新用户权限
        return new SimpleAuthenticationInfo(
                user.getId(),
                user.getPassword(),
                ByteSource.Util.bytes(username),
                getName()
        );
    }

    /**
     * 权限认证，为当前登录的Subject授予角色和权限（角色的权限信息集合）
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        String userId = (String) SecurityUtils.getSubject().getPrincipal();

        // 赋予角色
        List<RbacRoleEO> roleList = rbacRoleService.listRolesByUserId(userId);
        for (RbacRoleEO role : roleList) {
            info.addRole(role.getRoleCode());
        }

        // 赋予权限
        List<RbacResourcesEO> resourcesList = null;
        RbacUser user = rbacUserService.getById(userId);
        if (null == user) {
            return info;
        }
        // SUPER_ADMIN用户默认拥有所有权限
        if (roleList.stream().map(role -> role.getRoleCode()).collect(Collectors.toList()).contains(UserTypeEnum.SUPER_ADMIN.getCode())) {
            resourcesList = rbacResourcesService.list();
        } else {
            resourcesList = rbacResourcesService.listByUserId(userId);
        }
        if (!CollectionUtils.isEmpty(resourcesList)) {
            Set<String> permissionSet = new HashSet<>();
            for (RbacResourcesEO resources : resourcesList) {
                String permission = null;
                if (!StringUtils.isEmpty(permission = resources.getPermission())) {
                    permissionSet.addAll(Arrays.asList(permission.trim().split(",")));
                }
            }
            info.setStringPermissions(permissionSet);
        }
        //将用户的角色、权限和用户信息放到缓存中  todo
        redisTemplate.opsForValue().set(user.getUserLoginCode(), JacksonUtils.obj2json(info));
        return info;
    }

}
