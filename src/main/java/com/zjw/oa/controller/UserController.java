package com.zjw.oa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjw.oa.domain.OperateLog;
import com.zjw.oa.entity.Dk;
import com.zjw.oa.entity.Dto.UserDto;
import com.zjw.oa.entity.Hys;
import com.zjw.oa.entity.User;
import com.zjw.oa.service.OperateLogService;
import com.zjw.oa.service.UserService;
import com.zjw.oa.util.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author dddz97
 * @date 2019-03-21 10:49:24
 */

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;
    @Resource
    private OperateLogService operateLogService;

    /**
     * Description 登录，可以加入@CrossOrigin支持跨域。
     *
     * @param user 用户输入的学号和密码
     * @return Result
     * @author dddz97
     * @date 2019-03-21 09:06:08
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    @CrossOrigin
    public JSONObject login(@RequestBody User user) {
        logger.info("===>into login: {}",JSONObject.toJSONString(user));
        User user1 = userService.login(user);
        OperateLog saveOperLog = new OperateLog();
        saveOperLog.setOperate("登录");
        saveOperLog.setCreateDate(new Date());

        if (user1 != null&&user1.getPermission()==1) {

            saveOperLog.setUserId(String.valueOf(user1.getUserId()));
            saveOperLog.setUserName(user1.getUserName());
            saveOperLog.setUserAccount(user1.getUserAccount());
            saveOperLog.setMsg("成功");
            operateLogService.save(saveOperLog);

            return JSON.parseObject("{statusCode:200,user:{isAdmin:false,userName:\""+user1.getUserName()+"\"," +
                    "zw:\"" + user1.getZw() + "\"," +
                    "userId:\"" + user1.getUserId() + "\"," +
                    "permission:" + user1.getPermission() + "}}");
        }else if(user1 != null){

            saveOperLog.setUserAccount(user.getUserAccount());
            saveOperLog.setMsg("失败");
            operateLogService.save(saveOperLog);

            return JSON.parseObject("{statusCode:200,user:{isAdmin:false,userName:\""+user1.getUserName()+"\"," +
                    "userId:\"" + user1.getUserId() + "\"," +
                    "zw:\"" + user1.getZw() + "\"," +
                    "permission:" + user1.getPermission() + "}}");
        }
        return JSON.parseObject("{statusCode:404}");
    }

    /**
     * Description 获取用户列表
     *
     * @author dddz97
     * @date 2019-03-22 10:50:47
     */
    @RequestMapping(value = "/userList")
    @ResponseBody
    @CrossOrigin
    public JSONArray getUserList(@RequestBody(required=false) User user) {
        List<User> list = userService.getUserList(user);
        String jsonStr = JsonUtil.serializeDate(list);
        return JSON.parseArray(jsonStr);
    }

    @RequestMapping(value = "/addDk")
    @ResponseBody
    @CrossOrigin
    public JSONObject addDk(@RequestBody Dk dk) {
        try{
            userService.addDk(dk);
        }catch (Exception e){
            return JSON.parseObject("{success:false,msg:\"打卡失败！\"}");
        }
        return JSON.parseObject("{success:true,msg:\"打卡成功！\"}");
    }

    @RequestMapping(value = "/getDkList")
    @ResponseBody
    @CrossOrigin
    public JSONArray getDkList(@RequestBody UserDto userDto) {
        List<UserDto> list = userService.getDkList(userDto);
        String jsonStr = JsonUtil.serializeDate(list);
        return JSON.parseArray(jsonStr);
    }

    @RequestMapping(value = "/getHysList")
    @ResponseBody
    @CrossOrigin
    public JSONArray getHysList() {
        List<Hys> list = userService.hysList();
        String jsonStr = JsonUtil.serializeDate(list);
        return JSON.parseArray(jsonStr);
    }

    @RequestMapping(value = "/updateHys")
    @ResponseBody
    @CrossOrigin
    public JSONObject updateHys(Hys hys) {
        try{
            userService.updateHys(hys);
        }catch (Exception e){
            System.out.println(e);
            return JSON.parseObject("{success:false,msg:\"更改状态失败！\"}");
        }
        return JSON.parseObject("{success:true,msg:\"更改状态成功！\"}");
    }

    @RequestMapping(value = "/ky")
    @ResponseBody
    @CrossOrigin
    public String kh(String time,String hysbh,List<String> userIdList) throws Exception {
        //List<User> userList = new ArrayList<>();
        for(int i=0;i<userIdList.size();i++){
            User user = new User();
            user.setUserId(Long.parseLong(userIdList.get(i)));
            User u = userService.getUser(user);
            try {
                message(u.getPhone(), hysbh, time);
            }catch (Exception e){
                return "false";
            }
        }
        return "true";
    }

    private void message(String phone,String hysbh,String time) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
        NameValuePair[] data = { new NameValuePair("Uid", "dddz97"),
                new NameValuePair("Key", "d41d8cd98f00b204e980"),
                new NameValuePair("smsMob", phone),
                new NameValuePair("smsText", "请于"+time+",到"+hysbh+"开会") };
        post.setRequestBody(data);

        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:" + statusCode);
        for (Header h : headers) {
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        System.out.println(result); // 打印返回消息状态

        post.releaseConnection();
    }

}
