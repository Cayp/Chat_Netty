package com.Interceptor;

import com.Utils.Response;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.net.NioEndpoint;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author ljp
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
            //判断该http请求 head首部是否带有authorization和是否与服务器存的一致，一致则通过
            String authorization = request.getHeader("authorization");
            String authorizationSession = (String) session.getAttribute("authorization");
            boolean isPass =  authorization != null && authorizationSession != null && authorization.equals(authorizationSession);
            if (!isPass) {
                Object jsonObject = JSONObject.toJSON(new Response().notVisit(40001, "需要登录"));
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    out.append(jsonObject.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
            return isPass;
    }
}
