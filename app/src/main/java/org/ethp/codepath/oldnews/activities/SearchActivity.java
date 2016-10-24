package org.ethp.codepath.oldnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.adapters.ArticleAdapter;
import org.ethp.codepath.oldnews.api.ArticlesApi;
import org.ethp.codepath.oldnews.databinding.ContentSearchBinding;
import org.ethp.codepath.oldnews.fragments.SearchSettingsFragment;
import org.ethp.codepath.oldnews.models.Article;
import org.ethp.codepath.oldnews.models.ArticleSearchParameters;
import org.ethp.codepath.oldnews.models.Response;
import org.ethp.codepath.support.network.ConnectivityHelper;
import org.ethp.codepath.support.recyclerview.EndlessRecyclerViewScrollListener;
import org.ethp.codepath.support.recyclerview.ItemClickSupport;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity {

    ContentSearchBinding binding;
    RecyclerView rvArticles;

    MenuItem miSearch;
    MenuItem miSearchProgress;

    List<Article> articles;
    ArticleAdapter articlesAdapter;

    private ArticleSearchParameters searchParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        binding = ContentSearchBinding.bind(findViewById(R.id.content_search));
        rvArticles = binding.rvArticles;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();

        // Fetch articles
        fetchArticles(0);
    }

    private void setup() {
        // Setup Articles Recycler View
        articles = new ArrayList<>();
        articlesAdapter = new ArticleAdapter(this, articles);
        // Setup click support
        setupRecyclerView();
        // Create object to store the search parameter
        searchParameters = new ArticleSearchParameters();
    }

    private void setupRecyclerView() {
        // Set the adapter
        rvArticles.setAdapter(articlesAdapter);
        // Set the layout manager
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(gridLayoutManager);
        // Add click support
        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // Get article
                Article article = articles.get(position);
                // Create and setup intent
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("article", Parcels.wrap(article));
                // Launch the activity
                startActivity(intent);
            }
        });
        // Add endless scroll support
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                // customLoadMoreDataFromApi(page);
                fetchArticles(page);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Setup the search action
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

    /**
     * Fetch articles when the query changes
     * @param query
     */
    public void fetchArticles(String query) {
        searchParameters.setQuery(query);

        if (!query.isEmpty()) {
            Toast.makeText(this, "Searching: " + query, Toast.LENGTH_LONG).show();
        }

        fetchArticles(0);
    }

    /**
     * fetch page used by endless scroll
     * @param page
     */
    public void fetchArticles(final int page) {
        // Clear articles if querying 1st page
        if (page == 0) {
            int size = articles.size();
            articles.clear();
            if (size > 0) {
                articlesAdapter.notifyItemRangeRemoved(0, size);
            }
        }

        // miSearch won't be there during the onCreate call
        if (miSearch != null) {
            miSearch.collapseActionView();
        }
        if (miSearchProgress != null) {
            miSearchProgress.setVisible(true);
        }

        // Execute API request
        ArticlesApi articlesApi = new ArticlesApi(this);
        articlesApi.getArticles(page, searchParameters, new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                int statusCode = response.code();
                Response articlesResponse = response.body();
                if (articlesResponse != null) {
                    List<Article> articlesToLoad = articlesResponse.getArticles();
                    int insertAt = articles.size();
                    articles.addAll(articlesToLoad);
                    articlesAdapter.notifyItemRangeInserted(insertAt, articlesToLoad.size());
                    miSearchProgress.setVisible(false);
                } else {
                    miSearchProgress.setVisible(false);
                    handleFetchFailure(page, statusCode);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                handleFetchFailure(page);

            }
        });
    }

    private void handleFetchFailure(final int page) {
        handleFetchFailure(page, null);
    }

    private void handleFetchFailure(final int page, Integer statusCode) {
        // Hide progress
        miSearchProgress.setVisible(false);

        String errorMessage = getString(R.string.error_fetch_results_failure);

        // Verify connectivity
        if (statusCode == null)
        {
            // If we have a status code it means we received a response
            // If we don't receive a response, lets check the connectivity
            if (!ConnectivityHelper.isNetworkAvailableAndOnline(this))
            {
                errorMessage = getString(R.string.error_connectivity_failure);
            }
        }

        // Show SnackBar
        View parentLayout = findViewById(R.id.content_search);
        Snackbar.make(parentLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SearchActivity.this.fetchArticles(page);
                    }
                }).show();
    }

    public void onSettingsAction(MenuItem menuItem) {
        FragmentManager fm = getSupportFragmentManager();
        SearchSettingsFragment searchSettingsFragment = new SearchSettingsFragment();

        searchSettingsFragment.setOnApplyClickedListener(new SearchSettingsFragment.OnApplyClickedListener() {
            @Override
            public void onApplyClicked(Date beginDate, int sortBySelection, boolean newsDeskArtsChecked, boolean newsDeskFashionChecked, boolean newsDeskSportsChecked) {
                // Update parameters and execute search
                searchParameters.setBeginDate(beginDate);
                searchParameters.setSortBy(getResources()
                        .getStringArray(R.array.sort_by_api_values)[sortBySelection]);
                searchParameters.setArtsChecked(newsDeskArtsChecked);
                searchParameters.setFashionAndStyleChecked(newsDeskFashionChecked);
                searchParameters.setSportsChecked(newsDeskSportsChecked);

                // Execute search
                SearchActivity.this.fetchArticles(0);
            }
        });

        searchSettingsFragment.show(fm, "fragment_search_settings");
    }
}
