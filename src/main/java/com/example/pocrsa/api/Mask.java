package com.example.pocrsa.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class Mask {

    private static final int PAN_UNMASKED_CHARS_BEGINNING = 6; // No. of unmasked character at beginning of PAN
    private static final int PAN_UNMASKED_CHARS_END = 4; // No. of unmasked chars at end of PAN
    private static final int PAN_UNMASKED_LENGTH = PAN_UNMASKED_CHARS_BEGINNING + PAN_UNMASKED_CHARS_END; // No. of total unmasked chars

    public static String pan(String pan, String replace) {
        if (pan == null) {
            return null;
        }
        return apply(pan, PAN_UNMASKED_CHARS_BEGINNING, pan.length() - PAN_UNMASKED_CHARS_END, replace);
    }

    public static String all(String input, String replace) {
        if (input == null) {
            return "";
        }
        return replace.repeat(input.length());
    }

    public static String apply(String input, int start, int end, String replace) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }

        if (input.length() < PAN_UNMASKED_LENGTH) {
            return all(input, replace);
        }

        if (start < 0) {
            start = 0;
        }

        if (end > input.length()) {
            end = input.length();
        }

        if (start > end) {
            throw new IllegalArgumentException("End index cannot be greater than start index");
        }

        int maskLength = end - start;

        return StringUtils.overlay(input, replace.repeat(maskLength), start, end);
    }
}
