package com.synload.forums.utils.access;

import com.synload.forums.models.Session;
import com.synload.forums.models.User;
import com.synload.framework.ws.WSHandler;

import java.util.List;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class SessionValidation {
    public static User validate(WSHandler ws){
        if(ws.getSessionData().containsKey("session")) {
            Session session = (Session) ws.getSessionData().get("session");
            try {
                List<User> users = session._related(User.class).exec(User.class);
                if(users.size()==1){
                    return users.get(0);
                }
            }catch (Exception x){
                x.printStackTrace();
            }
        }
        return null;
    }
}
