package com.hyh.springboot.Controller;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.CookieStore;
import java.net.HttpCookie;

public class getCookies {

    public  String getCookie(String url) throws IOException {
        // 全局请求设置
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        // 创建cookie store的本地实例
        CookieStore cookieStore = (CookieStore) new BasicCookieStore();
        // 创建HttpClient上下文
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore((org.apache.http.client.CookieStore) cookieStore);

        // 创建一个HttpClient
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig)
                .setDefaultCookieStore((org.apache.http.client.CookieStore) cookieStore).build();

        CloseableHttpResponse res = null;

        // 创建一个get请求用来获取必要的Cookie，如_xsrf信息
        HttpGet get = new HttpGet(url);

        res = httpClient.execute(get, context);
        // 获取常用Cookie,包括_xsrf信息
        StringBuffer cookie=new StringBuffer();
        for (HttpCookie c : cookieStore.getCookies()) {
            //拼接所有cookie变成一个字符串；
            c.setMaxAge(24 * 60 * 60);
            c.setPath("/");
            cookie.append(c.getName()+"="+c.getValue()+";");
            System.out.println(c.getName() + ": " + c.getValue());
        }

        String cookieres=cookie.toString();
        cookieres=cookieres.substring(0,cookieres.length()-1);
        res.close();
        return cookieres;
    }

}
