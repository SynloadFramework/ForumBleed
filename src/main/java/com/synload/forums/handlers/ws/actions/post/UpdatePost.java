package com.synload.forums.handlers.ws.actions.post;

import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.models.Post;
import com.synload.forums.models.User;
import com.synload.forums.utils.access.SessionValidation;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.List;
import java.util.Date;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class UpdatePost {
    @WSEvent(method = "update", action = "post", description = "Update a post", enabled = true, name = "Update Post")
    public void updatePost(RequestEvent e){
        User user;
        if((user = SessionValidation.validate(e.getSession()))!=null){
            if(
                e.getRequest().getData().containsKey("post") &&
                e.getRequest().getData().containsKey("subject") &&
                e.getRequest().getData().containsKey("body")
            ){
                String post = e.getRequest().getData().get("post");
                String subject = e.getRequest().getData().get("subject");
                String body = e.getRequest().getData().get("body");
                if(body.equals("") || subject.equals("")){
                    e.getResponse().send(new Error(102, "Please fill out the form"));
                    return;
                }
                try {
                    List<Post> posts = Post._find(Post.class, "id=?", post).exec(Post.class);
                    if(posts.size()==1){
                        Post postObj = posts.get(0);

                        // Permission check here!

                        boolean updated = false;
                        if(postObj.getBody().equals(body)){
                            postObj.setBody(body);
                            postObj._save("body", body);
                            updated=true;
                        }
                        if(postObj.getSubject().equals(subject)){
                            postObj.setSubject(subject);
                            postObj._save("subject", subject);
                            updated=true;
                        }
                        if(updated){
                            postObj.setUpdated(new Date().getTime());
                            postObj._save("updated", postObj.getUpdated());
                        }
                    }else{
                        e.getResponse().send(new Error(113, "Post not found"));
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
