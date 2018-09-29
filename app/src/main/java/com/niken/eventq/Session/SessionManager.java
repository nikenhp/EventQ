package com.niken.eventq.Session;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    android.content.SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "UserPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_USER = "IsLoggedUser";
    public static final String KEY_ID = "id";
    public static final String KEY_NAMAUSER = "nama";
    public static final String KEY_EMAILUSER = "email";
    public static final String KEY_ALAMATUSER = "adress";

    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void CreateLoginSession(int id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID, id);
        editor.commit();
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAMAUSER,pref.getString(KEY_NAMAUSER,null));
        user.put(KEY_EMAILUSER,pref.getString(KEY_EMAILUSER,null));
        user.put(KEY_ALAMATUSER,pref.getString(KEY_ALAMATUSER,null));
        user.put(KEY_ID,String.valueOf(pref.getInt(KEY_ID,0)));
        return user;
    }

    public void CreateUser(String namauser){
        editor.putBoolean(IS_USER,true);
        editor.putString(KEY_NAMAUSER,namauser);
        editor.commit();
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public boolean IsLoggedUser(){
        return pref.getBoolean(IS_USER, false);
    }
    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }

}