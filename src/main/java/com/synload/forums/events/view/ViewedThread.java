package com.synload.forums.events.view;

import com.synload.eventsystem.EventClass;
import com.synload.eventsystem.Handler;
import com.synload.eventsystem.Type;
import com.synload.forums.models.User;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class ViewedThread extends EventClass {
    public Thread thread;
    public User u;
    public ViewedThread(Thread thread, User u){
        this.thread = thread;
        this.u = u;
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
