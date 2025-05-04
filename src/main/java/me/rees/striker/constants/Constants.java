package me.rees.striker.constants;

import java.io.*;

public class Constants {
  // General constants
  public static final String STRIKER_WHO_AM_I = "striker-java";
  public static final String STRIKER_VERSION = "v3.00.00";
  public static final String TIME_LAYOUT = "yyyy-MM-dd HH:mm:ss Z";

  //
  public static final long NUMBER_OF_CARDS_IN_DECK = 52;
  public static final long NUMBER_OF_CORES_PHYSICAL = 24;
  public static final long NUMBER_OF_CORES_LOGICAL = 32;
  public static final long NUMBER_OF_CORES_DEFAULT = 1;

  // Define the maximum size string fields
  public static final long MAX_STRING_SIZE = 512;
  public static final long MAX_BUFFER_SIZE = 8192;
  public static final long MAX_MEMORY_SIZE = 536870912;
  public static final long STATUS_ROUNDS = 1000000;

  // Simulation constants
  public static final long MILLION = 1000000L;
  public static final long BILLION = MILLION * 1000L;
  public static final long NUMBER_OF_HANDS_MAXIMUM = BILLION * 10L;
  public static final long NUMBER_OF_HANDS_MINIMUM = 1000L;
  public static final long NUMBER_OF_HANDS_DEFAULT = MILLION * 100L;
  public static final long NUMBER_OF_HANDS_DATABASE = MILLION * 100L;

  // Betting constants
  public static final int MINIMUM_BET = 2;
  public static final int MAXIMUM_BET = 20;
  public static final int TRUE_COUNT_BET = 2;
  public static final int TRUE_COUNT_MULTIPLIER = 26;

  // Methods to get environment variables
  public static String getRulesUrl() {
    return System.getenv("STRIKER_URL_RULES");
  }

  public static String getChartsUrl() {
    return System.getenv("STRIKER_URL_CHARTS");
  }

  public static String getSimulationsUrl() {
    return System.getenv("STRIKER_URL_SIMULATIONS");
  }

  public static String unescapeJson(String str) {
    StringBuilder result = new StringBuilder();
    boolean isEscape = false;

    for (char ch : str.toCharArray()) {
      if (isEscape) {
        switch (ch) {
          case 'n':
            result.append('\n');
            break;
          case '"':
            result.append('"');
            break;
          case '\\':
            result.append('\\');
            break;
          default:
            result.append(ch);
            break;
        }
        isEscape = false;
      } else if (ch == '\\') {
        isEscape = true; // Mark next character as escaped
      } else {
        result.append(ch);
      }
    }
    return result.toString();
  }

  public static String stripQuotes(String str) {
    if (str.length() > 1 && str.startsWith("\"") && str.endsWith("\"")) {
      return str.substring(1, str.length() - 1);
    }
    return str;
  }
}
