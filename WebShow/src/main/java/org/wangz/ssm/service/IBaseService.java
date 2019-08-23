package org.wangz.ssm.service;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */
public interface IBaseService<T> {
    void insert(T t);

    void update(T t);

    void delete(Integer id);

    T selectOne(Integer id);

    List<T> selectAll();

    /**
     * 分页查询
     */
    List<T> selectPage(int offset, int len);

    int selectCount();
}
