package com.example.android.brexitguardian;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {

    /**
     * Base URL components are declared as constants
     */

    // Base URL.
    private static final String URL = "https://content.guardianapis.com/search?q=";

    // The purpose of the App is to display Brexit articles only.
    private static final String URL_TOPIC = "brexit";

    // JSON format.
    private static final String URL_FORMAT = "&format=json";

    // Displaying thumbnails and contributor.
    private static final String URL_CONTENT = "&show-fields=thumbnail&show-tags=contributor";

    // API key needed to access the API.
    private static final String URL_KEY = "test";

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of articles.
     */
    private NewsAdapter adapter;

    /**
     * TextView that is displayed when the list is empty.
     */
    private TextView emptyStateTextView;

    /**
     * Progress bar showing the user that the data is loading.
     */
    private ProgressBar loadingProgressSpinner;

    /**
     * Renders and recycles our set of views.
     */
    private RecyclerView RecyclerView;

    /**
     * Refreshes the content.
     */
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Set the refresh listener to refresh the layout when the user swipes the screen.
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView = (RecyclerView) findViewById(R.id.list_view);
        RecyclerView.setHasFixedSize(true);

        android.support.v7.widget.RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(LayoutManager);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);

        loadingProgressSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new adapter that takes an empty list of articles as input.
        adapter = new NewsAdapter(this, new ArrayList<News>(), new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News news) {
                String url = news.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        RecyclerView.setAdapter(adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity.
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network.
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter.
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingProgressSpinner.setVisibility(View.GONE);

            // Hide RecyclerView
            RecyclerView.setVisibility(View.GONE);

            // Update empty state with no connection error message.
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    // Create a new loader for the given URL
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        // User's latest preferences for the articles order
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String order = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_default));

        RecyclerView.setVisibility(View.GONE);

        /*
         * Use the key: getString(R.string.settings_order_by_key).
         * When building the URI and appending query parameters, use the user's preference.
         * Not hardcode "order-by".
         */
        String query = URL + URL_TOPIC + URL_FORMAT + URL_CONTENT;
        Uri baseUri = Uri.parse(query);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("order-by", order);
        uriBuilder.appendQueryParameter("api-key", URL_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        loadingProgressSpinner.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(false);

        // Clear the adapter of previous data
        adapter.clear();

        /*
         * If there is a valid list of {@link News}, add them to the adapter's data set.
         * This will trigger the list to update.
         */
        if (news != null && !news.isEmpty()) {
            RecyclerView.setVisibility(View.VISIBLE);
            adapter.addAll(news);
            //If there are no articles to display, then display a "no articles found" message.
        } else
            emptyStateTextView.setText(R.string.no_articles_found);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
    }
}
