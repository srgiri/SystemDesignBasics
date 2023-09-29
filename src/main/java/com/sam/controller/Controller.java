package com.sam.controller;

import com.sam.dto.User;
import com.sam.message.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sam")
public class Controller {
    @Autowired
    private Producer jsonProducer;


    @PostMapping("/publish")
    public ResponseEntity<String> sendJsonMessage(@RequestBody User user,
                                                  @RequestParam(required = false) Integer routingKey) {
        if (routingKey == null) {
            routingKey = 1;
        }
        jsonProducer.sendJsonMessage(user, routingKey);
        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }
}
