package org.ethp.codepath.oldnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import org.ethp.codepath.oldnews.models.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.etQuery)
    EditText etQuery;
    @BindView(R.id.gvResults)
    GridView gvResults;
    @BindView(R.id.btnSearch)
    Button btnSearch;

    List<Article> articles;
    ArticleArrayAdapter articlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();
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

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
//https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=9c7e6b7d1d334c1fbf5cdd8d6dba16e7
//https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=9c7e6b7d1d334c1fbf5cdd8d6dba16e7&q=query_to_search

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.add("api-key", "9c7e6b7d1d334c1fbf5cdd8d6dba16e7");
        params.add("page", "0");
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

        Toast.makeText(this, "Search: " + query, Toast.LENGTH_LONG).show();
    }
}
