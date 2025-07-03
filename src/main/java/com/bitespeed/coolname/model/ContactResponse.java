package com.bitespeed.coolname.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ContactResponse {

    private String primaryContactId;
    private List<String> emails;
    private List<String> phoneNumbers;
    private List<String> secondaryContactIds;
}
