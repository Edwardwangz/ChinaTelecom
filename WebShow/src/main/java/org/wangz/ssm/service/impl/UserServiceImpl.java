package org.wangz.ssm.service.impl;

import org.springframework.stereotype.Service;
import org.wangz.ssm.dao.IBaseDao;
import org.wangz.ssm.domain.Item;
import org.wangz.ssm.domain.Order;
import org.wangz.ssm.domain.User;
import org.wangz.ssm.service.IUserService;

import javax.annotation.Resource;

/**
 *
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Resource(name = "itemDao")
    private IBaseDao<Item> itemDao;

    @Resource(name = "userDao")
    public void setDao(IBaseDao<User> dao) {
        super.setDao(dao);
    }

    /**
     * 长事务测试
     */
    public void longTx() {
        //插入item
        Item i = new Item();
        i.setItemName("ttt");

        Order o = new Order();
        o.setId(2);
        //
        itemDao.insert(i);

        this.delete(1);
    }

    public void save(User u) {
        if (u.getId() != null) {
            this.update(u);
        } else {
            this.insert(u);
        }
    }
}