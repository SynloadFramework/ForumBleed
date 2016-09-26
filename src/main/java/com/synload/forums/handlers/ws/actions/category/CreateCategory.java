package com.synload.forums.handlers.ws.actions.category;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.thread.ThreadCreated;
import com.synload.forums.models.Category;
import com.synload.forums.models.Post;
import com.synload.forums.models.Thread;
import com.synload.forums.models.User;
import com.synload.forums.utils.access.SessionValidation;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Nathaniel on 9/26/2016.
 */
public class CreateCategory {
    @WSEvent(method = "create", action = "category", description = "Create a new category", enabled = true, name = "Create Category")
    public void createCategory(RequestEvent e){
        User user;
        if((user = SessionValidation.validate(e.getSession()))!=null){
            if(
                e.getRequest().getData().containsKey("category") &&
                e.getRequest().getData().containsKey("title") &&
                e.getRequest().getData().containsKey("description")
            ){
                String category = e.getRequest().getData().get("category");
                String title = e.getRequest().getData().get("title");
                String body = e.getRequest().getData().get("body");
                String description = e.getRequest().getData().get("description");
                try {
                    List<Category> categories = Category._find(Category.class, "id=?", category).exec(Category.class);
                    if(categories.size()==1) {
                        Category categoryObj = categories.get(0);

                        // Permission check here!

                        Thread threadObj = new Thread();
                        threadObj.setCreated(new Date().getTime());
                        threadObj.setDescription(description);
                        threadObj.setTitle(title);
                        threadObj.setUri(title.toLowerCase().replaceAll("[^\\p{L}\\p{Z}]","").replace(" ","_"));
                        threadObj._insert();
                        threadObj._set(categoryObj);
                        threadObj._set(user);

                        Post postObj = new Post();
                        postObj.setBody(body);
                        postObj.setSubject(title);
                        postObj.setCreated(new Date().getTime());
                        postObj._insert();
                        postObj._set(threadObj);
                        postObj._set(user);
                        threadObj._set(postObj);

                        e.getResponse().send(new Success(106, "Thread created"));
                        EventPublisher.raiseEvent( new ThreadCreated(threadObj), true, null);
                    }else{
                        e.getResponse().send(new Error(112, "Category not found"));
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
