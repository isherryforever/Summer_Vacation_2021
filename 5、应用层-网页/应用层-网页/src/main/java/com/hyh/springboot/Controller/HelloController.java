package com.hyh.springboot.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
@Controller
public class HelloController {
    @RequestMapping("/i18n")
    public String changeSessionLanauage(HttpServletRequest request, String lang){
        System.out.println("controller-param:"+lang);
        if("zh_CN".equals(lang)){
            //代码中即可通过以下方法进行语言设置
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,new Locale("zh","CN"));
        }else if("en_US".equals(lang)){
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,new Locale("en","US"));
        }
        return "redirect:/index.html";
    }
}
