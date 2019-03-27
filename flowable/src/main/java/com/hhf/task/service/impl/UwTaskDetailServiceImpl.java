package com.hhf.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhf.common.exception.BusinessException;
import com.hhf.demo.model.Person;
import com.hhf.flowable.constant.ProcessThreadLocal;
import com.hhf.flowable.dto.CompleteTaskReqDTO;
import com.hhf.flowable.dto.HandlerReqDTO;
import com.hhf.flowable.service.FlowableService;
import com.hhf.myrabbitmq.core.message.AddPersonMessage;
import com.hhf.myrabbitmq.utils.MessageSendUtils;
import com.hhf.task.dto.AuditTaskReqDTO;
import com.hhf.task.dto.TaskAssigeeReqDTO;
import com.hhf.task.mapper.UwTaskDetailMapper;
import com.hhf.task.model.UwTaskDetail;
import com.hhf.task.service.UwTaskDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-19
 */
@Service
public class UwTaskDetailServiceImpl extends ServiceImpl<UwTaskDetailMapper, UwTaskDetail> implements UwTaskDetailService {

    private static final Logger log = LoggerFactory.getLogger(UwTaskDetailServiceImpl.class);
    @Autowired
    FlowableService flowableService;
    @Autowired
    UwTaskDetailMapper uwTaskDetailMapper;

    @Override
    public void receiveTask(TaskAssigeeReqDTO taskAssigeeReqDTO) {
        //TODO 判断用户是否能领取  领取校验
        if (taskAssigeeReqDTO == null) {
            throw new BusinessException("领取对象不能为空");
        }
        if (StringUtils.isEmpty(taskAssigeeReqDTO.getTaskDetailId())) {
            throw new BusinessException("任务id不能为空");
        }
        if (taskAssigeeReqDTO.getHandlerDTO() == null) {
            throw new BusinessException("指定处理人不能为空");
        }
        UwTaskDetail uwTaskDetail = uwTaskDetailMapper.selectById(taskAssigeeReqDTO.getTaskDetailId());
        if (uwTaskDetail == null) {
            throw new  BusinessException("没有查询到分配处理人的任务");
        }
        HandlerReqDTO handlerReqDTO = new HandlerReqDTO();
        handlerReqDTO.setHandlerDTO(taskAssigeeReqDTO.getHandlerDTO());
        handlerReqDTO.setProcessTaskId(uwTaskDetail.getProcessTaskId());
        flowableService.setAssignee(handlerReqDTO);
    }

    @Override
    public boolean auditTask(AuditTaskReqDTO auditTaskReqDTO) {
        if (auditTaskReqDTO == null) {
            throw new BusinessException("审核对象不能为空");
        }
        UwTaskDetail uwTaskDetail = uwTaskDetailMapper.selectById(auditTaskReqDTO.getTaskDetailId());
        if (uwTaskDetail == null) {
            throw new  BusinessException("没有查询到分配处理人的任务");
        }
        CompleteTaskReqDTO completeTaskReqDTO = new CompleteTaskReqDTO();
        BeanUtils.copyProperties(auditTaskReqDTO,completeTaskReqDTO);
        completeTaskReqDTO.setTaskProcessId(uwTaskDetail.getProcessTaskId());
        //true  终审  false 未终审  调用工作流
        try {
            boolean flag = flowableService.completeTask(completeTaskReqDTO);
            //todo 测试rabbitmq  按照自己业务来处理
            if (flag) {
                Person person = new Person();
                person.setUserName("测试mq审核任务添加");
                person.setUserPwd("6666666666666");
                person.setCreatedOn(new Date());
                person.setCreatedBy("胡海丰测试");
                try {
//                    for (int i=0;i<30000;i++) {
                        AddPersonMessage addPersonMessage = new AddPersonMessage();
                        addPersonMessage.setPerson(person);
                        addPersonMessage.setBusinessNo(uwTaskDetail.getBusinessNo());
                    MessageSendUtils.send2UwAuditBack(addPersonMessage);
//                    }
                } catch (Exception e) {
                    log.info("rabbitmq处理异常,异常信息为：{}",e);
                    return false;
                }
            }
        } finally {
            if (ProcessThreadLocal.getProcessDispatchDTO() != null) {
                log.info("线程变量中待删除的内容为：{}",ProcessThreadLocal.getProcessDispatchDTO());
                ProcessThreadLocal.removeProcessDispatchDTO();
            }
        }
        return true;
    }
}
