package org.wangz.chinaTelecom.hbaseconsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

public class HBaseConsumer {

    public static void main(String[] args) {
        HBaseDao dao = new HBaseDao();
//        dao.truncateTable("hbase:calllogs");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(PropertiesUtil.getProperties());
        consumer.subscribe(Collections.singletonList(PropertiesUtil.getProp(Constants.KAFKA_TOPIC)));
        while(true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord record : records) {
                System.out.printf("offset = %d, key = %s, value = %s",
                        record.offset(), record.key(), record.value());
                System.out.println();
                dao.put(record.value().toString());
            }
        }
    }
}
