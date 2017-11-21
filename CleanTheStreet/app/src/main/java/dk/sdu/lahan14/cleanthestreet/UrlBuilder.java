package dk.sdu.lahan14.cleanthestreet;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by lasse on 19-11-2017.
 */

public class UrlBuilder {

    private String baseUrl = "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api";

    public String createTaskUrl(TaskDto task) throws UnsupportedEncodingException {

        String str = Base64.encodeToString(task.image, Base64.NO_WRAP);
        return baseUrl +"/tasks/create/"+task.description+"/"+str+"/"+Integer.toString(task.score)+"/"+Float.toString(task.lattitude)+"/"+Float.toString(task.longtitude)+"/"+Integer.toString(task.creator.getId())+"/"+Integer.toString(task.accepter.getId());
    }


}
