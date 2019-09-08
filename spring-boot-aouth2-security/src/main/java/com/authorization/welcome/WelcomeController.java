/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.authorization.welcome;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *  @author Md. Zakir Hossain
 */

@RestController
public class WelcomeController {
    
    private final String WELCOME_MESSAGE = "Welcome to authorization api! If you see this welcome message, be sure that authorization"
                + " api is running successfully.";
    
    @RequestMapping("/")
    public String welcome() {
        return this.WELCOME_MESSAGE;
    }
    
}
