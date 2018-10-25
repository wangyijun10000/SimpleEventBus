package com.wyj.simpleeventbus;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 仿EventBus观察者模式
 * Created by wyj on 2018/10/25.
 */
public class EventBus {

    /**
     * 保存订阅类
     */
    private HashMap<Class<?>, Object> subscriptionsByEventType;

    public EventBus() {
        subscriptionsByEventType = new HashMap<>();
    }

    private static class InstanceHolder {
        static EventBus INSTANCE = new EventBus();
    }

    /**
     * 获取默认实现
     *
     * @return
     */
    public static EventBus getDefault() {
        return EventBus.InstanceHolder.INSTANCE;
    }

    /**
     * 订阅
     *
     * @param o
     */
    public void register(Object o) {
        subscriptionsByEventType.put(o.getClass(), o);
    }

    /**
     * 取消订阅
     *
     * @param o
     */
    public void unregister(Object o) {
        if (!subscriptionsByEventType.isEmpty()) {
            subscriptionsByEventType.remove(o.getClass());
        }
    }

    /**
     * 查找实现了观察者注解的方法，并且调用
     *
     * @param oserver
     */
    private void findAnnotationMethod(Object oserver, Object event) {
        Class<?> aClass = oserver.getClass();
        Method[] methods = aClass.getMethods();
        try {
            for (Method method : methods) {
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe != null) {
                    Class<?>[] classes = method.getParameterTypes();
                    if (classes.length != 1) {
                        continue;
                    }
                    for (Class<?> clazz : classes) {
                        if (event.getClass() == clazz) {
                            method.invoke(oserver, event);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送广播
     *
     * @param event
     */
    public void post(Object event) {
        Iterator<Map.Entry<Class<?>, Object>> iterator = subscriptionsByEventType.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class<?>, Object> entry = iterator.next();
            Object oserver = entry.getValue();
            findAnnotationMethod(oserver, event);
        }
    }
}