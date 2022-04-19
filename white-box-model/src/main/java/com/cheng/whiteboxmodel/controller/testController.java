package com.cheng.whiteboxmodel.controller;

import com.cheng.whiteboxmodel.utils.AuthToken;
import com.cheng.whiteboxmodel.utils.RestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @RequestMapping(value = "/test/token-test", method = RequestMethod.GET)
    @AuthToken
    public RestResponse tokenTest() {
        return RestResponse.OK;
    }

}
