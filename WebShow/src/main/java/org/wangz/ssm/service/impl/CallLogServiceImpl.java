package org.wangz.ssm.service.impl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;
import org.wangz.ssm.domain.CallLog;
import org.wangz.ssm.domain.CallLogRange;
import org.wangz.ssm.service.ICallLogService;
import org.wangz.ssm.util.CallLogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：Edward wangz
 * @date ：2019/8/22 16:03
 */
@Service("callLogService")
public class CallLogServiceImpl extends BaseServiceImpl<CallLog> implements ICallLogService {
    private Table table;

    public CallLogServiceImpl() {
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", "node2,node3,node4");
            Connection conn = ConnectionFactory.createConnection(conf);
            TableName name = TableName.valueOf("hbase:calllogs");
            table = conn.getTable(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有log
     */
    public List<CallLog> findAll() {
        List<CallLog> list = new ArrayList<CallLog>();
        try {
            Scan scan = new Scan();
            ResultScanner rs = table.getScanner(scan);
            Iterator<Result> it = rs.iterator();
            byte[] family = Bytes.toBytes("f1");

            byte[] calling = Bytes.toBytes("calling");
            byte[] called = Bytes.toBytes("called");
            byte[] callTime = Bytes.toBytes("callTime");
            byte[] duration = Bytes.toBytes("duration");
            CallLog log = null;
            while (it.hasNext()) {
                log = new CallLog();
                Result r = it.next();
                log.setCalling(Bytes.toString(r.getValue(family, calling)));
                log.setCalled(Bytes.toString(r.getValue(family, called)));
                log.setCallTime(Bytes.toString(r.getValue(family, callTime)));
                log.setDuration(Bytes.toString(r.getValue(family, duration)));
                list.add(log);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照范围查询通话记录
     */
    public List<CallLog> findCallogs(String caller, List<CallLogRange> ranges) {
        List<CallLog> logs = new ArrayList<CallLog>();
        try {
            for (CallLogRange range : ranges) {
                Scan scan = new Scan();
                //设置扫描起始行
                scan.setStartRow(Bytes.toBytes(CallLogUtil.getStartRowkey(caller, range.getStartPoint(), 100)));
                //设置扫描结束行
                scan.setStopRow(Bytes.toBytes(CallLogUtil.getStopRowkey(caller, range.getStartPoint(), range.getEndPoint(), 100)));

                ResultScanner rs = table.getScanner(scan);
                Iterator<Result> it = rs.iterator();
                byte[] family = Bytes.toBytes("f1");

                byte[] calling = Bytes.toBytes("calling");
                byte[] called = Bytes.toBytes("called");
                byte[] callTime = Bytes.toBytes("callTime");
                byte[] duration = Bytes.toBytes("duration");
                CallLog log = null;
                while (it.hasNext()) {
                    log = new CallLog();
                    Result r = it.next();
                    //rowkey
                    String rowkey = Bytes.toString(r.getRow());
                    String flag = rowkey.split(",")[3];
                    log.setFlag(flag.equals("0") ? true : false);
                    //calling
                    log.setCalling(Bytes.toString(r.getValue(family, calling)));
                    //called
                    log.setCalled(Bytes.toString(r.getValue(family, called)));
                    //callTime
                    log.setCallTime(Bytes.toString(r.getValue(family, callTime)));
                    //duration
                    log.setDuration(Bytes.toString(r.getValue(family, duration)));
                    logs.add(log);
                }
            }
            return logs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
