package com.bitespeed.coolname.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentifyRequest {

    @NotNull
    private String email;

    @NotNull
    private String phoneNumber;
}
