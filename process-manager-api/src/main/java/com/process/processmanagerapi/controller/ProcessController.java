package com.process.processmanagerapi.controller;

import com.process.processmanagerapi.entity.Process;
import com.process.processmanagerapi.service.ProcessService;
import com.process.processmanagerapi.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    ProcessService processService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Process createProcess(@RequestBody CreateProcessVO createProcessVO) {
        return processService.createProcess(createProcessVO);
    }

    @RequestMapping(value = "/finalize", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Process finishProcess(@RequestBody FinishProcessVO finishProcessVO) {
        return processService.finishProcess(finishProcessVO);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Process> findAllProcess(@RequestBody ViewAllProcessVO viewAllProcessVO) {
        return processService.findAllProcess(viewAllProcessVO);
    }

    @RequestMapping(value = "/findByProcessNumber", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Process findProcessByProcessNumber(@RequestBody ViewProcessByProcessNumberVO viewProcessByProcessNumberVO) {
        return processService.findProcessByProcessNumber(viewProcessByProcessNumberVO);
    }

    @RequestMapping(value = "/includeProcessOpinion", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Process includeProcessOpinion(@RequestBody ProcessOpinionVO processOpinionVO) {
        return processService.includeProcessOpinion(processOpinionVO);
    }

}
