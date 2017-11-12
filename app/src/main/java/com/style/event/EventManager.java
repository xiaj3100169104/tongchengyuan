package com.style.event;

import org.simple.eventbus.EventBus;

/**
 * Created by xiajun on 2017/5/27.
 */

public class EventManager {

    /**
     * The Default EventBus instance
     */
    private static EventManager eventManager;

    private EventManager() {

    }

    public static EventManager getDefault() {
        if (eventManager == null) {
            synchronized (EventManager.class) {
                if (eventManager == null) {
                    eventManager = new EventManager();
                }
            }
        }
        return eventManager;
    }

    public void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public void unRegister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发布事件
     *
     * @param event 要发布的事件
     * @param tag   事件的tag, 类似于BroadcastReceiver的action
     */
    public void post(String tag, Object event) {
        EventBus.getDefault().post(event, tag);
    }
}
