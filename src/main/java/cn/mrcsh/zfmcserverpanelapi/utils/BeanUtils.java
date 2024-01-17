package cn.mrcsh.zfmcserverpanelapi.utils;

import java.lang.reflect.Field;

public class BeanUtils{
    public static void copyProperty(Object from,Object to) throws IllegalAccessException {
        for (Field declaredField : from.getClass().getDeclaredFields()) {
            for (Field field : to.getClass().getDeclaredFields()) {
                if(declaredField.getName().equals(field.getName())){
                    declaredField.setAccessible(true);
                    field.setAccessible(true);
                    field.set(to, declaredField.get(from));
                }
            }
        }
    }
}
