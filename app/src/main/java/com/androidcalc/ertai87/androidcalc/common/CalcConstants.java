package com.androidcalc.ertai87.androidcalc.common;

import android.content.res.Configuration;
import android.util.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lyle Waldman on 29/06/16.
 */
public class CalcConstants {
    public static int MAX_CHARS_PORTRAIT = 16;
    public static int MAX_CHARS_LANDSCAPE = 32;
    public static int MAX_CHARS_LARGE_PORTRAIT = 25;
    public static int MAX_CHARS_XLARGE_PORTRAIT = 13;
    public static int MAX_CHARS_XLARGE_LAND = 21;

    public static final Map<Pair<Integer, Integer>, Integer> sizeMap;

    static {
        Map<Pair<Integer, Integer>,Integer> aMap = new HashMap<>();
        aMap.put(new Pair<>(Configuration.ORIENTATION_PORTRAIT, Configuration.SCREENLAYOUT_SIZE_NORMAL), MAX_CHARS_PORTRAIT);
        aMap.put(new Pair<>(Configuration.ORIENTATION_LANDSCAPE, Configuration.SCREENLAYOUT_SIZE_NORMAL), MAX_CHARS_LANDSCAPE);
        aMap.put(new Pair<>(Configuration.ORIENTATION_PORTRAIT, Configuration.SCREENLAYOUT_SIZE_LARGE), MAX_CHARS_LARGE_PORTRAIT);
        aMap.put(new Pair<>(Configuration.ORIENTATION_PORTRAIT, Configuration.SCREENLAYOUT_SIZE_XLARGE), MAX_CHARS_XLARGE_PORTRAIT);
        aMap.put(new Pair<>(Configuration.ORIENTATION_LANDSCAPE, Configuration.SCREENLAYOUT_SIZE_XLARGE), MAX_CHARS_XLARGE_LAND);
        sizeMap = Collections.unmodifiableMap(aMap);
    }
}
