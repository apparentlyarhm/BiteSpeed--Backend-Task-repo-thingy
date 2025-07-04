package com.bitespeed.coolname.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentifyRequest {

    private String email;

    private String phoneNumber;
}
