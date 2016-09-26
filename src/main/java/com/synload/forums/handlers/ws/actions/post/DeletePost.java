package com.synload.forums.handlers.ws.actions.post;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.post.PostDeleted;
import com.synload.forums.models.Post;
import com.synload.forums.models.Thread;
import com.synload.forums.models.User;
import com.synload.forums.utils.access.SessionValidation;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.List;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class DeletePost {
    @WSEvent(method = "delete", action = "post", description = "Delete a post", enabled = true, name = "Delete Post")
    public void deletePost(RequestEvent e){
        User user;
        if((user = SessionValidation.validate(e.getSession()))!=null){
            if(
                e.getRequest().getData().containsKey("post")
            ){
                String post = e.getRequest().getData().get("post");
                try {
                    List<Post> posts = Post._find(Post.class, "id=?", post).exec(Post.class);
                    if(posts.size()==1) {
                        Post postObj = posts.get(0);

                        // Permission check here!

                        deletePost(postObj); // delete post
                        e.getResponse().send(new Success(105, "Post deleted"));
                        EventPublisher.raiseEvent( new PostDeleted(postObj), true, null);
                    }else{
                        e.getResponse().send(new Error(113, "Post not found"));
                    }
                }catch (Exception x){
                    x.printStackTrace();
                }
            }else{
                e.getResponse().send(new Error(114, "Request invalid"));
            }
        }else{
            e.getResponse().send(new Error(111, "Not logged in"));
        }
    }
    public static void deletePost(Post postObj) throws Exception{
        List<User> users = postObj._related(User.class).exec(User.class);
        if(users.size()==1) {
            User user = users.get(0);
            postObj._unset(user);
        }
        List<Thread> threads = postObj._related(Thread.class).exec(Thread.class);
        if(threads.size()==1) {
            Thread thread = threads.get(0);
            postObj._unset(thread);
        }
        postObj._delete();
    }
}
