package com.synload.forums.events.thread;

import com.synload.eventsystem.EventClass;
import com.synload.eventsystem.Handler;
import com.synload.eventsystem.Type;
import com.synload.forums.models.Thread;
import com.synload.forums.models.User;

import java.util.List;

/**
 * Created by Nathaniel on 9/26/2016.
 */
public class ThreadDeleted extends EventClass {
    public Thread thread;
    public User u;
    public ThreadDeleted(Thread thread){
        this.thread = thread;
        try { // get user
            List<User> users = thread._related(User.class).exec(User.class);
            if (users.size() == 1) {
                u = users.get(0);
            }
        }catch (Exception e){}
        this.setHandler(Handler.EVENT);
        this.setType(Type.OTHER);
    }

    public Thread getThread() {
        return thread;
    }

    public User getUser() {
        return u;
    }
}