package com.chototclone.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "brand")
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    @Id
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

}
