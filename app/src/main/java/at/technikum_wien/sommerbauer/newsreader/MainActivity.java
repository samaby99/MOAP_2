package at.technikum_wien.sommerbauer.newsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import at.technikum_wien.sommerbauer.newsreader.data.NewsItem;
import at.technikum_wien.sommerbauer.newsreader.data.download.NewsLoader;
import at.technikum_wien.sommerbauer.newsreader.rv.NewsListAdapter;
import at.technikum_wien.sommerbauer.newsreader.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>>, SharedPreferences.OnSharedPreferenceChangeListener {
  private static final String LOG_TAG = MainActivity.class.getCanonicalName();
  private static final String SCROLL_POSITION = "scroll_position";

  private NewsListAdapter mAdapter;
  private RecyclerView mNewsList;
  private int mPosition = RecyclerView.NO_POSITION;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mAdapter = new NewsListAdapter(new NewsListAdapter.ListItemClickListener() {
      @Override
      public void onListItemClick(NewsItem clickedItem) {
        Intent intent = new Intent(MainActivity.this, NewsDetailsActivity.class);
        intent.putExtra(NewsDetailsActivity.ITEM_KEY, clickedItem);
        startActivity(intent);
      }
    });

    mNewsList = findViewById(R.id.rv_list);
    mNewsList.setAdapter(mAdapter);
    mNewsList.setLayoutManager(new LinearLayoutManager(this));

    startLoader(false);

    if (savedInstanceState != null) {
      mPosition = savedInstanceState.getInt(SCROLL_POSITION, RecyclerView.NO_POSITION);
    }

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
  }

  private void startLoader(boolean reload) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String url = sharedPreferences.getString(getString(R.string.settings_news_url_key),
                                             getString(R.string.settings_news_url_default));
    Bundle loaderArgs = new Bundle();
    loaderArgs.putString(NewsLoader.FEED_URL_EXTRA, url);

    LoaderManager loaderManager = getSupportLoaderManager();

    if (!reload)
      loaderManager.initLoader(NewsLoader.LOADER_ID, loaderArgs, this);
    else
      loaderManager.restartLoader(NewsLoader.LOADER_ID, loaderArgs, this);
  }

  @NonNull
  @Override
  public Loader<List<NewsItem>> onCreateLoader(int id, final Bundle args) {
    return new NewsLoader(this, args.getString(NewsLoader.FEED_URL_EXTRA));
  }

  @Override
  public void onLoadFinished(@NonNull Loader<List<NewsItem>> loader, List<NewsItem> data) {
    if (data == null)
      Toast.makeText(this, R.string.rss_load_error, Toast.LENGTH_LONG).show();
    else {
      int position = ((LinearLayoutManager)mNewsList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
      if (position != RecyclerView.NO_POSITION)
        mPosition = position;
      mAdapter.swapItems(data);
      if (mPosition != RecyclerView.NO_POSITION)
        ((LinearLayoutManager) mNewsList.getLayoutManager()).scrollToPositionWithOffset(mPosition, 0);
    }
  }

  @Override
  public void onLoaderReset(@NonNull Loader<List<NewsItem>> loader) {
    mAdapter.swapItems(new LinkedList<NewsItem>());
  }

  @Override
  public void onPause() {
    super.onPause();
    mPosition = ((LinearLayoutManager)mNewsList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(SCROLL_POSITION, mPosition);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();

    if (itemId == R.id.action_reload) {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      String url = sharedPreferences.getString(getString(R.string.settings_news_url_key), getString(R.string.settings_news_url_default));
      startLoader(true);
      return true;
    }
    else if (itemId == R.id.action_settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      return true;
    }
    else
      return super.onOptionsItemSelected(item);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(getString(R.string.settings_news_url_key))) {
      startLoader(true);
    }
  }
}
