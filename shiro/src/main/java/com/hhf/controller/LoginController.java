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
package com.hhf.controller;

import com.hhf.common.response.constant.ResponseCodeConstant;
import com.hhf.common.response.pojo.CommonResponse;
import com.hhf.common.user.model.RbacUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 登录相关
 *
 * @author huhaifeng
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Api(value = "/api/user", description = "登录管理模块")
@Slf4j
@Controller
@RequestMapping(value = "/api/user")
public class LoginController {

    @Autowired
    RedisTemplate redisTemplate;

    @ApiOperation("进入用户登录页面")
    @GetMapping("/inlogin")
    public ModelAndView login(Model model) {
        return new ModelAndView("/login4.jsp");
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/signin")
    @ResponseBody
    public CommonResponse<String> submitLogin(String username, String password, boolean rememberMe, String kaptcha) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到xxRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            currentUser.login(token);
            //获取用户的信息(用户信息、权限信息、角色信息)
            return CommonResponse.buildRespose4Success(null, "登录成功！");
        } catch (Exception e) {
            log.error("登录失败，用户名[{}]", username, e);
            token.clear();
            return CommonResponse.buildRespose4Fail(ResponseCodeConstant.ERROR, e.getMessage());
        }
    }

    /**
     * 使用权限管理工具进行用户的退出，跳出登录，给出提示信息
     *
     * @param redirectAttributes
     * @return
     */
    @ApiOperation("用户退回")
    @GetMapping("/logout")
    public ModelAndView logout(RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        RbacUser user = (RbacUser) subject.getPrincipal();
        // http://www.oschina.net/question/99751_91561
        // 此处有坑： 退出登录，其实不用实现任何东西，只需要保留这个接口即可，也不可能通过下方的代码进行退出
        // SecurityUtils.getSubject().logout();
        // 因为退出操作是由Shiro控制的
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        mv.setViewName("redirect:index");
        return mv;
    }
}
