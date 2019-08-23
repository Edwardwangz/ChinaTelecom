package org.wangz.ssm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wangz.ssm.domain.CallLog;
import org.wangz.ssm.domain.CallLogRange;
import org.wangz.ssm.service.ICallLogService;
import org.wangz.ssm.util.CallLogUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：Edward wangz
 * @date ：2019/8/22 16:22
 */
@Controller
public class CallLogController {

    @Resource(name = "callLogService")
    private ICallLogService cs;

    //查询所有通话记录
    @RequestMapping("/callLog/findAll")
    public String findAll(Model m) {
        List<CallLog> list = cs.findAll();
        m.addAttribute("callLogs", list);
        return "callLog/callLogList";
    }

    //进入查询通话记录的页面,form
    @RequestMapping("/callLog/findCallLogPage")
    public String toFindCallLogPage(){
        return "callLog/findCallLog" ;
    }

    //查询指定时间段内，某个用户的通话记录
    @RequestMapping(value = "/callLog/findCallLog",method = RequestMethod.POST)
    public String findCallLog(Model m , @RequestParam("calling") String calling, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime){
        List<CallLogRange> list = CallLogUtil.getCallLogRanges(startTime, endTime);
        List<CallLog> logs = cs.findCallogs(calling,list);
        m.addAttribute("callLogs", logs);
        return "callLog/callLogList" ;
    }
}