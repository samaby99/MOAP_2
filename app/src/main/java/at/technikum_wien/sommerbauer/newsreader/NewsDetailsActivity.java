package at.technikum_wien.sommerbauer.newsreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;

import at.technikum_wien.sommerbauer.newsreader.data.NewsItem;
import at.technikum_wien.sommerbauer.newsreader.data.download.ImageLoaderTask;

public class NewsDetailsActivity extends AppCompatActivity {
  public static final String ITEM_KEY = "item";
  private static final String LOG_TAG = NewsDetailsActivity.class.getCanonicalName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news_details);
    if (!getIntent().hasExtra(ITEM_KEY) || !(getIntent().getSerializableExtra(ITEM_KEY) instanceof NewsItem)) {
      Toast.makeText(this, "Internal error.", Toast.LENGTH_SHORT).show();
      Log.e(LOG_TAG, "Intent did not contain a news item.");
    }
    else {
      final NewsItem item = (NewsItem)getIntent().getSerializableExtra(ITEM_KEY);
      TextView newsItemTitleTextView = findViewById(R.id.tv_news_item_title);
      TextView newsItemDescriptionTextView = findViewById(R.id.tv_news_item_description);
      TextView newsItemAuthorTextView = findViewById(R.id.tv_news_item_author);
      TextView newsItemPublicationDateTextView = findViewById(R.id.tv_news_item_publication_date);
      TextView newsItemKeywordsTextView = findViewById(R.id.tv_news_item_keywords);
      ImageView newsItemImageView = findViewById(R.id.iv_news_item_icon);
      Button fullArticleButton = findViewById(R.id.bt_news_item_button);
      fullArticleButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.setData(Uri.parse(item.getLink()));
          if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        }
      });

      newsItemTitleTextView.setText(item.getTitle());
      newsItemDescriptionTextView.setText(item.getDescription());
      newsItemAuthorTextView.setText(item.getAuthor());
      DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
      newsItemPublicationDateTextView.setText(df.format(item.getPublicationDate()));
      newsItemKeywordsTextView.setText(TextUtils.join(", ", item.getKeywords()));
      new ImageLoaderTask(newsItemImageView, item.getImageUrl()).execute();
    }
  }
}
