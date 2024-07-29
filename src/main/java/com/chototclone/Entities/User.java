package com.chototclone.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    @Length(max = 50)
    private String userName;

    @Column(name = "password")
    @Length(max = 255)
    private String password;

    @Column(name = "email", unique = true)
    @Length(max = 100)
    private String email;

    @Column(name = "phone")
    @Length(max = 20)
    private String phone;

    @Column(name = "address")
    @Length(max = 255)
    private String address;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @Column(name = "admin_flg")
    private int adminFlag = 0;

    @Column(name = "user_flg")
    private int userFlag = 0;

    @Column(name = "customer_flg")
    private int customerFlag = 0;

    @OneToMany(mappedBy = "sender")
    private Set<Message> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private Set<Message> receivedMessages;

    @OneToMany(mappedBy = "user")
    private Set<Favorite> favorites;
}
