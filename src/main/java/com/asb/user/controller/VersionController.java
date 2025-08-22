package com.asb.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class VersionController {

    @Value("${app.version}")
    private String appVersion;
    @GetMapping("/version")
    public ResponseEntity<String> get() {
        return new ResponseEntity<>(appVersion, HttpStatus.OK);
    }

}
