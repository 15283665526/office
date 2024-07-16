package com.zjw.oa.controller;

import com.alibaba.fastjson.JSON;
import com.zjw.oa.domain.OperateLog;
import com.zjw.oa.service.OperateLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 操作日志controller
 * @author 24382
 */
@RestController
@RequestMapping("/operateLog")
public class OperateLogController {

    private static final Logger logger = LoggerFactory.getLogger(OperateLogController.class);

    @Resource
    private OperateLogService operateLogService;

    @RequestMapping(value = "/list")
    @ResponseBody
    @CrossOrigin
    public String upload(String rzTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if (StringUtils.isEmpty(rzTime)){
            rzTime = sdf.format(date);
            logger.info("=====>默认时间：{}",rzTime);
        }

        List<OperateLog> list = operateLogService.lambdaQuery().eq(OperateLog::getCreateDate, rzTime).list();
        return JSON.toJSONString(list);
    }

}
