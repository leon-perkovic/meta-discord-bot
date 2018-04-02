package com.meta.leon.discordbot.command;

/**
 * Response form - can contain any object
 *
 * Created by Leon on 18/03/2018
 */
public class ResponseForm<R>{

    private R response;


    public ResponseForm(){
        // default constructor
    }

    public ResponseForm(R response){
        this.response = response;
    }

    // -- getters and setters -------------------------------------------------

    public R getResponse(){
        return response;
    }

    public void setData(R response){
        this.response = response;
    }

}
