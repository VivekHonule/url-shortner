package com.url.shortner.model;

public class URL {

    private String longUrl;
    private String shortenedUrl;

    public URL(String longUrl) {
        this.longUrl = longUrl;
    }

    public URL() {
    }

    public String getLongUrl() {
        if (longUrl == null) {
            return "";
        }
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortenedUrl() {
        if (shortenedUrl == null) {
            return "";
        }
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }
}
