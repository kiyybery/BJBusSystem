package ggsdk.xyn.com.bjbussystem.library;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import ggsdk.xyn.com.bjbussystem.BBSApplication;


/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class PreferencesUtil {

    public static <T> void putPreferences(String key, T value) {
        SharedPreferences.Editor editor = BBSApplication.preferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.commit();
    }

    @SuppressLint("CommitPrefEdits")
    public static <T> void put(String key, T value) {
        SharedPreferences.Editor editor = BBSApplication.preferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.commit();
    }

    public static <T> T get(String key, T value) {
        Object o = null;
        if (value instanceof String) {
            o = BBSApplication.preferences.getString(key, value.toString());
        } else if (value instanceof Boolean) {
            o = BBSApplication.preferences.getBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            o = BBSApplication.preferences.getInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            o = BBSApplication.preferences.getFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            o = BBSApplication.preferences.getLong(key, ((Long) value).longValue());
        }
        T t = (T) o;
        return t;
    }
}
