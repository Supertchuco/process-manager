package com.process.processmanagerapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "ProcessOpinion")
@Table(name = "ProcessOpinion")
@AllArgsConstructor
@NoArgsConstructor
public class ProcessOpinion implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private int processOpinionId;

    @Column
    private String processOpinion;

    @Column
    private Date createDate;

    @Column
    private String createBy;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "processNumber", nullable = false)
    private Process process;

    public ProcessOpinion(final String processOpinion, final Date createDate, final String createBy,
                          final Process process) {
        this.processOpinion = processOpinion;
        this.createDate = createDate;
        this.createBy = createBy;
        this.process = process;
    }
}
