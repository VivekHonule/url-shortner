package com.url.shortner.service;

import com.url.shortner.exception.UrlException;
import com.url.shortner.model.URL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUrlShortnerService implements UrlShortnerService {

    protected abstract URL computeShortenedUrl(URL url) throws UrlException;

    protected abstract URL getIfPresent(URL url) throws UrlException;

    protected abstract void save(URL url) throws UrlException;

    protected String createShortenedUrl(String base62String) {
        return BASE_URL + base62String;
    }

    protected String base62ToString(List<Integer> base62) {
        StringBuilder sb = new StringBuilder();
        for (Integer aBase62 : base62) {
            int idx = aBase62 - 1;
            sb.append(BASE_62_STRING_REPRESENTATION.charAt(idx));
        }
        return sb.toString();
    }

    protected List<Integer> convertDecimalToBase62(Long decimal) {
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

    protected Long convertBinaryToDecimal(boolean[] bits) {
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

    protected boolean[] readBitsFromHash(byte[] md5Hash, int noOfBits) {
        boolean[] bits = new boolean[noOfBits];
        for (int i = 0; i < noOfBits; i++) {
            if ((md5Hash[i / 8] & (1 << (7 - (i % 8)))) > 0) {
                bits[i] = true;
            }
        }
        return bits;
    }

    protected File getUrlDir() {
        String userHome = System.getProperty("user.home");
        userHome = userHome.replace("\\", "/");
        return new File(userHome, "url-shortner");
    }
}
