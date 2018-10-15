package com.process.processmanagerapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "ProcessOpinion")
@Table(name = "ProcessOpinion")
public class UserType implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private int userTypeId;

    @Column
    private String userTypeName;

    @OneToMany
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

}
