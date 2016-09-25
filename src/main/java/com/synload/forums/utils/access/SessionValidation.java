package com.synload.forums.utils.access;

import com.synload.forums.models.Session;
import com.synload.framework.ws.WSHandler;

/**
 * Created by Nathaniel on 9/25/2016.
 */
public class SessionValidation {
    public static boolean validate(WSHandler ws){
        if(ws.getSessionData().containsKey("session")) {
            Session session = (Session) ws.getSessionData().get("session");
            session._related()
        }
    }
}
