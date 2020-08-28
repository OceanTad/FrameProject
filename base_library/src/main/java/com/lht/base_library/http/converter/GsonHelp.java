package com.lht.base_library.http.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelp {

    private static volatile GsonHelp instance;

    private GsonHelp() {

    }

    public static GsonHelp getInstance() {
        if (instance == null) {
            synchronized (GsonHelp.class) {
                if (instance == null) {
                    instance = new GsonHelp();
                }
            }
        }
        return instance;
    }

    private Gson gson;

    public Gson createGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(String.class, new StringConverter())
                    .registerTypeAdapter(int.class, new IntegerConverter())
                    .registerTypeAdapter(Integer.class, new IntegerConverter())
                    .create();
        }
        return gson;
    }

}
