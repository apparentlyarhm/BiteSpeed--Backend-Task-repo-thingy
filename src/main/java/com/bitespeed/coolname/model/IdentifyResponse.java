package com.bitespeed.coolname.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentifyResponse {
    private ContactResponse contact;

    public IdentifyResponse(ContactResponse contact){
        this.contact = contact;
    }
}

