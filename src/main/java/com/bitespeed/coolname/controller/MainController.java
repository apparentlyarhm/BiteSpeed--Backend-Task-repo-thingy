package com.bitespeed.coolname.controller;

import com.bitespeed.coolname.exception.BaseException;
import com.bitespeed.coolname.model.IdentifyRequest;
import com.bitespeed.coolname.model.IdentifyResponse;
import com.bitespeed.coolname.service.contract.IdentityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Main Controller", description = "Each API will go here")
public class MainController {

    @Autowired
    private IdentityService identifyService;

    @PostMapping(value = "/identify", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "It does something bro")
    public IdentifyResponse identify(@RequestBody IdentifyRequest request) throws BaseException {
        return null;
    }
}
