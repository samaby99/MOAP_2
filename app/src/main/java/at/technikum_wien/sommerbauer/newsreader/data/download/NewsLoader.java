package at.technikum_wien.sommerbauer.newsreader.data.download;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import at.technikum_wien.sommerbauer.newsreader.data.NewsItem;
import at.technikum_wien.sommerbauer.newsreader.data.parser.RssParser;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {
  private static final String LOG_TAG = NewsLoader.class.getCanonicalName();
  public static final int LOADER_ID = 42;
  public static final String FEED_URL_EXTRA = "feed_url";

  private String mUrl;
  private List<NewsItem> mResult;

  public NewsLoader(Context context, String url) {
    super(context);
    mUrl = url;
  }

  @Override
  public List<NewsItem> loadInBackground() {
    try {
      Log.d(LOG_TAG, "Start downloading " + mUrl + " ...");
      URL url = new URL(mUrl);
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      try {
        urlConnection.setConnectTimeout(5000);
        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
          Log.w(LOG_TAG, String.format("Error opening RSS feed, Error code: %1$s", urlConnection.getResponseCode()));
          return null;
        }
        Log.d(LOG_TAG, "Start parsing ...");
        RssParser parser = new RssParser();
        List<NewsItem> result = parser.parse(urlConnection.getInputStream());
        Log.d(LOG_TAG, "Parsing finished.");
        return result;

      }
      finally {
        urlConnection.disconnect();
      }
    }
    catch(MalformedURLException ex) {
      Log.w(LOG_TAG, String.format("Error opening RSS feed, Feed %1$s url invalid.", mUrl), ex);
      return null;
    }
    catch(IOException ex) {
      Log.w(LOG_TAG, "Error reading RSS feed.", ex);
      return null;
    }
    catch(ParseException ex) {
      Log.w(LOG_TAG, "Error parsing RSS feed.", ex);
      return null;
    }
    catch(XmlPullParserException ex) {
      Log.w(LOG_TAG, "Error parsing RSS feed.", ex);
      return null;
    }
  }

  @Override
  protected void onStartLoading() {
    if (mUrl == null)
      return;
    if (mResult != null)
      deliverResult(mResult);
    else
      forceLoad();
  }

  @Override
  protected void onStopLoading() {
    cancelLoad();
  }

  @Override
  public void deliverResult(@Nullable List<NewsItem> data) {
    mResult = data;
    super.deliverResult(data);
  }
}
