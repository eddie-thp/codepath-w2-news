package org.ethp.codepath.oldnews.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Articles {
    @SerializedName("docs")
    List<Article> articles;

    public Articles() {
        articles = new ArrayList<Article>();
    }

}
