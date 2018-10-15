package com.process.processmanagerapi.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateProcessVO implements Serializable {

    private String processNumber;

    private String processDescription;

    private String createBy;
}
