package com.synload.forums.events.post;

import com.synload.eventsystem.EventClass;
import com.synload.eventsystem.Handler;
import com.synload.eventsystem.Type;
import com.synload.forums.models.Post;
import com.synload.forums.models.User;

import java.util.List;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class PostEdited extends EventClass {
    public Post p;
    public User u;
    public PostEdited(Post p){
        this.p = p;
        try { // get user
            List<User> users = p._related(User.class).exec(User.class);
            if (users.size() == 1) {
                u = users.get(0);
            }
        }catch (Exception e){}
        this.setHandler(Handler.EVENT);
        this.setType(Type.OTHER);
    }

    public Post getPost() {
        return p;
    }

    public User getUser() {
        return u;
    }
}
