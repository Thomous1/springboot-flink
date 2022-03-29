package com.example.springflink.utils;

import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author wangzuoyu1
 * @description application工具类
 */

@Component
@Lazy(value = false)
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == ApplicationContextUtil.context) ApplicationContextUtil.context = applicationContext;
    }

    public static Object getBeanByName(String name) {
        if (Objects.isNull(context)) {
            throw new ApplicationContextException("Spring applicationContext not Found!");
        }
        return context.getBean(name);
    }

    public static <T> T getBeanByType(Class<T> clazz) {
        if (Objects.isNull(context)) {
            throw new ApplicationContextException("Spring applicationContext not Found!");
        }
        return context.getBean(clazz);
    }

    public static ApplicationContext getApplicationContext() {
        if (Objects.isNull(context)) {
            throw new ApplicationContextException("Spring applicationContext not Found!");
        }
        return context;
    }
}
