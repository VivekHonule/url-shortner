package com.url.shortner.service;

import com.url.shortner.exception.UrlException;
import com.url.shortner.model.URL;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.List;

@Component
public class DefaultUrlShortnerService extends AbstractUrlShortnerService {

    private static final int FIRST_43_BITS_OF_MD5_HASH = 43;

    @Override
    public URL shorten(URL url) throws UrlException {
        URL shortenedUrl = getIfPresent(url);
        if (shortenedUrl.getShortenedUrl() == null) {
            shortenedUrl = computeShortenedUrl(url);
            save(shortenedUrl);
        }
        return shortenedUrl;
    }

    @Override
    public URL longUrl(URL url) throws UrlException {
        return getIfPresent(url);
    }

    @Override
    protected URL computeShortenedUrl(URL url) throws UrlException {
        URL shortenedUrl = new URL(url.getLongUrl());
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = url.getLongUrl().getBytes(StandardCharsets.UTF_8);
            byte[] md5Hash = messageDigest.digest(bytes);
            boolean[] bits = readBitsFromHash(md5Hash, FIRST_43_BITS_OF_MD5_HASH);
            Long decimal = convertBinaryToDecimal(bits);
            List<Integer> base62 = convertDecimalToBase62(decimal);
            String base62String = base62ToString(base62);
            String shortenedUrlStr = createShortenedUrl(base62String);
            shortenedUrl.setShortenedUrl(shortenedUrlStr);
        } catch (Exception e) {
            throw new UrlException("Error occured while shortening the url", e);
        }
        return shortenedUrl;
    }

    @Override
    protected URL getIfPresent(URL url) throws UrlException {
        URL shortenedUrl = new URL();
        File urlDir = getUrlDir();
        if (!urlDir.exists()) {
            return shortenedUrl;
        }

        File urls = new File(urlDir, "urls");
        try (BufferedReader br = Files.newBufferedReader(urls.toPath())) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                String longUrl = url.getLongUrl();
                String shortUrl = url.getShortenedUrl();
                if (longUrl.equals(split[0]) || shortUrl.equals(split[1])) {
                    shortenedUrl.setLongUrl(longUrl);
                    shortenedUrl.setShortenedUrl(split[1]);
                }
            }
        } catch (IOException e) {
            throw new UrlException("Error occurred while reading url from file", e);
        }
        return shortenedUrl;
    }

    @Override
    protected void save(URL shortenedUrl) throws UrlException {
        File urlDir = getUrlDir();
        if (!urlDir.exists()) {
            urlDir.mkdir();
        }

        File urls = new File(urlDir, "urls");
        try (BufferedWriter bw = Files.newBufferedWriter(urls.toPath(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(shortenedUrl.getLongUrl() + " " + shortenedUrl.getShortenedUrl());
            bw.newLine();
        } catch (IOException e) {
            throw new UrlException("Error occurred while writing url to file", e);
        }
    }
}
