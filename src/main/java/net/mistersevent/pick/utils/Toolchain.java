package net.mistersevent.pick.utils;

import java.text.DecimalFormat;
import java.util.Random;

public class Toolchain {
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.#");
   private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("#.###");
   private static final Random RANDOM = new Random();
   private static final String[] MONEY_FORMATS = new String[]{"M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR", "QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG"};

   public static String format(double value) {
      if (value <= 999999.0D) {
         return DECIMAL_FORMAT.format(value);
      } else {
         int zeros = (int)Math.log10(value);
         int thou = zeros / 3;
         int arrayIndex = Math.min(thou - 2, MONEY_FORMATS.length - 1);
         return DECIMAL_FORMAT.format(value / Math.pow(1000.0D, (double)arrayIndex + 2.0D)) + MONEY_FORMATS[arrayIndex];
      }
   }

   public static String formatWithoutReduce(double value) {
      return DECIMAL_FORMAT.format(value);
   }

   public static String formatPercentage(double value) {
      return PERCENTAGE_FORMAT.format(value);
   }

   public static int getRandomInt(int min, int max) {
      return RANDOM.nextInt(max + 1 - min) + min;
   }
}

