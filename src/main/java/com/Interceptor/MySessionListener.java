package com.Interceptor;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * @author ljp
 */
@WebListener
public class MySessionListener implements HttpSessionListener {
    private static Integer online = 0;

    public static Integer getOnline() {
        return online;
    }

    @Override
    public synchronized void sessionCreated(HttpSessionEvent se) {
        online++;
    }

    @Override
    public synchronized void sessionDestroyed(HttpSessionEvent se) {
       online--;
    }
}
