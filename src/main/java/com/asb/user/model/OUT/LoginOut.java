/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asb.user.model.OUT;

import com.asb.user.model.dto.UserDto;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author manuelm
 */
@Data
@Builder
public class LoginOut extends GenericResponseOut {

    private UserDto data;

    public LoginOut() {
    }

    public LoginOut(UserDto data) {
        this.data = data;
    }

    public UserDto getData() {
        return data;
    }

    public void setData(UserDto data) {
        this.data = data;
    }   
    
}
