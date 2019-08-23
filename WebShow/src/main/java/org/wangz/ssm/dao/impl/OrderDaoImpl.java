package org.wangz.ssm.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.wangz.ssm.dao.IBaseDao;
import org.wangz.ssm.domain.Order;

import java.util.List;

/**
 * OrderDao
 */
@Repository
public class OrderDaoImpl extends SqlSessionDaoSupport implements IBaseDao<Order> {

    public void insert(Order order) {

    }

    public void update(Order order) {

    }

    public void delete(Integer id) {

    }

    public Order selectOne(Integer id) {
        return null;
    }

    public List<Order> selectAll() {
        return null;
    }

    public List<Order> selectPage(int offset, int len) {
        return null;
    }

    public int selectCount() {
        return 0;
    }
}