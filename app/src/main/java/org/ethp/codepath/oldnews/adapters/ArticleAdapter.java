package org.ethp.codepath.oldnews.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.models.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static org.ethp.codepath.oldnews.R.id.ivImage;

/**
 * Article RecyclerView Adapter class
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    /**
     * ViewHolder implementation
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements Target, View.OnClickListener {
        @BindView(ivImage)
        DynamicHeightImageView ivThumbnail;
        @BindView(R.id.tvTitle)
        TextView tvHeadline;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
            ivThumbnail.setHeightRatio(ratio);
            ivThumbnail.setImageBitmap(bitmap);
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

        viewHolder.tvHeadline.setText(article.getHeadline());

        // Cleanup thumbnail ImageView and use Picasso to load the new image
        viewHolder.ivThumbnail.setImageResource(0);
        String thumbnail = article.getThumbnail();
        //viewHolder.ivThumbnail.setrat
        if (!thumbnail.isEmpty()) {
            Picasso.with(getContext())
                    .load(thumbnail)
                    .transform(new RoundedCornersTransformation(5, 5))
                    .into(viewHolder);
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
