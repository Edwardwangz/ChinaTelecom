package org.wangz.ssm.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.wangz.ssm.dao.IBaseDao;
import org.wangz.ssm.domain.User;

import java.util.List;

/**
 */
@Repository("userDao")
public class UserDaoImpl extends SqlSessionDaoSupport implements IBaseDao<User> {

    public void insert(User user) {
        getSqlSession().insert("users.insert",user);
    }

    public void update(User user) {
        getSqlSession().update("users.update", user);
    }

    public void delete(Integer id ) {
        getSqlSession().delete("users.delete", id);
    }

    public User selectOne(Integer id) {
        return getSqlSession().selectOne("users.selectOne",id) ;
    }

    public List<User> selectAll() {
        return getSqlSession().selectList("users.selectAll");
    }

    public List<User> selectPage(int offset, int len) {
        return getSqlSession().selectList("users.selectPage",new RowBounds(offset, len));
    }

    public int selectCount() {
        return getSqlSession().selectOne("users.selectCount");
    }
}
