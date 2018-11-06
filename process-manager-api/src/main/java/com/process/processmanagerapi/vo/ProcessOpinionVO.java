package com.process.processmanagerapi.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ProcessOpinionVO implements Serializable {

    @NotNull
    private int processNumber;

    @NotNull
    private String processOpinion;

    @NotNull
    private String userName;

}
