package com.process.processmanagerapi.service;

import com.process.processmanagerapi.entity.Process;
import com.process.processmanagerapi.entity.ProcessOpinion;
import com.process.processmanagerapi.entity.User;
import com.process.processmanagerapi.exception.ProcessNotFoundException;
import com.process.processmanagerapi.exception.UserNotAuthorizedAddProcessOpinionException;
import com.process.processmanagerapi.repository.ProcessRepository;
import com.process.processmanagerapi.vo.CreateProcessVO;
import com.process.processmanagerapi.vo.FinishProcessVO;
import com.process.processmanagerapi.vo.ProcessOpinionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class ProcessService {

    @Autowired
    UserService userService;

    @Autowired
    ProcessRepository processRepository;

    public Process createProcess(final CreateProcessVO createProcessVO) {
        log.info("Create new process with number: {}", createProcessVO.getProcessNumber());
        User user = userService.findUserByUserName(createProcessVO.getCreateBy());
        userService.validateUser(user, UserService.TRIADOR_USER);
        Process process = processRepository.findByProcessNumber(createProcessVO.getProcessNumber());
        validateProcess(process, createProcessVO.getProcessNumber());
        process = new Process(createProcessVO.getProcessNumber(), createProcessVO.getProcessDescription(),
                new Date(), createProcessVO.getCreateBy());
        process = processRepository.save(process);
        log.info("process saved");
        return process;
    }

    public Process addProcessOpinion(final ProcessOpinionVO processOpinionVO) {
        log.info("Add process opinion to process with number {}", processOpinionVO.getProcessNumber());
        User user = userService.findUserByUserName(processOpinionVO.getUserName());
        userService.verifyIfUserIsNull(user);
        Process process = processRepository.findByProcessNumber(processOpinionVO.getProcessNumber());
        validateProcess(process, processOpinionVO.getProcessNumber());
        if (!userService.verifyIfUserIsUserOpinion(user, process.getOpinionUsers())) {
            log.error("User {} is not authorized to add process opinion");
            throw new UserNotAuthorizedAddProcessOpinionException("User is not authorized to add process opinion");

        }
        if (CollectionUtils.isEmpty(process.getProcessOpinion())) {
            process.setProcessOpinion(new ArrayList<>());
        }
        process.getProcessOpinion().add(new ProcessOpinion(processOpinionVO.getProcessOpinion(), new Date(),
                processOpinionVO.getUserName(), process));
        process = processRepository.save(process);
        log.info("process opinion added");
        return process;
    }

    public Process finishProcess(final FinishProcessVO finishProcessVO) {
        log.info("Finish process with number {}", finishProcessVO.getProcessNumber());
        User user = userService.findUserByUserName(finishProcessVO.getFinishBy());
        userService.validateUser(user, UserService.FINISHER_USER);
        Process process = processRepository.findByProcessNumber(finishProcessVO.getProcessNumber());
        validateProcess(process, finishProcessVO.getProcessNumber());
        process.setFinishDate(new Date());
        process.setFinishBy(finishProcessVO.getFinishBy());
        process = processRepository.save(process);
        log.info("process finished");
        return process;
    }


    private void validateProcess(final Process process, final int processNumber) {
        log.info("validate process with number {}", processNumber);
        if (Objects.isNull(process)) {
            log.error("Process with number {} not found", processNumber);
            throw new ProcessNotFoundException("Process not found");
        }
    }

}