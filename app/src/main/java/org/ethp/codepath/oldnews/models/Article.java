package org.ethp.codepath.oldnews.models;

import android.graphics.Movie;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.thumbnail;
import static android.media.CamcorderProfile.get;

/**
 * Article model implementation
 */
@Parcel
public class Article {

    Headline headline;

    @SerializedName("web_url")
    String webUrl;

    @SerializedName("multimedia")
    List<Multimedia> thumbnails;

    /**
     * Empty constructor required by the Parceler library
     */
    public Article() {
        thumbnails = new ArrayList<Multimedia>();
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline.main;
    }

    public String getThumbnail() {
        String thumbnail = "";
        if (thumbnails.size() > 0) {
            thumbnail = "https://www.nytimes.com/" + thumbnails.get(0).url;
        }
        return thumbnail;
    }

}
