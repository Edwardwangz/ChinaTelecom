package org.wangz.ssm.service;


import org.wangz.ssm.domain.User;

import java.util.List;

/**
 *
 */
public interface IUserService extends IBaseService<User> {
    void longTx();

    void save(User u);

    List<User> selectPage(int offset, int len);
}
