package com.cheng.whiteboxmodel.utils;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String httpTokenHeader = "Authorization";
        final int unauthorizedErrorCode = 501;

        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {

            String token = request.getHeader(httpTokenHeader);

            String account = "";
            Jedis jedis = new Jedis(ConstantKit.REDIS_IP);

            if (token != null && token.length() > 1) {
                account = jedis.get(token);
            }

            System.err.println(token);
            System.err.println(account);

            if (account != null && account.length() > 1) {
                resetTokenExpireTime(jedis, account, token);

                // DEBUG
                System.err.println("auth pass!");

                return true;
            }

            JSONObject jsonObject = new JSONObject();

            PrintWriter out = null;
            try {

                // DEBUG
                System.err.println("auth fail!");

                response.setStatus(HttpStatus.ACCEPTED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                jsonObject.put("code", unauthorizedErrorCode);
                jsonObject.put("message", HttpStatus.UNAUTHORIZED);
                out = response.getWriter();
                out.println(jsonObject);

                return false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private void resetTokenExpireTime(Jedis jedis, String account, String token) {
        Long tokenBirthTime = Long.valueOf(jedis.get(account + token));
        Long diffTime = System.currentTimeMillis() - tokenBirthTime;

        if (diffTime > ConstantKit.TOKEN_RESET_TIME) {
            jedis.expire(account, ConstantKit.TOKEN_EXPIRE_TIME);
            jedis.expire(token, ConstantKit.TOKEN_EXPIRE_TIME);
            jedis.set(TokenGenerator.getStampKey(account, token), "" + System.currentTimeMillis());
            jedis.expire(TokenGenerator.getStampKey(account, token), ConstantKit.TOKEN_EXPIRE_TIME);
        }
    }
}
