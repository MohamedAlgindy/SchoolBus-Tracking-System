package com.example.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {
    private static SharedPreferences.Editor editor;
    private static Context _context;
    private static SharedPreferences mPreferences;
    private static final String PREF_NAME = "Tracking";
    private static MySharedPreference uniqInstance;
    private static final int PRIVATE_MODE = 0;

    /**
     * Private Constructor for not allowing other classes to instantiate this
     * class
     */
    private MySharedPreference() {

    }

    /**
     * @param context of the class calling this method
     * @return instance of this class This method is the global point of access
     * for getting the only one instance of this class
     */
    public static synchronized MySharedPreference getInstance(Context context) {
        if (uniqInstance == null) {
            _context = context;
            mPreferences = _context.getSharedPreferences(PREF_NAME,
                    PRIVATE_MODE);
            editor = mPreferences.edit();
            uniqInstance = new MySharedPreference();
        }
        return uniqInstance;
    }

    /**
     * Method is used for reset all the key & values of the shared preference.
     */
    public void resetAll() {
        editor.clear();
        editor.commit();
    }


    public void setUserLoginType(int navigationType) {
        editor.putInt("loginType", navigationType);
        editor.commit();
    }

    public int getUserLoginType() {
        return mPreferences.getInt("loginType",0);
    }



    public void setUserId(String userId) {
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getUserId() {
        return mPreferences.getString("userId","");
    }


}
