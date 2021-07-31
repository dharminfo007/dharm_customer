package in.app.dharm.info.online.shopping.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.app.dharm.info.online.shopping.model.CartProductListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class DataProcessor {


    private static Context context;

    public DataProcessor(Context context){
        this.context = context;
    }

    public final static String PREFS_NAME = "dharm_prefs";

    public boolean sharedPreferenceExist(String key)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        if(!prefs.contains(key)){
            return true;
        }
        else {
            return false;
        }
    }

    public static void setInt( String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public static void setStr(String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key,"DNF");
    }

    public static void setBool(String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBool(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key,false);
    }

    public void saveArrayList(ArrayList<CartProductListPojo> list, String key){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<CartProductListPojo> getArrayList(String key){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<CartProductListPojo>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveFavoriteArrayList(ArrayList<ProductListPojo> list, String key){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }

    public ArrayList<ProductListPojo> getFavoriteArrayList(String key){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<ProductListPojo>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void removeFromFavArrayList(ArrayList<ProductListPojo> list, String key, ProductListPojo product){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        list.remove(product);
        saveFavoriteArrayList(list, "favorite");
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
        editor.commit();

    }

}