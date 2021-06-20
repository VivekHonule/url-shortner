package com.url.shortner.service;

import com.url.shortner.exception.UrlException;
import com.url.shortner.model.URL;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultUrlShortnerService implements UrlShortnerService {

    private static final int FIRST_43_BITS_OF_MD5_HASH = 43;

    @Override
    public URL shorten(URL url) throws UrlException {
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
        } catch (NoSuchAlgorithmException e) {
            throw new UrlException("Error occured while shortening the url", e);
        }

        return shortenedUrl;
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
