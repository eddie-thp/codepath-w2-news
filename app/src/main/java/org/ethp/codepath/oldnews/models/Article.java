package org.ethp.codepath.oldnews.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Article model implementation
 */
@Parcel
public class Article {
    String webUrl;
    String headline;
    String thumbnail;

    /**
     * Empty constructor required by the Parceler library
     */
    public Article() {

    }

    public Article(JSONObject obj) throws JSONException {
        webUrl = obj.getString("web_url");
        headline = obj.getJSONObject("headline").getString("main");
        JSONArray multimedia = obj.getJSONArray("multimedia");
        if (multimedia.length() > 0)
        {
            thumbnail = "https://www.nytimes.com/" + multimedia.getJSONObject(0).getString("url");

        } else {
            thumbnail = "";
        }
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public static List<Article> fromJSONArray(JSONArray articlesJson) {
        List<Article> articles = new ArrayList<>();

        for(int i = 0; i < articlesJson.length(); i++) {
            try {
                articles.add( new Article(articlesJson.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e("ARTICLE_PARSING", "Failed parsing article json " + e.getMessage(), e);
            }
        }

        return articles;
    }
}
