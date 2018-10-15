package com.process.processmanagerapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "Process")
@Table(name = "Process")
@AllArgsConstructor
@NoArgsConstructor
public class Process implements Serializable {

    @Id
    @Column
    private int processNumber;

    @Column
    private String processDescription;

    @Column
    private Date finishDate;

    @Column
    private String finishBy;

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProcessOpinion> processOpinion;

}
