package at.technikum_wien.sommerbauer.newsreader.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Date;
import java.util.Set;

public class NewsContract {
    public static final String AUTHORITY = "at.technikum_wien.sommerbauer.newsreader";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_NEWS = "news";

    public static final class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";

        public static final String COLUMN_IDENTIFIER = "identifier";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESRIPTION = "description";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_PUBLICATION_DATE = "publication_date";
        public static final String COLUMN_KEYWORDS = "keywords";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final Uri getNewsWithIdUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }
    }
}
