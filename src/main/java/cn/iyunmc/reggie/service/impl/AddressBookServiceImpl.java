package cn.iyunmc.reggie.service.impl;

import cn.iyunmc.reggie.entity.AddressBook;
import cn.iyunmc.reggie.mapper.AddressBookMapper;
import cn.iyunmc.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
