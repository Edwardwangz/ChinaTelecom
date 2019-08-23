package org.wangz.chinaTelecom.hbaseconsumer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

/**
 * 该DAO类，用于对HBase进行一些基本的CRUD操作
 */
public class HBaseDao {
    private Admin admin;//Admin 类，用于管理表
    private Table table;//Table 类，用于管理表中的数据

    public HBaseDao() {
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", "node2,node3,node4");
            Connection conn = ConnectionFactory.createConnection(conf);
            TableName tableName = TableName.valueOf(PropertiesUtil.getProp(Constants.HBASE_TABLENAME));
            admin = conn.getAdmin();
            table = conn.getTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * put 数据到 HBase中
     * 数据格式为：
     * 13454151335	13662449734	2019-03-12 06:50:44	 65
     * 主叫 被叫 通话时间 通话时长（分钟）
     * RowKey为
     * hash值，calling，callTime，0/1，called，duration
     */
    public void put(String log) {
        if (log == null || "".equals(log)) return;

        String[] values = log.split("\t");
        String calling = null;
        String called = null;
        String callTime = null;
        String duration = null;
        if (values != null && values.length == 4) {
            calling = values[0];
            called = values[1];
            callTime = values[2];
            duration = values[3];
        }
        String rowKey = getRowKey(calling, callTime, "0", called, duration, 100);
        Put put = new Put(rowKey.getBytes());
        put.addColumn("f1".getBytes(), "calling".getBytes(), calling.getBytes());
        put.addColumn("f1".getBytes(), "called".getBytes(), called.getBytes());
        put.addColumn("f1".getBytes(), "callTime".getBytes(), callTime.getBytes());
        put.addColumn("f1".getBytes(), "duration".getBytes(), duration.getBytes());

        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据主叫号码、通话时间以及分区数进行hash散列
     *
     * @param calling
     * @param callTime
     * @param partitions
     * @return
     */
    public int getHashcode(String calling, String callTime, int partitions) {
        int last4Nums = Integer.valueOf(calling.substring(calling.length() - 4));//取出主叫号码后4位
        callTime = callTime.replaceAll("-","");
        int mon = Integer.valueOf(callTime.substring(0, 6));//取出通话时间的年月部分，如（201906）
        int hashcode = (last4Nums ^ mon) % partitions;
        return Integer.valueOf(String.format("%02d", hashcode));
    }

    public String getRowKey(String calling, String callTime, String flag,
                            String called, String duration, int partitions) {
        //2019-02-12 08:05:00 变为20190212080500
        callTime = callTime.replaceAll("-","")
                            .replaceAll(" ","")
                            .replaceAll(":","");
        int hashcode = getHashcode(calling, callTime, partitions);
        return hashcode + "," + calling + "," + callTime + "," + flag + "," + called + "," + duration;
    }

    /**
     * 清空表
     *
     * @param tableName 要清空的表名称
     */
    public void truncateTable(String tableName) {
        if (tableName == null || "".equals(tableName)) return;
        try {
            admin.disableTable(TableName.valueOf(tableName));
            admin.truncateTable(TableName.valueOf(tableName), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
