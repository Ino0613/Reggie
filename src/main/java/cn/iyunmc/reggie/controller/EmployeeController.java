package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.common.BaseContext;
import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.entity.Employee;
import cn.iyunmc.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        /**
         * 2. 根据页面提交的用户名username查询数据库
         * 3. 如果没有查询到则返回登陆失败
         * 4. 密码对比，如果不一致则返回登陆失败结果
         * 5. 查看员工状态，如果为禁用状态，则返回员工已禁用结果
         * 6. 恩鲁成公，将员工id存入Session并返回登陆成功结果
         */

        //1. 将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        if (emp == null) {
            return R.error("登陆失败！");
        }
        if (emp.getStatus() == 0) {
            return R.error("登陆失败，账号已被封禁！");
        }
        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败！");
        }
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出功能
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    @PostMapping
    public R<String> add(HttpServletRequest request, @RequestBody Employee employee) {

        //设置初始密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);
        log.info("新增员工信息 : {}", employee);
        return R.success("新增员工成功！");
    }

    /**
     * 员工信息查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> employeeList(int page, int pageSize, String name) {
        log.info("page : {} ,pageSize : {},name : {}", page, pageSize, name);

        //构造分页构造器
        Page<Employee> pageinfo = new Page<>(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        //添加过滤条件
        lqw.like(StringUtils.isNotEmpty(name), Employee::getUsername, name);
        //添加排序条件
        lqw.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageinfo, lqw);
        return R.success(pageinfo);
    }

    @PutMapping
    public R<String> updateStatus(HttpServletRequest request, @RequestBody Employee employee) {
//        log.info("id:{},status:{}", employee.getId(), employee.getStatus());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
//        Employee emp = employeeService.getById(employee.getId());
//        System.out.println(emp);

        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> updateEmployee(HttpServletRequest request, @PathVariable Long id) {
//        Long empId = (Long) request.getSession().getAttribute("employee");
        Employee emp = employeeService.getById(id);
//        emp.setUpdateTime(LocalDateTime.now());
//        emp.setUpdateUser(empId);
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setThreadLocal(empId);
        return R.success(emp);
    }
}
