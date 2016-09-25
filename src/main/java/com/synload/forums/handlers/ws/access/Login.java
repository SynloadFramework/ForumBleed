package com.synload.forums.handlers.ws.access;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.user.LoggedIn;
import com.synload.forums.models.Session;
import com.synload.forums.models.User;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class Login {
    @WSEvent(method = "process", action = "login", description = "Process and log the user in", enabled = true, name = "Process Login")
    public void processLogin(RequestEvent e){
        if(
            !e.getSession().getSessionData().containsKey("session")
        ){
            if(
                e.getRequest().getData().containsKey("user") &&
                e.getRequest().getData().containsKey("pass")
            ){
                String user = e.getRequest().getData().get("user");
                String pass = e.getRequest().getData().get("pass");
                String ip = e.getSession().session.getRemoteAddress().getAddress().getHostAddress();
                try {
                    List<User> u;
                    if ((u = User._find(User.class, "name=?", user).exec(User.class)) != null) {
                        if (u.size() == 1) {
                            User userObj = u.get(0);
                            if (User.hash(pass).equals(userObj.getPassword())) {
                                // Correct login info

                                Session session = new Session();
                                session.setKey(UUID.randomUUID().toString());
                                session.setIp(ip);
                                session._insert();
                                userObj._set(session);

                                e.getSession().getSessionData().put("session", session);

                                Success s = new Success(101, "You are now logged in!");
                                s.getData().put("key", session.getKey());
                                EventPublisher.raiseEvent(new LoggedIn(userObj), true, null); // raise logged in event
                                e.getResponse().send(s);

                            } else {
                                // Incorrect login info
                                e.getResponse().send(new Error(101, "Login information incorrect!"));
                            }
                        } else {
                            // No account found
                            e.getResponse().send(new Error(101, "Login information incorrect!"));
                        }
                    } else {
                        // No account found
                        e.getResponse().send(new Error(101, "Login information incorrect!"));
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
    @WSEvent(method = "process", action = "resume_session", description = "Process and log the user in", enabled = true, name = "Process Resume Session")
    public void resumeSession(RequestEvent e){
        if(
            !e.getSession().getSessionData().containsKey("session")
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
