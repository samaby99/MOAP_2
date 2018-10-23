package at.technikum_wien.sommerbauer.newsreader.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class NewsItem implements Serializable {
  private static final long serialVersionUID = 1L;

  private String mIdentifier;
  private String mTitle;
  private String mDescription;
  private String mLink;
  private String mImageUrl;
  private String mAuthor;
  private Date mPublicationDate;
  private Set<String> mKeywords;

  public NewsItem(String identifier, String title, String link, String description, String imageUrl, String author, Date publicationDate,
                  Set<String> keywords) {
    this.mIdentifier = identifier;
    this.mLink = link;
    this.mTitle = title;
    this.mDescription = description;
    this.mImageUrl = imageUrl;
    this.mAuthor = author;
    this.mPublicationDate = publicationDate;
    this.mKeywords = keywords;
  }

  public String getIdentifier() {
    return mIdentifier;
  }

  public void setIdentifier(String identifier) {
    this.mIdentifier = identifier;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String title) {
    this.mTitle = title;
  }

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    this.mDescription = description;
  }

  public String getAuthor() {
    return mAuthor;
  }

  public void setAuthor(String author) {
    this.mAuthor = author;
  }

  public Date getPublicationDate() {
    return mPublicationDate;
  }

  public void setPublicationDate(Date publicationDate) {
    this.mPublicationDate = publicationDate;
  }

  public String getLink() {
    return mLink;
  }

  public void setLink(String link) {
    this.mLink = link;
  }

  public String getImageUrl() {
    return mImageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.mImageUrl = imageUrl;
  }

  public Set<String> getKeywords() {
    return mKeywords;
  }

  public void addKeyword(String keyword) {
    this.mKeywords.add(keyword);
  }
}
