package dk.sdu.lahan14.cleanthestreet.Network;

import dk.sdu.lahan14.cleanthestreet.Util.User;

/**
 * Created by Simon-PC on 21-11-2017.
 */

public interface OnJSONResponseCallback {
    public void onJSONResponse(boolean success, User user);
}
