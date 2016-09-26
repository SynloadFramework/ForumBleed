package com.synload.forums.handlers.ws.actions.thread;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.thread.ThreadDeleted;
import com.synload.forums.handlers.ws.actions.post.DeletePost;
import com.synload.forums.models.Category;
import com.synload.forums.models.Post;
import com.synload.forums.models.Thread;
import com.synload.forums.models.User;
import com.synload.forums.utils.access.SessionValidation;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.List;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class DeleteThread {
    @WSEvent(method = "delete", action = "thread", description = "Delete a new thread", enabled = true, name = "Delete Thread")
    public void deleteThread(RequestEvent e){
        User user;
        if((user = SessionValidation.validate(e.getSession()))!=null){
            if(
                e.getRequest().getData().containsKey("thread")
            ){
                String thread = e.getRequest().getData().get("thread");
                try {
                    List<Thread> threads = Thread._find(Thread.class, "id=?", thread).exec(Thread.class);
                    if(threads.size()==1) {
                        Thread threadObj = threads.get(0);

                        // Permission check here!

                        EventPublisher.raiseEvent( new ThreadDeleted(threadObj), true, null);
                        deleteThread(threadObj, user); // now delete thread
                        e.getResponse().send(new Success(107, "Thread deleted"));

                    }else{
                        e.getResponse().send(new Error(112, "Thread not found"));
                    }

                }catch (Exception x){
                    x.printStackTrace();
                }
            }else{
                e.getResponse().send(new Error(114, "Not valid request"));
            }
        }else{
            e.getResponse().send(new Error(111, "Not logged in"));
        }
    }
    public static void deleteThread(Thread threadObj, User user) throws Exception{
        threadObj._unset(user);
        List<Category> categories = threadObj._related(Category.class).exec(Category.class);
        if(categories.size()==1){
            threadObj._unset(categories.get(0));
        }

        List<Post> posts = threadObj._related(Post.class).exec(Post.class);
        for(Post post: posts){
            DeletePost.deletePost(post);
        }

        threadObj._delete();
    }
}
