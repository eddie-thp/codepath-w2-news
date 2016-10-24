package org.ethp.codepath.oldnews.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    @SerializedName("response")
    Articles articles;
    String status;

    public Response() {

    }

    public List<Article> getArticles() {
        return articles.articles;
    }
}
