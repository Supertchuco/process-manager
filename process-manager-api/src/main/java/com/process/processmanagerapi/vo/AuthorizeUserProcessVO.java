package com.process.processmanagerapi.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AuthorizeUserProcessVO implements Serializable {

    @NotNull
    private int processNumber;

    @NotNull
    private String userName;

    @NotNull
    private String authorizedBy;
}
