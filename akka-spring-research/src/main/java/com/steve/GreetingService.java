package com.steve;

import org.springframework.stereotype.Component;

/**
 * Created by stevexu on 4/16/17.
 */
@Component
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name;
    }

}
