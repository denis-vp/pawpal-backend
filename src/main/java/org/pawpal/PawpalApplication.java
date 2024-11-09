package org.pawpal;

/*import org.pawpal.controller.PetController;*/
import org.pawpal.controller.PetController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PawpalApplication{

    public static void main(String[] args)  {
        SpringApplication.run(PawpalApplication.class, args);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        System.out.println("aici");
        return ResponseEntity.ok("hellou");
    }
}
