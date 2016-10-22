package org.ethp.codepath.oldnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.models.Article;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    @BindView(R.id.wvArticle)
    WebView webView;

    private Article mArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();

        loadArticle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate Article activity menu
        getMenuInflater().inflate(R.menu.menu_article, menu);
        // Setup share action provider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        setupShareAction((ShareActionProvider) MenuItemCompat.getActionProvider(item));

        return true;
    }

    private void setup() {
        ButterKnife.bind(this);

        // Retrieve article extra
        mArticle = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));

        // Setup webView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO why and how to fix the deprecated call ?
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * Creates share intent and adds to the share action provider
     * @param miActionProvider
     */
    private void setupShareAction(ShareActionProvider miShareAction)
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String shareText = getString(R.string.share_text, mArticle.getHeadline(), mArticle.getWebUrl());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        miShareAction.setShareIntent(shareIntent);
    }

    /**
     * Loads the article into the WebView and setups the share intent
     * @param article
     */
    private void loadArticle()
    {
        String url = mArticle.getWebUrl();
        webView.loadUrl(url);
    }

}
