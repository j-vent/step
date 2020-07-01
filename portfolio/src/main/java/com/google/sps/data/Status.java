
package com.google.sps.data;
public class Status{
    private final boolean isLoggedIn;
    private final String loginUrl;
    public Status(boolean isLoggedIn, String loginUrl){
       this.isLoggedIn = isLoggedIn;
       this.loginUrl = loginUrl;
    }
}