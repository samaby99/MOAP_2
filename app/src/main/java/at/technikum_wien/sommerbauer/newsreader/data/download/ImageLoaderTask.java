package at.technikum_wien.sommerbauer.newsreader.data.download;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import at.technikum_wien.sommerbauer.newsreader.R;

public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {
  private static final String LOG_TAG = ImageLoaderTask.class.getCanonicalName();
  @SuppressLint("StaticFieldLeak") // Will be refractored in a future version of the App
  private ImageView mImageView;
  @SuppressLint("StaticFieldLeak") // Will be refractored in a future version of the App
  private ProgressBar mProgressBar;
  private String mUri;
  private boolean mLoad;

  public ImageLoaderTask(ImageView imageView, String uri) {
    this(imageView, null, uri);
  }

  public ImageLoaderTask(ImageView imageView, ProgressBar progressBar, String uri) {
    mImageView = imageView;
    mProgressBar = progressBar;
    mUri = uri;
    mImageView.setTag(uri);
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(imageView.getContext());
    mLoad = sharedPreferences.getBoolean(imageView.getContext().getString(R.string.settings_image_display_key),
                                                imageView.getContext().getResources().getBoolean(R.bool.settings_image_display_default));
  }

  @Override
  protected Bitmap doInBackground(Void... voids) {
    if (mUri == null || !mLoad)
      return null;

    HttpURLConnection urlConnection = null;
    try {
      URL url = new URL(mUri);
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setConnectTimeout(5000);
      int statusCode = urlConnection.getResponseCode();
      if (statusCode != 200) {
        Log.e(LOG_TAG, "Error downloading image from " + mUri + ". Response code: " + statusCode);
        return null;
      }

      InputStream inputStream = urlConnection.getInputStream();
      if (inputStream == null) {
        Log.e(LOG_TAG, "Error downloading image from " + mUri);
        return null;
      }
      return BitmapFactory.decodeStream(inputStream);
    }
    catch(MalformedURLException ex) {
      Log.e(LOG_TAG, "Invalid image url: " + mUri, ex);
      return null;
    }
    catch(IOException ex) {
      Log.e(LOG_TAG, "IOException while downloading image from url: " + mUri, ex);
      return null;
    }
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    mImageView.setVisibility(View.INVISIBLE);
    if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
  }

  @Override
  protected void onPostExecute(Bitmap bitmap) {
    super.onPostExecute(bitmap);
    if (mProgressBar != null) mProgressBar.setVisibility(View.INVISIBLE);
    if (bitmap != null && mImageView.getTag().equals(mUri)) {
      mImageView.setImageBitmap(bitmap);
      mImageView.setVisibility(View.VISIBLE);
    }
  }
}
