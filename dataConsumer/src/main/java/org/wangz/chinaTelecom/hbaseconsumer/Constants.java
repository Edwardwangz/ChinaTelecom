package org.wangz.chinaTelecom.hbaseconsumer;

/**
 * 保存一些基本都常量信息
 *
 * @author Edward wangz
 * @date 2019/8/21 15:21
 */
public interface Constants {
    String HBASE_TABLENAME = "table.name";


    //kafka相关的配置信息
    String KAFKA_TOPIC = "topic";
    String KAFKA_GROUP_ID = "group.id";
    String BOOTSTRAP_SERVERS = "bootstrap.servers";
}
