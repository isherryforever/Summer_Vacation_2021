package com.hyh.springboot.Controller;

//import com.hyh.springboot.Dao.BookDao;
import com.hyh.springboot.Dao.Dht11Dao;
import com.hyh.springboot.Dao.RegNLog;
//import com.hyh.springboot.Dao.SDao;
//import com.hyh.springboot.bean.Book;
import com.hyh.springboot.bean.Dht11;
import com.hyh.springboot.service.MailService;

//import com.hyh.springboot.service.BookService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static com.hyh.springboot.service.SchService.number;

@Controller

public class LoginController {
    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    Dht11Dao dht11Dao;
    String url="http://127.0.0.1:8080/";
    getCookies getCookies;
    MailService ms=new MailService();
    @RequestMapping("/index.html")
    public String index(Model model)
    {
    	dht11Dao.get();
        Collection<Dht11> s = dht11Dao.getNow();
       // System.out.println("display数据"+s);w
        model.addAttribute("sNow",s);//MVC模式向前端传递值，定义为s2
        return "index";
    }
    
    @RequestMapping({"/","page-login.html"})
    public String login(Model model)
    {
    	return "page-login";
    }
    @RequestMapping("/page-register.html")
    public String register(Model model)
    {
    	return "page-register";
    }
    @RequestMapping("/registers")
    public ModelAndView zhuce()
    {
    	ModelAndView mv=new ModelAndView("register");
    	return mv;
    }
    @PostMapping(value="/login")
    public String login(@RequestParam("username") String username,
    					@RequestParam("password") String password,
    					Map<String,Object> map,HttpSession session,HttpServletRequest request,HttpServletResponse response)throws IOException
    {
    	RegNLog rnl=new RegNLog();
    	if(rnl.checkPassword(username,password))
    	{
    		return "index";
    	}
    	else
    	{
    		map.put("msg", "密码错误");
    		return "page-login";
    	}
    }
    @PostMapping(value="/register")
    public String login(@RequestParam("username") String username,
    					@RequestParam("email") String email,
    					@RequestParam("password") String password,
    					Map<String,Object> map,HttpSession session,HttpServletRequest request,HttpServletResponse response) throws IOException
    {
    	RegNLog rnl=new RegNLog();
    	if(rnl.checkifExists(username, password, email)) {
    		map.put("msg0", "账号或邮箱已存在");
    		return "page-register";
    	}
    	else {
    		map.put("msg", "注册成功");
    		return "page-login";
    	}
//    	if(!StringUtils.isEmpty(username)&&"123456".equals(password))
//    	{
//    		session.setAttribute("loginUser", username);
//    		if("root".equals(username))
//    		{
//    			getCookies.getCookie(url+"user.login");
//    		}
//    		return "redirect:/main.html";
//    	}
//    	else
//    	{
//    		map.put("msg", "用户名密码错误");
//    		return "page-register";
//    	}
    }
//    @PostMapping(value = "/user/login")
//    public String login(@RequestParam("username") String username,
//                        @RequestParam("password") String password,
//                        Map<String,Object> map, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException 
//    {
//
//        if(!StringUtils.isEmpty(username) && "123456".equals(password))
//        {
////            Cookie cookie = new Cookie("sessionid","123456");
////            response.addCookie(cookie);
//            session.setAttribute("loginUser",username);
//            if("root".equals(username))
//            {
//                getCookies.getCookie(url+"user.login");
//            }
//            return "redirect:/main.html";
//        }
//        else
//        {map.put("msg","用户名密码错误");
//            return "page-login";}
//    }

    @RequestMapping("/success")//实时数据
    public String success(Model model)
    {
    dht11Dao.get();
    Dht11 s = dht11Dao.getone();
    model.addAttribute("s1",s);//MVC模式向前端传递值，定义为s1
    return "success";
    }
    @RequestMapping("/monitor.html")
    public String toMon()
    {
    	return "monitor";
    }
    @RequestMapping("/dashboard")
    public String dashboard()
    {
        return "redirect:/main.html";
    }

    @RequestMapping("/mqtt")
    public String mqtt(Model model)
    { model.addAttribute("num",number);
        return "mqttsend";}
    
   

    @RequestMapping("/on")
    public String on(Model model)
    {
        number = "开启";
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news","1");
        ms.test1("电机打开！");
        System.out.println("按钮点击 发送到mq队列1");
        dht11Dao.schon();
        model.addAttribute("num",number);
        return  "index";
    }
    @RequestMapping("/off")
    public String off(Model model)
    {
        number = "关闭";
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news","0");
        ms.test1("电机关闭!");
        System.out.println("按钮点击 发送到mq队列0");
        dht11Dao.schoff();
        model.addAttribute("num",number);
        return  "index";
    }
    @RequestMapping("/display")//历史数据
    public String display(Model model)
    {   dht11Dao.get();
        Collection<Dht11> s = dht11Dao.getfifty();
       // System.out.println("display数据"+s);
        model.addAttribute("s2",s);//MVC模式向前端传递值，定义为s2
        return "display";
    }

    @RequestMapping("/chart")//历史数据
    public String display2(Model model)
    {
        return "display2";
    }


    @RequestMapping("/mychart")
   @ResponseBody
   public Dht11 mychart(Model model)
   {
       dht11Dao.get();

       return dht11Dao.getone();
   }
    
    @RequestMapping("/hischart")
    @ResponseBody
    public Collection<Dht11> hischart()
    {
        dht11Dao.get();
        return dht11Dao.getAll();
    }
}
