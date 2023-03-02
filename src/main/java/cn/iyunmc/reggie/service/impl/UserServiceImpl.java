package cn.iyunmc.reggie.service.impl;

import cn.iyunmc.reggie.entity.User;
import cn.iyunmc.reggie.mapper.UserMapper;
import cn.iyunmc.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
