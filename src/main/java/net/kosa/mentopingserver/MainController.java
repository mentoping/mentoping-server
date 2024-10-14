package net.kosa.mentopingserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Metain!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Server is up and running!";
    }

    @GetMapping("/version")
    public String version() {
        return "Metain version 1.0.0";
    }
}