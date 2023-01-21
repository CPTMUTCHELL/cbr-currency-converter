package com.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String value;
    @Column(name = "expiry_date")
    private Date expiryDate;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;


}
