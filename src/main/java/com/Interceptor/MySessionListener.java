package com.Interceptor;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author ljp
 */
@WebListener
public class MySessionListener implements HttpSessionListener {
    private static AtomicInteger online = new AtomicInteger(0);

    public static int getOnline() {
        return online.get();
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        online.getAndIncrement();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
       online.getAndDecrement();
    }
}
