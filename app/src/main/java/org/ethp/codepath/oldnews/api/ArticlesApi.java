package org.ethp.codepath.oldnews.api;

import android.content.Context;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.models.ArticleSearchParameters;
import org.ethp.codepath.oldnews.models.Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ArticlesApi {

    private interface ApiInterface {
        @GET("/svc/search/v2/articlesearch.json")
        Call<org.ethp.codepath.oldnews.models.Response> getArticles(@Query("api-key") String apiKey,
                                                                    @Query("page") int page,
                                                                    @Query("q") String query,
                                                                    @Query("begin_date") String beginDate,
                                                                    @Query("sort") String sort,
                                                                    @Query("fq") String filteredQuery);
    }

    private  static final String BASE_URL = "https://api.nytimes.com/";

    private Retrofit retrofit;
    private ApiInterface apiService;
    private Context context;

    public ArticlesApi(Context context) {
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =  retrofit.create(ApiInterface.class);
    }

    public Call<org.ethp.codepath.oldnews.models.Response> getArticles(int page, ArticleSearchParameters parameters, Callback<Response> callback) {
        Call<org.ethp.codepath.oldnews.models.Response> call = apiService
                .getArticles(context.getString(R.string.api_key_ny_times)
                        , page
                        , parameters.getQuery()
                        , parameters.getBeginDate()
                        , parameters.getSortBy()
                        , parameters.getFilteredQuery());

        call.enqueue(callback);
        return call;
    }

}
