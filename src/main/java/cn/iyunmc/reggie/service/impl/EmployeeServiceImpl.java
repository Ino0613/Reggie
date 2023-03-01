package cn.iyunmc.reggie.service.impl;

import cn.iyunmc.reggie.entity.Employee;
import cn.iyunmc.reggie.mapper.EmployeeMapper;
import cn.iyunmc.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
