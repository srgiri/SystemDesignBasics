package com.sam.controller;

import com.sam.dto.Employee;
import com.sam.dto.User;
import com.sam.message.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sam")
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

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

    @PostMapping("/publish/obj")
    public ResponseEntity<String> sendObjectMessage(@RequestBody Employee employee) {
        jsonProducer.sendObjectMessage(employee);
        return ResponseEntity.ok("Object message sent to RabbitMQ ...");
    }

    @PostMapping("/github-webhook")
    public ResponseEntity<String> githubWebhook() {
        LOGGER.info("Received GitHub webhook API call");
        return ResponseEntity.ok().build();
    }
}
