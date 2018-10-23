package at.technikum_wien.sommerbauer.newsreader.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "newsDb.db";
    public static final int VERSION = 1;

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + NewsContract.NewsEntry.TABLE_NAME + " (" +
                NewsContract.NewsEntry._ID                      + " INTEGER PRIMARY KEY, " +
                NewsContract.NewsEntry.COLUMN_IDENTIFIER        + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_TITLE             + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_DESRIPTION        + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_LINK              + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_IMAGE_URL         + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_AUTHOR            + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_PUBLICATION_DATE  + " NUMERIC NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_KEYWORDS          + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        onCreate(db);
    }
}
