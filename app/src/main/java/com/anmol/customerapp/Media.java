package com.anmol.customerapp;

import java.io.Serializable;

/**
 * Created by anmol on 2017-08-04.
 */

public class Media implements Serializable {
    String url;
    String presuri;
    String type;
    String imagedescription;
    String uploadid;

    public String getUploadid() {
        return uploadid;
    }

    public void setUploadid(String uploadid) {
        this.uploadid = uploadid;
    }

    public String getImagedescription() {
        return imagedescription;
    }

    public void setImagedescription(String imagedescription) {
        this.imagedescription = imagedescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPresuri() {
        return presuri;
    }

    public void setPresuri(String presuri) {
        this.presuri = presuri;
    }

    public Media(String url, String presuri, String type, String imagedescription, String uploadid) {
        this.url = url;this.presuri = presuri;this.type = type;this.imagedescription = imagedescription;this.uploadid = uploadid;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Media(){}
}
