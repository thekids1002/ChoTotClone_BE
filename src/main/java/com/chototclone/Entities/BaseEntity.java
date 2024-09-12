package com.chototclone.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "del_flg")
    private int delFlag;


    @PrePersist
    protected void onCreate() {
//        createdAt = LocalDateTime.now(); Temporary comment, will use later
        if (createdAt == null) {
            createdAt = new Date(System.currentTimeMillis());
            updatedAt = new Date(System.currentTimeMillis());
        }
    }

    @PreUpdate
    protected void onUpdate() {
//        updatedAt = LocalDateTime.now(); Temporary comment, will use later
        updatedAt = new Date(System.currentTimeMillis());
    }
}
