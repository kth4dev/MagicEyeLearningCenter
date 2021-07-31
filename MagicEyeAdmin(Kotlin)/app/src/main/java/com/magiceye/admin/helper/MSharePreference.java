package com.magiceye.admin.helper;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class MSharePreference {
    public static String getSavingString(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ALL_DATA",MODE_PRIVATE);
        String value =sharedPreferences.getString(key,null);
        return value;
    }
    public static void  saveString(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ALL_DATA",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static Boolean isAdmin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGIN_LOGOUT",MODE_PRIVATE);
        Boolean value =sharedPreferences.getBoolean("IS_ADMIN",false);
        return value;
    }
    public static void  setAdmin(Context context,boolean user_condition){
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGIN_LOGOUT",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IS_ADMIN",user_condition);
        editor.commit();
    }
}
