package com.process.processmanagerapi.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProcessOpinionVO implements Serializable {

    private int processNumber;

    private String processOpinion;

    private String userName;
}
