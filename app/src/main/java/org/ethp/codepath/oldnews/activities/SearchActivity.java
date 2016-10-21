package org.ethp.codepath.oldnews.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.adapters.ArticleArrayAdapter;
import org.ethp.codepath.oldnews.fragments.SearchSettingsFragment;
import org.ethp.codepath.oldnews.models.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.etQuery)
    EditText etQuery;
    @BindView(R.id.gvResults)
    GridView gvResults;
    @BindView(R.id.btnSearch)
    Button btnSearch;

    List<Article> articles;
    ArticleArrayAdapter articlesAdapter;

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

        onArticleSearch();
    }

    private void setup() {
        ButterKnife.bind(this);
        articles = new ArrayList<>();
        articlesAdapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(articlesAdapter);

        gvResults.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        return true;
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

    public void onArticleSearch() {
        onArticleSearch(null);
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        if (!query.isEmpty()) {
            params.add("q", query);
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray docs = null;
                try {
                    docs = response.getJSONObject("response").getJSONArray("docs");
                    // Note that this also adds the information to the list
                    articlesAdapter.addAll(Article.fromJSONArray(docs));
                } catch (Exception e) {
                    Log.e("NY_TIMES_API_GET", "Failed parsing response: " + e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
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
                    String beginDateStr = new SimpleDateFormat(getString(R.string.api_date_format)).format(beginDate);
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
                SearchActivity.this.onArticleSearch();

            }
        });

        searchSettingsFragment.show(fm, "fragment_search_settings");
    }
}
