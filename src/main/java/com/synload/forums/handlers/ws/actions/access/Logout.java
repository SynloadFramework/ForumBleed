package com.synload.forums.handlers.ws.actions.access;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.user.LoggedOut;
import com.synload.forums.models.Session;
import com.synload.forums.models.User;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.List;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class Logout {
    @WSEvent(method = "process", action = "logout", description = "Process and log the user out", enabled = true, name = "Process Logout")
    public void processLogout(RequestEvent e){
        if(e.getSession().getSessionData().containsKey("session")){
            Session userSession = (Session) e.getSession().getSessionData().get("session");
            try {
                List<User> users = userSession._related(User.class).exec(User.class);
                if(users.size()==1){
                    User user = users.get(0);

                    userSession._unset(user);
                    userSession._delete();

                    EventPublisher.raiseEvent( new LoggedOut(user), true, null); // send event of logout

                    Success s = new Success(102, "You have logged out!");
                    s.getData().put("key", userSession.getKey());
                    e.getResponse().send(s);

                }else{
                    userSession._delete();
                    e.getResponse().send(new Error(105, "User account not found on this session!"));
                }
            }catch (Exception x){
                x.printStackTrace();
            }
        }else{
            e.getResponse().send(new Error(106, "Session not found!"));
        }
    }
}
