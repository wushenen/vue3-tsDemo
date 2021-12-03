package com.qtec.unicom.component.fastJsonConfig;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.qtec.unicom.component.util.DateUtils;

import java.lang.reflect.Field;
import java.util.Date;

public class BeanPropertyFilter implements ValueFilter {
    private Field field = null;
    @Override
    public Object process(Object obj, String name, Object value) {
        Boolean flag = false;
        try {
            field = obj.getClass().getDeclaredField(name);
            // 获取注解
//            flag = field.getAnnotation(FieldToString.class).value().equals("true");
            if (value == null) {
                // 这里 其他类型转换成String类型
                value = "";
            }
            if(field.getType() == Date.class){
                value = DateUtils.getZTimeStr((Date)value);
            }
        } catch (NoSuchFieldException e) {
            return value;
        } catch (Exception e) {
            return value;
        }
        return value;
    }
}
