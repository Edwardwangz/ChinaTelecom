package org.wangz.chinaTelecom.HbaseCoprocessor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 协处理器,
 */
public class CallLogRegionObserver extends BaseRegionObserver {

    //被叫引用id
    private static final String REF_ROWKEY = "refRowKey" ;
    //通话记录表名
    private static final String CALL_LOG_TABLE_NAME = "hbase:calllogs" ;

    /**
     * Put后处理
     */
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e,
                        Put put, WALEdit edit, Durability durability) throws IOException {
        super.postPut(e, put, edit, durability);
        //
        String tableName0 = TableName.valueOf(CALL_LOG_TABLE_NAME).getNameAsString();

        //得到当前的TableName对象
        String tableName1 = e.getEnvironment().getRegion().getRegionInfo().getTable().getNameAsString();
        //判断是否是hbase:calllogs表
        if (!tableName0.equals(tableName1)) {
            return;
        }

        //得到主叫的rowKey,格式为：59,13815885930,20190906080500,0/1,15969824568,45
        String rowKey = Bytes.toString(put.getRow());

        //如果被叫就放行
        String[] arr = rowKey.split(",");
        if (arr[3].equals("1")) {
            return;
        }
        //hashcode,calling,time,flag,called,duration
        String calling = arr[1] ;        //主叫
        String callTime = arr[2] ;      //通话时间
        String called = arr[4] ;        //被叫
        String duration = arr[5] ;  //通话时长

        //被叫hashcode
        String hashcode = CallLogUtil.getHashcode(called,callTime,100);
        //被叫rowKey
        String calledRowKey = hashcode + "," + called + "," + callTime + ",1," + calling + "," + duration;
        Put newPut = new Put(Bytes.toBytes(calledRowKey));
        newPut.addColumn(Bytes.toBytes("f2"), Bytes.toBytes(REF_ROWKEY), Bytes.toBytes(rowKey));
        TableName tn = TableName.valueOf(CALL_LOG_TABLE_NAME);
        Table t = e.getEnvironment().getTable(tn);
        t.put(newPut);
    }

    /**
     * 重写方法，完成被叫查询，返回主叫结果。
     */
    @Override
    public void postGetOp(ObserverContext<RegionCoprocessorEnvironment> e,
                          Get get, List<Cell> results) throws IOException {
        //获得表名
        String tableName = e.getEnvironment().getRegion().getRegionInfo().getTable().getNameAsString();

        //判断表名是否是hbase:calllogs
        if(!tableName.equals(CALL_LOG_TABLE_NAME)){
            super.preGetOp(e, get, results);
        }
        else{
            //得到rowKey
            String rowKey = Bytes.toString(get.getRow());
            //
            String[] arr = rowKey.split(",");
            //主叫
            if(arr[3].equals("0")){
                super.postGetOp(e, get, results);
            }
            //被叫
            else{
                //得到主叫方的rowKey
                String refRowkey = Bytes.toString(CellUtil.cloneValue(results.get(0)));
                //
                Table tt = e.getEnvironment().getTable(TableName.valueOf(CALL_LOG_TABLE_NAME));
                Get g = new Get(Bytes.toBytes(refRowkey));
                Result r = tt.get(g);
                List<Cell> newList = r.listCells();
                results.clear();
                results.addAll(newList);
            }
        }
    }
    /**
     *重写方法，完成被叫查询，返回主叫结果
     */
    @Override
    public boolean postScannerNext(ObserverContext<RegionCoprocessorEnvironment> e,
                                   InternalScanner s, List<Result> results, int limit, boolean hasMore) throws IOException {
        boolean b = super.postScannerNext(e, s, results, limit, hasMore);

        //新集合
        List<Result> newList = new ArrayList<Result>();

        //获得表名
        String tableName = e.getEnvironment().getRegion().getRegionInfo().getTable().getNameAsString();

        //判断表名是否是hbase:calllogs
        if (tableName.equals(CALL_LOG_TABLE_NAME)) {
            Table table = e.getEnvironment().getTable(TableName.valueOf(CALL_LOG_TABLE_NAME));
            for(Result r : results){
                //rowKey
                String rowKey = Bytes.toString(r.getRow());
                String flag = rowKey.split(",")[3] ;
                //主叫
                if(flag.equals("0")){
                    newList.add(r) ;
                }
                //被叫
                else{
                    //取出主叫号码
                    byte[] refRowKey = r.getValue(Bytes.toBytes("f2"),Bytes.toBytes(REF_ROWKEY)) ;
                    Get newGet = new Get(refRowKey);
                    newList.add(table.get(newGet));
                }
            }
            results.clear();
            results.addAll(newList);
        }
        return b ;
    }
}
