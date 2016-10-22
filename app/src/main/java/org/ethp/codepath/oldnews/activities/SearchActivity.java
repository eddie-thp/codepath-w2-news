package org.ethp.codepath.oldnews.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.adapters.ArticleAdapter;
import org.ethp.codepath.oldnews.fragments.SearchSettingsFragment;
import org.ethp.codepath.oldnews.models.Article;
import org.ethp.codepath.support.recyclerview.ItemClickSupport;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    MenuItem miSearch;
    MenuItem miSearchProgress;

    @BindView(R.id.rvArticles)
    RecyclerView rvArticles;

    List<Article> articles;
    ArticleAdapter articlesAdapter;

    // TODO remove from here
    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();

        boolean isNetworkAvailable = isNetworkAvailable();

        // Figure out why fetching articles at the beginning causes crash

        //fetchArticles();
        // probably because onPrepareOptionsMenu is called after this, then when to call fetch ?
    }

    private void setup() {
        ButterKnife.bind(this);
        articles = new ArrayList<>();
        articlesAdapter = new ArticleAdapter(this, articles);
        rvArticles.setAdapter(articlesAdapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(gridLayoutManager);

        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // Get article
                Article article = articles.get(position);
                // Create and setup intent
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("article", article);
                // Launch the activity
                startActivity(intent);
            }
        });


        // TODO remove this from here
        params = new RequestParams();
        params.put("api-key", "9c7e6b7d1d334c1fbf5cdd8d6dba16e7");
        params.put("page", "0");

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        setupSearchAction(menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miSearchProgress = menu.findItem(R.id.miActionProgress);
        return super.onPrepareOptionsMenu(menu);
    }

    private void setupSearchAction(Menu menu) {
        miSearch = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(miSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Execute query
                SearchActivity.this.fetchArticles(query);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchArticles() {
        fetchArticles("");
    }

    public void fetchArticles(String query) {
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        if (query.isEmpty()) {
            params.remove("q");
        } else {
            params.put("q", query);
        }

        miSearch.collapseActionView();
        miSearchProgress.setVisible(true);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int size = articles.size();
                articles.clear();
                if (size > 0) {
                    articlesAdapter.notifyItemRangeRemoved(0, size);
                }

                JSONArray docs = null;
                try {
                    docs = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(docs));
                    articlesAdapter.notifyItemRangeInserted(0, articles.size());
                } catch (Exception e) {
                    Log.e("NY_TIMES_API_GET", "Failed parsing response: " + e.getMessage(), e);
                }
                miSearchProgress.setVisible(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                setVisible(false);
            }
        });

        if (query.isEmpty()) {
            Toast.makeText(this, "Search: " + query, Toast.LENGTH_LONG).show();
        }
    }

    public void onSettingsAction(MenuItem menuItem) {
        FragmentManager fm = getSupportFragmentManager();
        SearchSettingsFragment searchSettingsFragment = new SearchSettingsFragment();

        searchSettingsFragment.setOnApplyClickedListener(new SearchSettingsFragment.OnApplyClickedListener() {
            @Override
            public void onApplyClicked(Date beginDate, int sortBySelection, boolean newsDeskArtsChecked, boolean newsDeskFashionChecked, boolean newsDeskSportsChecked) {
                // Setup begin date param
                if (beginDate == null) {
                    params.remove("begin_date");
                } else {
                    String beginDateStr = new SimpleDateFormat("yyyyMMdd").format(beginDate);
                }

                // Setup sort param
                String sortByValue = getResources().getStringArray(R.array.sort_by_api_values)[sortBySelection];
                if (sortByValue.isEmpty()) {
                    params.remove("sort");
                } else {
                    params.put("sort", sortByValue);
                }

                StringBuilder newsDeskValBuilder = new StringBuilder();
                if (newsDeskArtsChecked) {
                    newsDeskValBuilder.append('"').append("Arts").append("\" ");
                }
                if (newsDeskFashionChecked) {
                    newsDeskValBuilder.append('"').append("Fashion & Style").append("\" ");
                }
                if (newsDeskSportsChecked) {
                    newsDeskValBuilder.append('"').append("Sports").append("\" ");
                }

                String test = getResources().getStringArray(R.array.sort_by_api_values).toString();

                // Setup news_desk param
                String newsDeskVal = newsDeskValBuilder.toString();
                if (newsDeskVal.isEmpty()) {
                    params.remove("fq");
                } else {
                    params.put("fq", String.format("news_desk:(%s)", newsDeskVal));
                }

                // Execute search
                SearchActivity.this.fetchArticles();

            }
        });

        searchSettingsFragment.show(fm, "fragment_search_settings");
    }
}
