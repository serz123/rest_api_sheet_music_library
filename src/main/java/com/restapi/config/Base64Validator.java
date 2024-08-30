package com.restapi.config;

import java.util.Base64;
import java.util.regex.Pattern;

public class Base64Validator {

  // Regular expression for base64 format
  private static final Pattern BASE64_REGEX = Pattern.compile("^[A-Za-z0-9+/]+={0,2}$");

  public static boolean isBase64Encoded(String data) {
    // Check if the string matches base64 pattern
    if (data == null || !BASE64_REGEX.matcher(data).matches()) {
      return false;
    }

    // Check if the length of the string is a multiple of 4
    if (data.length() % 4 != 0) {
      return false;
    }

    try {
      // Try to decode the string
      Base64.getDecoder().decode(data);
      return true;
    } catch (IllegalArgumentException e) {
      // If decoding fails, it's not a valid base64 string
      return false;
    }
  }
}
