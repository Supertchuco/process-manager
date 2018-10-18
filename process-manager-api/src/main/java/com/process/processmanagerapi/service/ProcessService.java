package com.process.processmanagerapi.service;

import com.process.processmanagerapi.entity.Process;
import com.process.processmanagerapi.entity.ProcessOpinion;
import com.process.processmanagerapi.entity.User;
import com.process.processmanagerapi.exception.ProcessAlreadyExistException;
import com.process.processmanagerapi.exception.ProcessAlreadyFinishedDuringIncludeProcessOpinionException;
import com.process.processmanagerapi.exception.ProcessNotFoundException;
import com.process.processmanagerapi.exception.UserNotAuthorizedException;
import com.process.processmanagerapi.repository.ProcessRepository;
import com.process.processmanagerapi.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        validateCreationProcess(createProcessVO.getProcessNumber());
        Process process = new Process(createProcessVO.getProcessNumber(), createProcessVO.getProcessDescription(),
                new Date(), createProcessVO.getCreateBy());
        process = processRepository.save(process);
        log.info("process saved");
        return process;
    }

    public Process includeProcessOpinion(final ProcessOpinionVO processOpinionVO) {
        log.info("Add process opinion to process with number {}", processOpinionVO.getProcessNumber());
        User user = userService.findUserByUserName(processOpinionVO.getUserName());
        userService.verifyIfUserIsNull(user);
        Process process = findProcessByProcessNumber(processOpinionVO.getProcessNumber());
        validateProcessBeforeIncludeOpinion(process, processOpinionVO.getProcessNumber());
        if (!userService.verifyIfUserIsUserOpinion(user, process.getOpinionUsers()) && user.getUserType().getUserTypeName().equals(UserService.FINISHER_USER)) {
            log.error("User {} is not authorized to add process opinion");
            throw new UserNotAuthorizedException("User is not authorized to add process opinion");

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
        Process process = findProcessByProcessNumber(finishProcessVO.getProcessNumber());
        validateProcess(process, finishProcessVO.getProcessNumber());
        process.setFinishDate(new Date());
        process.setFinishBy(finishProcessVO.getFinishBy());
        process = processRepository.save(process);
        log.info("process finished");
        return process;
    }

    public Process findProcessByProcessNumber(final ViewProcessByProcessNumberVO viewProcessByProcessNumberVO){
        log.info("Find process by process number");
        User user = userService.findUserByUserName(viewProcessByProcessNumberVO.getViewBy());
        userService.validateUser(user, UserService.TRIADOR_USER);
        return findProcessByProcessNumber(viewProcessByProcessNumberVO.getProcessNumber());
    }

    public List<Process> findAllProcess(final ViewAllProcessVO viewAllProcessVO){
        log.info("Find process by process number");
        User user = userService.findUserByUserName(viewAllProcessVO.getViewBy());
        userService.validateUser(user, UserService.TRIADOR_USER);
        return  processRepository.findAll();
    }

    private Process findProcessByProcessNumber(final int processNumber){
        return processRepository.findByProcessNumber(processNumber);
    }

    private void validateProcess(final Process process, final int processNumber) {
        log.info("validate process with number {}", processNumber);
        if (Objects.isNull(process)) {
            log.error("Process with number {} not found", processNumber);
            throw new ProcessNotFoundException("Process not found");
        }
    }

    private void validateProcessBeforeIncludeOpinion(final Process process, final int processNumber) {
        log.info("validate process  number {} to include opinion", processNumber);
        validateProcess(process, processNumber);
        if(Objects.isNull(process.getFinishDate())){
            log.error("Process with number {} already finalized", processNumber);
            throw new ProcessAlreadyFinishedDuringIncludeProcessOpinionException("Process not found");
        }
    }

    private void validateCreationProcess(final int processNumber) {
        log.info("validate creation process with number {}", processNumber);
        Process process = processRepository.findByProcessNumber(processNumber);
        if (!Objects.isNull(process)) {
            log.error("Process already exist with number {}", processNumber);
            throw new ProcessAlreadyExistException("Process already exist");
        }
    }

}