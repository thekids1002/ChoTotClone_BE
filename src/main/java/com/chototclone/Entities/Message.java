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
@Table(name = "messages")
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "listing_id", referencedColumnName = "listing_id")
    private Listing listing;

    @Column(name = "message_content", columnDefinition = "TEXT", nullable = false)
    private String messageContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", columnDefinition = "ENUM('Unread', 'Read')")
    private ReadStatus readStatus;

    public enum ReadStatus {
        UNREAD, READ
    }
}