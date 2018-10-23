package at.technikum_wien.sommerbauer.newsreader.data.parser;

import android.os.Build;
import android.text.Html;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import at.technikum_wien.sommerbauer.newsreader.data.NewsItem;

public class RssParser {
  //private static final String LOG_TAG = RssParser.class.getCanonicalName();

  private static final String ns = null;

  public List<NewsItem> parse(InputStream in) throws XmlPullParserException, IOException, ParseException {
    try {
      XmlPullParser parser = Xml.newPullParser();
      parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
      parser.setInput(in, null);
      parser.nextTag();
      return readRss(parser);
    }
    finally {
      in.close();
    }
  }

  private List<NewsItem> readRss(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
    List<NewsItem> entries = new ArrayList<>();
    parser.require(XmlPullParser.START_TAG, ns, "rss");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      if (name.equals("channel")) {
        entries.addAll(readChannel(parser));
      }
      else {
        skip(parser);
      }
    }
    return entries;
  }
  private List<NewsItem> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
    List<NewsItem> entries = new ArrayList<>();
    parser.require(XmlPullParser.START_TAG, ns, "channel");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      //Log.d(LOG_TAG, "In channel: " + name);
      if (name.equals("item")) {
        entries.add(readItem(parser));
      }
      else {
        skip(parser);
      }
    }
    return entries;
  }

  private NewsItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
    parser.require(XmlPullParser.START_TAG, ns, "item");
    String id = null;
    String title = null;
    String link = null;
    String author = null;
    String description = null;
    String imgString = null;
    Date publishedOn = null;
    Set<String> keywords = new HashSet<>();

    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      //Log.d(LOG_TAG, "In item: " + name);
      switch (name) {
        case "dc:identifier":
          id = readBasicTag(parser, "dc:identifier").trim();
          break;
        case "title":
          title = readBasicTag(parser, "title").trim();
          break;
        case "category":
          keywords.add(readBasicTag(parser, "category").trim());
          break;
        case "link":
          link = readBasicTag(parser, "link").trim();
          break;
        case "pubDate":
          publishedOn = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US).parse(readBasicTag(parser, "pubDate"));
          break;
        case "dc:creator":
          author = readBasicTag(parser, "dc:creator").trim();
          break;
        case "description":
          description = readBasicTag(parser, "description");
          if (description != null) {
            description = description.trim();
            if (description.startsWith("<img")) {
              int idx = description.indexOf("/>");
              if (idx > 0) {
                imgString = description.substring(0, idx);
                description = description.substring(idx + 2).trim();
                int start = -1;
                if (imgString.indexOf("src=\"") > 0) {
                  start = imgString.indexOf("src=\"") + 5;
                }
                else if (imgString.indexOf("src=\"") > 0) {
                  start = imgString.indexOf("src=") + 4;
                }
                if (start > 0) {
                  imgString = imgString.substring(start);
                  if (imgString.indexOf("\"") > 0)
                    imgString = imgString.substring(0, imgString.indexOf("\""));
                }
                if (Build.VERSION.SDK_INT >= 24)
                  imgString = Html.fromHtml(imgString.trim(), Html.FROM_HTML_MODE_LEGACY).toString().trim();
                else
                  imgString = Html.fromHtml(imgString.trim()).toString().trim();
              }
              if (Build.VERSION.SDK_INT >= 24)
                description = Html.fromHtml(description.trim(), Html.FROM_HTML_MODE_LEGACY).toString().trim();
              else
                description = Html.fromHtml(description.trim()).toString().trim();
            }
          }
          break;
        default:
          skip(parser);
          break;
      }
    }
    return new NewsItem(id, title, link, description, imgString, author, publishedOn, keywords);
  }

  private String readBasicTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
    parser.require(XmlPullParser.START_TAG, ns, tag);
    String result = readText(parser);
    parser.require(XmlPullParser.END_TAG, ns, tag);
    return result;
  }

  private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    String result = null;
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.getText();
      parser.nextTag();
    }
    return result;
  }

  private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
    if (parser.getEventType() != XmlPullParser.START_TAG) {
      throw new IllegalStateException();
    }
    int depth = 1;
    while (depth != 0) {
      switch (parser.next()) {
        case XmlPullParser.END_TAG:
          depth--;
          break;
        case XmlPullParser.START_TAG:
          depth++;
          break;
      }
    }
  }

}
