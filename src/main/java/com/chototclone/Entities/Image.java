package com.chototclone.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "listing_id", referencedColumnName = "listing_id")
    private Listing listing;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;
}