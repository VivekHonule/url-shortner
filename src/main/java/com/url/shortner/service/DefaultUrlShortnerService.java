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
import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultUrlShortnerService implements UrlShortnerService {

    private static final int FIRST_43_BITS_OF_MD5_HASH = 43;

    @Override
    public URL shorten(URL url) throws UrlException {
        URL shortenedUrl = getIfPresent(url);
        if (shortenedUrl.isShortenedUrlPresent()) {
            return shortenedUrl;
        } else {
            return computeShortenedUrl(url);
        }
    }

    private URL computeShortenedUrl(URL url) throws UrlException {
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
            save(shortenedUrl);
        } catch (Exception e) {
            throw new UrlException("Error occured while shortening the url", e);
        }
        return shortenedUrl;
    }

    private URL getIfPresent(URL url) throws UrlException {
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
                if (longUrl.equals(split[0])) {
                    shortenedUrl.setLongUrl(longUrl);
                    shortenedUrl.setShortenedUrl(split[1]);
                }
            }
        } catch (IOException e) {
            throw new UrlException("Error occurred while reading url from file", e);
        }
        return shortenedUrl;
    }

    private File getUrlDir() {
        String userHome = System.getProperty("user.home");
        userHome = userHome.replace("\\", "/");
        return new File(userHome, "url-shortner");
    }

    private void save(URL shortenedUrl) throws UrlException {
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

    private String createShortenedUrl(String base62String) {
        return BASE_URL + base62String;
    }

    private String base62ToString(List<Integer> base62) {
        StringBuilder sb = new StringBuilder();
        for (Integer aBase62 : base62) {
            int idx = aBase62 - 1;
            sb.append(BASE_62_STRING_REPRESENTATION.charAt(idx));
        }
        return sb.toString();
    }

    private List<Integer> convertDecimalToBase62(Long decimal) {
        List<Integer> base62 = new ArrayList<>();
        long dividend = decimal;
        long remainder;
        while (dividend > 0) {
            remainder = dividend % 62;
            dividend = dividend / 62;
            base62.add(0, (int) remainder);
        }
        return base62;
    }

    public Long convertBinaryToDecimal(boolean[] bits) {
        long decimalOfBinary = 0L;
        int exponent = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            if (bits[i]) {
                decimalOfBinary += Math.pow(2, exponent);
            }
            exponent++;
        }
        return decimalOfBinary;
    }

    private boolean[] readBitsFromHash(byte[] md5Hash, int noOfBits) {
        boolean[] bits = new boolean[noOfBits];
        for (int i = 0; i < noOfBits; i++) {
            if ((md5Hash[i / 8] & (1 << (7 - (i % 8)))) > 0) {
                bits[i] = true;
            }
        }
        return bits;
    }
}
