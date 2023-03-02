package cn.iyunmc.reggie.controller;

import cn.iyunmc.reggie.common.BaseContext;
import cn.iyunmc.reggie.common.R;
import cn.iyunmc.reggie.entity.AddressBook;
import cn.iyunmc.reggie.entity.User;
import cn.iyunmc.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 */
@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrenId());
        log.info("addressBook：{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 获取用户地址列表
     *
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrenId());
        log.info("addressBook：{}", addressBook);

        //SQL: select * from address_book where user_id = ? order by update_time desc
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        lqw.orderByDesc(AddressBook::getUpdateTime);

        return R.success(addressBookService.list(lqw));
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook: {}", addressBook);
        LambdaUpdateWrapper<AddressBook> luw = new LambdaUpdateWrapper<>();
        luw.eq(AddressBook::getUserId, BaseContext.getCurrenId());
        luw.set(AddressBook::getIsDefault, 0);
        //SQL: update address_book set is_default = 0 where user_id = ?
        addressBookService.update(luw);
        //SQL: update address_book set is_default = 1 where id = ?
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, BaseContext.getCurrenId());
        lqw.eq(AddressBook::getIsDefault, 1);

        //SQL: select * from address_book where user_id = ? and is_default =1
        AddressBook addressBook = addressBookService.getOne(lqw);
        if (null == addressBook) {
            return R.error("没有找到该对象");
        }else
            return R.success(addressBook);
    }
}
