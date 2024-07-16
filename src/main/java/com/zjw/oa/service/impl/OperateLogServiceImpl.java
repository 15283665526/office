package com.zjw.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjw.oa.domain.OperateLog;
import com.zjw.oa.service.OperateLogService;
import com.zjw.oa.mapper.OperateLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 24382
* @description 针对表【operate_log】的数据库操作Service实现
* @createDate 2024-07-05 22:04:38
*/
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog>
implements OperateLogService{

}
