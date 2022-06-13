package com.example.demo_resorce.controller;

import com.example.demo_resorce.enumAuth.Scope;
import com.example.demo_resorce.service.ResourceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping(path = "api/v1/google")
@CrossOrigin(origins = "*")
public class ResourceApi {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public ResponseEntity<?> getImage(
            @RequestHeader(name="Authorization") String token
    ) throws IOException {
        log.info("Scope.IMAGE la " + Scope.image.label);
        if (!resourceService.getScope(token).equals(Scope.image.label)){
            return new ResponseEntity("Bạn không được quyền", HttpStatus.FORBIDDEN);
        }
        if (!resourceService.getResource(token).isPresent()){
            return new ResponseEntity("Token hết hạn",HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(
                    "Thông tin ảnh",
                    HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(
            @RequestHeader(name="Authorization") String token
    ) throws IOException {
        if (!resourceService.getScope(token).equals(Scope.user.label)){
            return new ResponseEntity("Bạn không được quyền",HttpStatus.FORBIDDEN);
        }
        if (!resourceService.getResource(token).isPresent()){
            return new ResponseEntity("Token hết hạn",HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(
                    "Thông người dùng",
                    HttpStatus.OK);
        }
    }
}
