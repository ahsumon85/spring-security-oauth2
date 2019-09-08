/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.authorization.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 *  @author Md. Zakir Hossain
 */
@Component
public class UserInfoUtils {
    
    private static ApplicationContext context;

    @Autowired
    public UserInfoUtils(ApplicationContext ac) {
        context = ac;
    }

    public static ApplicationContext getContext() {
        return context;
    }
    
    
     public static String getHashPassword(String password) {
        PasswordEncoder userPasswordEncoder = context.getBean("userPasswordEncoder", PasswordEncoder.class);
        return userPasswordEncoder.encode(password);
    }
    
    public static boolean isPreviousPasswordCorrect(String previousPlainPassword,String previousEncriptedPassword) {
        PasswordEncoder userPasswordEncoder = context.getBean("userPasswordEncoder", PasswordEncoder.class);
       return userPasswordEncoder.matches(previousPlainPassword, previousEncriptedPassword);
    }
    
    
}
