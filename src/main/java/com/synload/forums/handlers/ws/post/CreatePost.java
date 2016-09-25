package com.synload.forums.handlers.ws.post;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.user.LoggedIn;
import com.synload.forums.models.Session;
import com.synload.forums.models.User;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.List;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class CreatePost {
    @WSEvent(method = "process", action = "resume_session", description = "Process and log the user in", enabled = true, name = "Process Resume Session")
    public void resumeSession(RequestEvent e){
        if(
            e.getSession().getSessionData().containsKey("session")
        ){
            if(
                e.getRequest().getData().containsKey("key")
            ){
                String key = e.getRequest().getData().get("key");
                String ip = e.getSession().session.getRemoteAddress().getAddress().getHostAddress();
                try {
                    List<Session> sessions = Session._find(Session.class, "key=? and ip=?", key, ip).exec(Session.class);
                    if(sessions.size()==1){
                        Session session = sessions.get(0);
                        List<User> users = session._related(User.class).exec(User.class);
                        if(users.size()==1) {
                            User userObj = users.get(0);
                            e.getSession().getSessionData().put("session", session);
                            Success s = new Success(101, "You are now logged in!");
                            s.getData().put("key", session.getKey());
                            EventPublisher.raiseEvent(new LoggedIn(userObj), true, null); // raise logged in event
                            e.getResponse().send(s);
                        }else{
                            e.getResponse().send(new Error(105, "User account not found on this session!"));
                        }
                    }else{
                        e.getResponse().send(new Error(106, "Session not found!"));

                    }
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }else{
                e.getResponse().send(new Error(102, "Please fill out the form"));
            }
        }else{
            e.getResponse().send(new Error(103, "Already Logged In"));
        }
    }
}
