package org.wangz.ssm.service;

import org.wangz.ssm.domain.CallLog;
import org.wangz.ssm.domain.CallLogRange;

import java.util.List;

/**
 * @author ：Edward wangz
 * @date ：2019/8/22 16:09
 */
public interface ICallLogService extends IBaseService<CallLog>{
    //查询所有通话记录
    List<CallLog> findAll();
    //按照范围查询通话记录
    List<CallLog> findCallogs(String call,List<CallLogRange> list);
}
