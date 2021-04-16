package tool.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String getBeforeHour(int numberOfHours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -numberOfHours);
        return formatDate(calendar.getTime(), "yyyyMMddHH");
    }

    /**
     * 取得指定日期格式的字符串
     *
     * @param date
     * @return String
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getBeforeHour(0));
    }

}
