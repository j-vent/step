
package com.google.sps.data;
public class Status {
    private final boolean isLoggedIn;
    private final String url;
    public Status(boolean isLoggedIn, String url){
       this.isLoggedIn = isLoggedIn;
       this.url = url;
    }
}