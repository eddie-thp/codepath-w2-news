package org.ethp.codepath.oldnews.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by eddie_thp on 10/18/16.
 */

public class Article implements Serializable {
    private String webUrl;
    private String headline;
    private String thumbnail;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
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
