package com.moonyboyvim.runnerz.foo.bar;

import org.springframework.stereotype.Component;

@Component
public class WelcomeMessage {
    // @Component mean that this class available to Spring application
    public String getWelcomeMessage() {
        return "Welcome to the Spring Boot Application!!!";
    }
}
