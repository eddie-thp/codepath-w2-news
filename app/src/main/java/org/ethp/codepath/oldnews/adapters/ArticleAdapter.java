package org.ethp.codepath.oldnews.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.databinding.ItemArticleResultBinding;
import org.ethp.codepath.oldnews.models.Article;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Article RecyclerView Adapter class
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    /**
     * ViewHolder implementation
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements Target, View.OnClickListener {

        ItemArticleResultBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemArticleResultBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            binding.ivImage.setHeightRatio(ratio);
            binding.ivImage.setImageBitmap(bitmap);
        }
    }

    private Context mContext;

    private List<Article> mArticles;

    /**
     *
     * @param context
     * @param articles
     */
    public ArticleAdapter(Context context, List<Article> articles) {
        mContext = context;
        mArticles = articles;
    }

    /**
     * Returns the Context object
     * @return Context
     */
    private Context getContext() {
        return mContext;
    }

    /**
     * Creates and returns a ViewHolder object
     * @param parent View parent
     * @param viewType View type
     * @return ViewHolder
     */
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    /**
     * Populates the ViewHolder view references with data from the Article at the position
     * @param viewHolder ViewHolder
     * @param position Article position
     */
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        Article article = mArticles.get(position);

        viewHolder.binding.setArticle(article);
        viewHolder.binding.executePendingBindings();
    }

    /**
     * Loads the thumbnail using the data binding framework
     * @param view
     * @param url
     */
    @BindingAdapter({"bind:imageUrl"})
    public static void loadThumbnail(ImageView view, String url) {
        view.setImageResource(0);
        if (!url.isEmpty()) {
            Picasso.with(view.getContext())
                    .load(url)
                    .transform(new RoundedCornersTransformation(5, 5))
                    .into(view);
        }
    }

    /**
     * Returns the total count of Articles in the list
     * @return count of Articles
     */
    @Override
    public int getItemCount() {
        return mArticles.size();
    }


}
