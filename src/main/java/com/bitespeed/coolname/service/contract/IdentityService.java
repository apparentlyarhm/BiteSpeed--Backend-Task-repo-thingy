package com.bitespeed.coolname.service.contract;

import com.bitespeed.coolname.model.IdentifyRequest;
import com.bitespeed.coolname.model.IdentifyResponse;

public interface IdentityService {

    public IdentifyResponse identifyContact(IdentifyRequest request);

}
