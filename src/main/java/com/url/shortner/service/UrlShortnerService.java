package com.url.shortner.service;

import com.url.shortner.exception.UrlException;
import com.url.shortner.model.URL;

public interface UrlShortnerService {

    String BASE_62_STRING_REPRESENTATION = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String BASE_URL = "http://www.short.com/";

    URL shorten(URL url) throws UrlException;
}
