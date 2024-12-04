package org.pawpal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PawpalApplication{

    public static void main(String[] args)  {
        SpringApplication.run(PawpalApplication.class, args);
    }

    @Secured("ROLE_USER")
    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        System.out.println("aici");
        return ResponseEntity.ok("hellou");
    }
}
