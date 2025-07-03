package com.bitespeed.coolname.repository;

import com.bitespeed.coolname.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepo extends JpaRepository<Contact, Long> {

    List<Contact> findByEmailOrPhoneNumberOrderByCreatedAtAsc(String email, String phoneNumber);

    List<Contact> findByContactIdOrLinkedId(String contactId, String linkedId);

}
