package com.bit.mybatis.reflection;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReflectionUtil {
    public static void setPropToBean(Object bean, String propName, Object value) {
        Field declaredField = null;
        try {
            declaredField = bean.getClass().getDeclaredField(propName);
            declaredField.setAccessible(true);
            declaredField.set(bean, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setPropToBeanFromResultSet(Object entity, ResultSet resultset) throws SQLException {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Class<?> type = declaredFields[i].getType();
            String name = declaredFields[i].getName();
            if (type.isAssignableFrom(String.class)) {
                setPropToBean(entity,name,resultset.getString(camelTransfer(name)));
            }
            if (type.isAssignableFrom(Integer.class)) {
                setPropToBean(entity,name,resultset.getInt(camelTransfer(name)));
            }
            if (type.isAssignableFrom(Long.class)) {
                setPropToBean(entity,name,resultset.getLong(camelTransfer(name)));
            }
            if (type.isAssignableFrom(Date.class)) {
                Timestamp timestamp = resultset.getTimestamp(camelTransfer(name));
                if (timestamp == null) {
                    continue;
                }
                Date time1=new Date(timestamp.getTime()); //java.util.Date
                setPropToBean(entity,name,time1);
            }
        }
    }

    private static String camelTransfer(String name) {
        if (name == null && name.length() < 1) {
            return "";
        }
        char[] chars = name.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (i == 0) {
                stringBuilder.append(aChar);
                continue;
            }
            if (Character.isUpperCase(aChar)) {
                stringBuilder.append("_");
                aChar = Character.toLowerCase(aChar);
            }
            stringBuilder.append(aChar);
        }
        return stringBuilder.toString();
    }
}
