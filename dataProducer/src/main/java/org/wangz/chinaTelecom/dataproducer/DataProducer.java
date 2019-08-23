package org.wangz.chinaTelecom.dataproducer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * 该类用来生成通话日志
 * 主要有 主叫用户，被叫用户，时间，通话时长等信息
 */
public class DataProducer {

    private static Random r = new Random();


    /**
     * @param args 参数为文件输出的位置：例如：    “/usr/local/files/calllog.log”
     * @throws IOException 如果file 不存在，抛出异常
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args == null || args.length == 0) {
            System.out.println("args can not be null !");
            System.exit(-1);
        }
        while(true) {
            String log = produceLog();
            writeToFile(args[0], log);
            Thread.sleep(200);
        }
/*        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {

            date = sdf.parse(produceDateTime("2019"));

        } catch (ParseException e) {

            e.printStackTrace();

        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);*/
    }

    /**
     * 此方法调用本类的另外两个producePhoneNum  和  produceDateTime方法
     * 用于生成日志文件信息，信息依次为：
     * 主叫号码 被叫号码    通话日期    通话时长
     *
     * @return 日志信息
     */
    private static String produceLog() {
        String callingNum = producePhoneNum();
        String calledNum = producePhoneNum();
        String callDate = produceDateTime("2019");
        String callDuration = String.format("%3d", r.nextInt(150));

        return callingNum + "\t" + calledNum + "\t" + callDate + "\t" + callDuration + "\n";
    }

    /**
     * 把产生的日志信息写入到文件中
     * 一次追加一行数据
     *
     * @param outputPath 日志文件输出路径
     * @param log        需要写入文件的日志信息
     * @throws IOException 若文件不存在或写入失败，则抛出该异常
     */
    private static void writeToFile(String outputPath, String log) throws IOException {

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputPath, true));//第二个参数为true，即为append数据，不overwrite
        osw.write(log);
        osw.flush();
    }

    /**
     * 随机生成电话号码数据，用于生成通话日志文件
     *
     * @return 返回一个电话号码
     */
    private static String producePhoneNum() {
/*        String[] phonePrefix = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        String phoneFirst = phonePrefix[r.nextInt(phonePrefix.length)];
        String phoneSecond = String.format("%08d", r.nextInt(100000000));
        return phoneFirst + phoneSecond;*/
        String[] phoneNums = {"13355452179","13961848258","13260490456","15078486552","13956020316","13229511285",
                "15537757690","15868859465","15097271769","13991685222","15135745376","15103634762",
                "15327088085","13092189833","15207772437","13439875415","13273232238","15833339954",
                "15940748185","13649685677","15518710484","15270711127","15730976797","15855832523",
                "13535615625","15038225361","15860239299","13035850674","13056323233","13384316058"};
        return phoneNums[r.nextInt(30)];
    }

    /**
     * 随机生成给定年份的日期时间数据，用于生成通话日志文件，以便后续的计算分析
     * 格式为：2019-07-19 17:15:20
     *
     * @param year 给定年份之后，随机生成后面的月、日等信息
     * @return 例如这样的2019-07-19 17:15:20 日期格式
     */
    private static String produceDateTime(String year) {
        String month = String.format("%02d", r.nextInt(12));
        String date = String.format("%02d", r.nextInt(31));
        String hour = String.format("%02d", r.nextInt(24));
        String min = String.format("%02d", r.nextInt(60));
        String sec = String.format("%02d", r.nextInt(60));

        return year + "-" + month + "-" + date + " " + hour + ":" + min + ":" + sec;
    }

}
