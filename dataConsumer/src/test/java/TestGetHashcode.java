import org.junit.Test;

/**
 * @author ：Edward wangz
 * @date ：2019/8/23 12:40
 */
public class TestGetHashcode {
    @Test
    public void test() {
        String rowKey = getRowKey("13815885930","2019-02-12 08:20:30",
                "0","15813665960","45",100);

        System.out.println(rowKey);
    }

    public int getHashcode(String calling, String callTime, int partitions) {
        int last4Nums = Integer.valueOf(calling.substring(calling.length() - 4));//取出主叫号码后4位
        int mon = Integer.valueOf(callTime.substring(0, 6));//取出通话时间的年月部分，如（201906）
        int hashcode = (last4Nums ^ mon) % partitions;
        return Integer.valueOf(String.format("%02d", hashcode));
    }

    public String getRowKey(String calling, String callTime, String flag,
                            String called, String duration, int partitions) {
        //2019-02-12 08:05:00 变为20190212080500
        callTime = callTime.replaceAll("-", "")
                .replaceAll(" ", "")
                .replaceAll(":", "");
        int hashcode = getHashcode(calling, callTime, partitions);
        return hashcode + "," + calling + "," + callTime + "," + flag + "," + called + "," + duration;
    }
}
