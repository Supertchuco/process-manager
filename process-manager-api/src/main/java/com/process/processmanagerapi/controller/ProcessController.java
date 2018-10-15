package com.process.processmanagerapi.controller;

import com.process.processmanagerapi.service.ProcessService;
import com.process.processmanagerapi.vo.CreateProcessVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    ProcessService processService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createProcess(@RequestBody CreateProcessVO createProcessVO) {
        //
        return new ResponseEntity("Access to be granted.", HttpStatus.OK);
    }
}
