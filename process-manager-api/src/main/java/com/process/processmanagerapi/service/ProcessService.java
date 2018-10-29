package com.process.processmanagerapi.service;

import com.process.processmanagerapi.entity.Process;
import com.process.processmanagerapi.entity.ProcessOpinion;
import com.process.processmanagerapi.entity.User;
import com.process.processmanagerapi.exception.*;
import com.process.processmanagerapi.repository.ProcessRepository;
import com.process.processmanagerapi.vo.*;
import lombok.extern.slf4j.Slf4j;
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
        try {
            process = processRepository.save(process);

        } catch (Exception e) {
            log.error("Error during save process", e);
            throw new ProcessSaveException(e.getMessage());
        }
        log.info("process saved");
        return process;
    }

    public Process includeProcessOpinion(final ProcessOpinionVO processOpinionVO) {
        log.info("Add process opinion to process with number {}", processOpinionVO.getProcessNumber());
        User user = userService.findUserByUserName(processOpinionVO.getUserName());
        userService.validateUser(user, UserService.FINISHER_USER);
        Process process = findProcessByProcessNumber(processOpinionVO.getProcessNumber());
        validateProcessBeforeIncludeOpinion(process);
        if (!userService.isUserAuthorizedToIncludeProcessOpinion(user, process.getOpinionUsers())) {
            log.error("User {} is not authorized to add process opinion");
            throw new UserNotAuthorizedToIncludeProcessOpnionException("User is not authorized to add process opinion");

        }
        if (Objects.isNull(process.getProcessOpinion())) {
            process.setProcessOpinion(new ArrayList<>());
        }
        process.getProcessOpinion().add(new ProcessOpinion(processOpinionVO.getProcessOpinion(), new Date(),
                processOpinionVO.getUserName(), process));
        try {
            process = processRepository.save(process);

        } catch (Exception e) {
            log.error("Error during save process", e);
            throw new ProcessSaveException(e.getMessage());
        }
        log.info("process opinion added");
        return process;
    }

    public Process finishProcess(final FinishProcessVO finishProcessVO) {
        log.info("Finish process with number {}", finishProcessVO.getProcessNumber());
        User user = userService.findUserByUserName(finishProcessVO.getFinishBy());
        userService.validateUser(user, UserService.FINISHER_USER);
        Process process = findProcessByProcessNumber(finishProcessVO.getProcessNumber());
        validateProcessBeforeFinishProcess(process);
        process.setFinishDate(new Date());
        process.setFinishBy(finishProcessVO.getFinishBy());
        try {
            process = processRepository.save(process);

        } catch (Exception e) {
            log.error("Error during save process", e);
            throw new ProcessSaveException(e.getMessage());
        }
        log.info("process finished");
        return process;
    }

    public Process getProcessByProcessNumber(final ViewProcessByProcessNumberVO viewProcessByProcessNumberVO) {
        log.info("Find process by process number");
        User user = userService.findUserByUserName(viewProcessByProcessNumberVO.getViewBy());
        userService.validateUser(user, UserService.TRIADOR_USER);
        return findProcessByProcessNumber(viewProcessByProcessNumberVO.getProcessNumber());
    }

    public List<Process> findAllProcess(final ViewAllProcessVO viewAllProcessVO) {
        log.info("Find process by process number");
        User user = userService.findUserByUserName(viewAllProcessVO.getViewBy());
        userService.validateUser(user, UserService.TRIADOR_USER);
        return processRepository.findAll();
    }

    private Process findProcessByProcessNumber(final int processNumber) {
        return processRepository.findByProcessNumber(processNumber);
    }

    private void validateProcess(final Process process) {
        log.info("validate process");
        if (Objects.isNull(process)) {
            log.error("Process not found");
            throw new ProcessNotFoundException("Process not found");
        }
    }

    private void validateProcessBeforeFinishProcess(final Process process) {
        log.info("validate process to finish");
        validateProcess(process);
        if (!Objects.isNull(process.getFinishDate())) {
            log.error("Process with number {} already finalized", process.getProcessNumber());
            throw new ProcessAlreadyFinishedDuringFinishProcessException("Process already finalized");
        }
    }

    private void validateProcessBeforeIncludeOpinion(final Process process) {
        log.info("validate process to include opinion");
        validateProcess(process);
        if (!Objects.isNull(process.getFinishDate())) {
            log.error("Process with number {} already finalized", process.getProcessNumber());
            throw new ProcessAlreadyFinishedDuringIncludeProcessOpinionException("Process already finalized");
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