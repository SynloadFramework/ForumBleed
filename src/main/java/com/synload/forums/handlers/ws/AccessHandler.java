package com.synload.forums.handlers.ws;

import com.synload.eventsystem.EventPublisher;
import com.synload.eventsystem.events.RequestEvent;
import com.synload.forums.elements.status.Error;
import com.synload.forums.elements.status.Success;
import com.synload.forums.events.user.LoggedIn;
import com.synload.forums.events.user.LoggedOut;
import com.synload.forums.models.Session;
import com.synload.forums.models.User;
import com.synload.framework.ws.annotations.WSEvent;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class AccessHandler {
    @WSEvent(method = "process", action = "login", description = "Process and log the user in", enabled = true, name = "Process Login")
    public void processLogin(RequestEvent e){
        if(
            !e.getSession().getSessionData().containsKey("session")
        ){
            if(
                e.getRequest().getData().containsKey("user") &&
                e.getRequest().getData().containsKey("pass")
            ) {
                String user = e.getRequest().getData().get("user");
                String pass = e.getRequest().getData().get("pass");
                try {
                    List<User> u;
                    if ((u = User._find(User.class, "name=?", user).exec(User.class)) != null) {
                        if (u.size() == 1) {
                            User userObj = u.get(0);
                            if (User.hash(pass).equals(userObj.getPassword())) {
                                // Correct login info

                                Session session = new Session();
                                session.setKey(UUID.randomUUID().toString());
                                session.setIp(e.getSession().session.getRemoteAddress().getAddress().getHostAddress());
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

                    Success s = new Success(104, "You have logged out!");
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
