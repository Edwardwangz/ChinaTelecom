package org.wangz.ssm.service.impl;

import org.wangz.ssm.dao.IBaseDao;
import org.wangz.ssm.service.IBaseService;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */
public abstract class BaseServiceImpl<T> implements IBaseService<T> {

    private IBaseDao<T> dao ;

    public void setDao(IBaseDao<T> dao) {
        this.dao = dao;
    }

    public IBaseDao<T> getDao() {
        return dao;
    }

    public void insert(T t) {
        dao.insert(t);
    }

    public void update(T t) {
        dao.update(t);
    }

    public void delete(Integer id) {
        dao.delete(id);
    }

    public T selectOne(Integer id) {
        return dao.selectOne(id);
    }

    public List<T> selectAll() {
        return dao.selectAll();
    }

    /**
     * 分页查询
     */
    public List<T> selectPage(int offset, int len){
        return dao.selectPage(offset,len);
    }

    public int selectCount() {
        return dao.selectCount();
    }
}
