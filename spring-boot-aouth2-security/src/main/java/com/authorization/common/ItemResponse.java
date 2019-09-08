/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.authorization.common;

/**
 *
 *  @author Md. Zakir Hossain
 */
public class ItemResponse<T> extends BaseResponse{
    
    private T item;

    public ItemResponse() {
    }
    
    public ItemResponse(T item) {
        super();
        this.item = item;
    }

    
    
    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
    
    
    
}
