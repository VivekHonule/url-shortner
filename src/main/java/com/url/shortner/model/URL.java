package com.url.shortner.model;

public class URL {

    private String longUrl;
    private String shortenedUrl;

    public URL(String longUrl) {
        this.longUrl = longUrl;
    }

    public URL(String longUrl, String shortenedUrl) {
        this.longUrl = longUrl;
        this.shortenedUrl = shortenedUrl;
    }

    public URL() {
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }
}
