package com.wgd.jndvsiplibrary;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * 说明：SharedPreferences工具类
 */

public class JNSpUtils {
    private static String PREFERENCE_NAME = "jndv_pjsip";

    private JNSpUtils() {
        throw new AssertionError();
    }

    /**
     * put string preferences
     *
     * @param context context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean setString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }


    /**
     * get string preferences
     *
     * @param context context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
     * name that is not a string
     * @see #getString(Context, String, String)
     */
    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    /**
     * get string preferences
     *
     * @param context      context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a string
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * put int preferences
     *
     * @param context context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean setInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * get int preferences
     *
     * @param context context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a int
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * get int preferences
     *
     * @param context      context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a int
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * put float preferences
     *
     * @param context context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean setFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     *
     * @param context context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a float
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * get float preferences
     *
     * @param context      context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a float
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * put boolean preferences
     *
     * @param context context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean setBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     *
     * @param context context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     * name that is not a boolean
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * get boolean preferences
     *
     * @param context      context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a boolean
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean setLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

//    public static boolean setLong(String key, long value) {
//        return setLong(JndvApplication.getInstance().getBaseContext(), key, value);
//    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

//    public static long getLong(String key, long defaultValue) {
//        return getLong(JndvApplication.getInstance().getBaseContext(), key, defaultValue);
//    }
    /**
     * 获取远程用户id
     */
    public static Set<String> getRemoteIds(Context context) {
        SharedPreferences pres = context.getSharedPreferences("RemoteIds",
                Context.MODE_PRIVATE);
        Set<String> set = new HashSet<String>();
        return pres.getStringSet("remoteids", set);
    }

    /**
     * 存入声音提醒设置
     */
    public static void saveVoiceData(Context context, String name, boolean value) {
        SharedPreferences pres = context.getSharedPreferences("Voice",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pres.edit();
        editor.putBoolean(name, value);

        editor.commit();
    }

    /**
     * 获取声音提醒设置
     */
    public static boolean getVoiceData(Context context) {
        SharedPreferences pres = context.getSharedPreferences("Voice",
                Context.MODE_PRIVATE);

        boolean value = pres.getBoolean("isVoice", true);
        return value;
    }

    /**
     * 存入震动提醒设置
     */
    public static void saveShakeData(Context context, String name, boolean value) {
        SharedPreferences pres = context.getSharedPreferences("Shake",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pres.edit();
        editor.putBoolean(name, value);

        editor.commit();
    }

    /**
     * 获取震动提醒设置
     */
    public static boolean getShakeData(Context context) {
        SharedPreferences pres = context.getSharedPreferences("Shake",
                Context.MODE_PRIVATE);

        boolean value = pres.getBoolean("isShake", false);
        return value;
    }

    /**
     * 存入是否可以删除
     */
    public static void saveIsCanClear(Context context, boolean value) {
        SharedPreferences pres = context.getSharedPreferences("IsCanClear",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pres.edit();
        editor.putBoolean("iscanclear", value);

        editor.commit();
    }

    /**
     * 获取是否可以删除
     */
    public static boolean getIsCanClear(Context context) {
        SharedPreferences pres = context.getSharedPreferences("IsCanClear",
                Context.MODE_PRIVATE);

        boolean value = pres.getBoolean("iscanclear", false);
        return value;
    }

/*
    *//**
     * @param context
     *
     *//*
    public static void saveDrawable(Context context,Bitmap bitmap) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        String imageBase64 = new String(Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT));
        editor.putString("self",imageBase64 );
        editor.commit();
    }

    public static Drawable getDrawableByKey(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCE_NAME,context.MODE_PRIVATE);
        String temp = sharedPreferences.getString("self", "");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        return Drawable.createFromStream(bais, "");
    }

    public static void clearDrawable(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("self","" );
        editor.commit();
    }*/
}
