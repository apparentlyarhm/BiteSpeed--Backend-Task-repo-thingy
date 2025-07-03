package com.bitespeed.coolname.entity;

import com.bitespeed.coolname.entity.base.Base;
import com.bitespeed.coolname.model.enums.LinkPrecedence;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "contact")
public class Contact extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "contact_id", unique = true, nullable = false, updatable = false)
    private String contactId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "linked_id")
    private String linkedId;

    @Enumerated(EnumType.STRING)
    @Column(name = "link_precedence", nullable = false)
    private LinkPrecedence linkPrecedence;

    @PrePersist
    public void onInsert(){
        this.contactId = UUID.randomUUID().toString();
    }
}
