package com.process.processmanagerapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "User")
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private int userId;

    @Column
    private String userName;

    @Column
    private String password;

    @Column
    private Date createDate;

    @Column
    private String createBy;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserType userType;

    public User(final String userName, final String password, final Date createDate, final String createBy){
        this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createBy = createBy;
    }
}
