package org.ethp.codepath.oldnews.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.models.Article;

import java.util.List;

import static android.R.attr.resource;
import static org.ethp.codepath.oldnews.R.id.tvTitle;

/**
 * Created by eddie_thp on 10/18/16.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get Item for the position
        Article article = getItem(position);

        // Check if convertView exists
        // Otherwise create layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        // find image view, text view
        ImageView iv = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tv = (TextView) convertView.findViewById(tvTitle);
        // clear image
        iv.setImageResource(0);

        // set data
        tv.setText(article.getHeadline());

        //
        String thumbnail = article.getThumbnail();
        if (!thumbnail.isEmpty()) {
            Picasso.with(getContext()).load(thumbnail).into(iv);
        }

        return convertView;
    }
}
