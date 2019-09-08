/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.authorization.user.repository;

import com.authorization.user.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 *  @author Md. Zakir Hossain
 */
public interface UsersRepository extends JpaRepository<Users, Long> {
    
    public Users findByUsername(String username);
    
}
