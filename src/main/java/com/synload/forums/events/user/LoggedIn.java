package com.synload.forums.events.user;

import com.synload.eventsystem.EventClass;
import com.synload.forums.models.User;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class LoggedIn extends EventClass {
    public User user;
    public LoggedIn(User u){
        this.user = u;
    }

    public User getUser() {
        return user;
    }
}
