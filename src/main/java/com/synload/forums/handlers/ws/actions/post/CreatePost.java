package com.synload.forums.handlers.ws.actions.post;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.post.PostCreated;
import com.synload.forums.models.Post;
import com.synload.forums.models.Thread;
import com.synload.forums.models.User;
import com.synload.forums.utils.access.SessionValidation;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class CreatePost {
    @WSEvent(method = "create", action = "post", description = "Create a new post", enabled = true, name = "Create Post")
    public void createPost(RequestEvent e){
        User user;
        if((user = SessionValidation.validate(e.getSession()))!=null){
            if(
                e.getRequest().getData().containsKey("thread") &&
                e.getRequest().getData().containsKey("subject") &&
                e.getRequest().getData().containsKey("body")
            ){
                String thread = e.getRequest().getData().get("thread");
                String subject = e.getRequest().getData().get("subject");
                String body = e.getRequest().getData().get("body");
                try {
                    List<Thread> threads = Thread._find(Thread.class, "id=?", thread).exec(Thread.class);
                    if(threads.size()==1) {
                        Thread threadObj = threads.get(0);

                        // Permission check here!

                        Post post = new Post();
                        post.setBody(body);
                        post.setSubject(subject);
                        post.setCreated(new Date().getTime());
                        post._insert();
                        post._set(threadObj);
                        post._set(user);
                        e.getResponse().send(new Success(104, "Post created"));
                        EventPublisher.raiseEvent( new PostCreated(post), true, null);
                    }else{
                        e.getResponse().send(new Error(112, "Thread not found"));
                    }

                }catch (Exception x){
                    x.printStackTrace();
                }
            }else{
                e.getResponse().send(new Error(102, "Please fill out the form"));
            }
        }else{
            e.getResponse().send(new Error(111, "Not logged in"));
        }
    }
}
