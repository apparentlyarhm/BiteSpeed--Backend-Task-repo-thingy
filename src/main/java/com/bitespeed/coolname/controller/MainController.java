package com.bitespeed.coolname.controller;

import com.bitespeed.coolname.model.IdentifyRequest;
import com.bitespeed.coolname.model.IdentifyResponse;
import com.bitespeed.coolname.service.contract.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private IdentityService identifyService;

    @PostMapping("/identify")
    public IdentifyResponse identify(@RequestBody IdentifyRequest request) {
        return identifyService.identifyContact(request);
    }
}
