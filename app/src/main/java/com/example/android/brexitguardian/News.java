package com.example.android.brexitguardian;

/**
 * An {@link News} object contains information related to a single article.
 */
class News {

    /** thumbnail image of the article */
    private String articleImage;

    /** Title of the article */
    private String articleTitle;

    /** Section of the website the article is in */
    private String articleSection;

    /** Author of the article */
    private String articleAuthor;

    /** Date when article was published */
    private String articleDate;

    /** Website URL of the article */
    private String articleUrl;

    /**
     * Constructs a new {@link News} object
     *
     * @param image is the thumbnail image of the article
     * @param title is the title of the article
     * @param section specifies the part of the website where the article is placed
     * @param author is the author of the article
     * @param date of publishing the article
     * @param url is the website URL to find more information about the article
     */
    News(String image, String title, String section, String author, String date, String url){
        articleImage = image;
        articleTitle = title;
        articleSection = section;
        articleAuthor = author;
        articleDate = date;
        articleUrl = url;
    }

    /**
     * Returns the thumbnail image of the article.
     */
    String getImage(){
        return articleImage;
    }

    /**
     * Returns the title of the article.
     */
    String getTitle(){
        return articleTitle;
    }

    /**
     * Returns the name of the website section to which the article belongs.
     */
    String getSection(){
        return articleSection;
    }

    /**
     * Returns the name of the author of the article.
     */
    String getAuthor(){
        return articleAuthor;
    }

    /**
     * Returns the date when the article was published.
     */
    String getDate(){
        return articleDate;
    }

    /**
     * Returns the website URL to find more information about the the article.
     */
    String getUrl() {
        return articleUrl;
    }
}
