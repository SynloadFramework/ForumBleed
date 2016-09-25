package com.synload.forums.handlers.ws.actions.access;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.user.LoggedOut;
import com.synload.forums.events.user.Registered;
import com.synload.forums.models.Session;
import com.synload.forums.models.User;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class Registration {

    // from https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @WSEvent(method = "process", action = "register", description = "Register a new user", enabled = true, name = "Process Register")
    public void processRegister(RequestEvent e){
        if(!e.getSession().getSessionData().containsKey("session")){
            if(
                e.getRequest().getData().containsKey("user") &&
                e.getRequest().getData().containsKey("pass") &&
                e.getRequest().getData().containsKey("email")
            ){
                String user = e.getRequest().getData().get("user");
                String pass = e.getRequest().getData().get("pass");
                String email = e.getRequest().getData().get("email");
                if(user.equals("") && user.length()>3){
                    e.getResponse().send(new Error(110, "User not valid!"));
                    return;
                }
                if(pass.equals("") && user.length()>5){
                    e.getResponse().send(new Error(109, "Password not valid!"));
                    return;
                }
                if(email.equals("") || !email.matches(EMAIL_PATTERN)){
                    e.getResponse().send(new Error(108, "Email not valid!"));
                    return;
                }
                try {
                    if (!User._exists(User.class, "name=?", user)) {
                        User u = new User();
                        u.setEmail(email);
                        u.setPassword(User.hash(pass));
                        u.setName(user);
                        u.setCreated(new Date().getTime());
                        u._insert();
                        EventPublisher.raiseEvent( new Registered(u), true, null);
                        e.getResponse().send(new Success(103, "Account completed registration"));
                    } else {
                        e.getResponse().send(new Error(107, "Account already exists"));
                    }
                }catch (Exception x){
                    x.printStackTrace();
                }
            }
        }else{
            e.getResponse().send(new Error(103, "Already logged in"));
        }
    }

    @WSEvent(method = "check", action = "user_exists", description = "Check to see if a user account exists", enabled = true, name = "Check User Exists")
    public void checkUserExists(RequestEvent e){
        if(
            e.getRequest().getData().containsKey("user")
        ){
            String user = e.getRequest().getData().get("user");
            if(user.equals("") && user.length()>3){
                e.getResponse().send(new Error(110, "User not valid!"));
                return;
            }
            try {
                if (!User._exists(User.class, "name=?", user)) {
                    e.getResponse().send(new Success(104, "Account not found!"));
                } else {
                    e.getResponse().send(new Error(107, "Account already exists"));
                }
            }catch (Exception x){
                x.printStackTrace();
            }
        }else{
            e.getResponse().send(new Error(103, "Already logged in"));
        }
    }
}
