package org.wangz.ssm.service.impl;

import org.springframework.stereotype.Service;
import org.wangz.ssm.dao.IBaseDao;
import org.wangz.ssm.domain.Item;
import org.wangz.ssm.service.IItemService;

import javax.annotation.Resource;

/**
 *
 */
@Service("itemService")
public class ItemServiceImpl extends BaseServiceImpl<Item> implements IItemService {

    @Resource(name="itemDao")
    public void setDao(IBaseDao<Item> dao) {
        super.setDao(dao);
    }
}