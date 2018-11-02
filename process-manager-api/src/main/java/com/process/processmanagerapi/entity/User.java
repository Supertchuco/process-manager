package com.process.processmanagerapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "ProcessUser")
@Table(name = "ProcessUser")
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private int userId;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private Date createDate;

    @Column
    private String createBy;

    @OneToOne
    @JoinColumn(name = "userTypeId")
    private UserType userType;

    public User(final String name, final String password, final Date createDate, final String createBy) {
        this.name = name;
        this.password = password;
        this.createDate = createDate;
        this.createBy = createBy;
    }
}
