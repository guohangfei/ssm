package converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: 郭航飞
 * @Description: 将传入的yyyy-MM-dd 日期的字符串转换成日期格式
 * @Date: created in      2018/4/917:24
 */
 /**
  * @Author:         郭航飞
  * @Description:
  * @CreateDate:   2018/4/10 9:04
  * @param：       No such property: code for class: Script1
  */
public class CustomDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        //实现 将日期串转成日期类型(格式是yyyy-MM-dd HH:mm:ss)
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果参数绑定失败返回null
        return null;
    }
}
