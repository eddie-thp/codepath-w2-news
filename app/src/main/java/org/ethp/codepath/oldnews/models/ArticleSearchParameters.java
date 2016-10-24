package org.ethp.codepath.oldnews.models;

import org.ethp.codepath.oldnews.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticleSearchParameters {
    String query;
    Date beginDate;
    String sortBy;
    boolean artsChecked;
    boolean fashionAndStyleChecked;
    boolean sportsChecked;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getBeginDate() {
        String beginDateStr = null;
        if (beginDate != null) {
            beginDateStr = new SimpleDateFormat("yyyyMMdd").format(beginDate);
        }
        return beginDateStr;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        if (sortBy.isEmpty()) {
            sortBy = null;
        }
        this.sortBy = sortBy;
    }

    public boolean isArtsChecked() {
        return artsChecked;
    }

    public void setArtsChecked(boolean artsChecked) {
        this.artsChecked = artsChecked;
    }

    public boolean isFashionAndStyleChecked() {
        return fashionAndStyleChecked;
    }

    public void setFashionAndStyleChecked(boolean fashionAndStyleChecked) {
        this.fashionAndStyleChecked = fashionAndStyleChecked;
    }

    public boolean isSportsChecked() {
        return sportsChecked;
    }

    public void setSportsChecked(boolean sportsChecked) {
        this.sportsChecked = sportsChecked;
    }

    public String getFilteredQuery() {
        String filteredQuery = null;

        StringBuilder newsDeskValBuilder = new StringBuilder();
        if (artsChecked) {
            newsDeskValBuilder.append('"').append("Arts").append("\" ");
        }
        if (fashionAndStyleChecked) {
            newsDeskValBuilder.append('"').append("Fashion & Style").append("\" ");
        }
        if (sportsChecked) {
            newsDeskValBuilder.append('"').append("Sports").append("\" ");
        }

        String newsDeskVal = newsDeskValBuilder.toString();

        if (!newsDeskVal.isEmpty()) {
            filteredQuery = String.format("news_desk:(%s)", newsDeskVal);
        }
        return filteredQuery;
    }
}
