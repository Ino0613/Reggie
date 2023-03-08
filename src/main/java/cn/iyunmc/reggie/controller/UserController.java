package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.Utils.SMSUtils;
import cn.iyunmc.reggie.Utils.ValidateCodeUtils;
import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.entity.User;
import cn.iyunmc.reggie.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 短信功能
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云提供的短信服务API完成发送短信
            log.info("验证码为：{}",code);
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code);
            //需要将生成的验证码保存到Session
//            session.setAttribute(phone,code);

            //将生成的验证码缓存到Redis中，并且设置时间为5Min
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("验证码发送成功！");
        }
        return R.error("验证码发送失败，请重试！");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
//        Object sessionCode = session.getAttribute(phone);

        Object sessionCode = redisTemplate.opsForValue().get(phone);
        //进行验证码的对比（页面提交的验证码和Session中保存的验证码对比）
        if (sessionCode != null && sessionCode.equals(code)) {
            //如果能够对比成功，则说明登陆成功
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userService.getOne(lqw);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());

            //如果用户登陆成功，删除Redis中缓存的验证码
            redisTemplate.delete(phone);
            return R.success("user");
        }

        //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
        return R.error("短信发送失败！");
    }
}
