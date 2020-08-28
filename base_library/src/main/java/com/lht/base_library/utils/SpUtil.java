package com.lht.base_library.utils;

import android.content.SharedPreferences;

import com.lht.base_library.base.BaseSp;

import java.util.Map;

public class SpUtil {

    public boolean commit(String key, Object object) {
        SharedPreferences.Editor editor = BaseSp.getInstance().getSp().edit();
        save(editor, key, object);
        return editor.commit();
    }

    public void apply(String key, Object object) {
        SharedPreferences.Editor editor = BaseSp.getInstance().getSp().edit();
        save(editor, key, object);
        editor.apply();
    }

    private void save(SharedPreferences.Editor editor, String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        String value = defaultValue;
        value = BaseSp.getInstance().getSp().getString(key, defaultValue);
        return value;
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        return BaseSp.getInstance().getSp().getInt(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return BaseSp.getInstance().getSp().getBoolean(key, defaultValue);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return BaseSp.getInstance().getSp().getLong(key, 0);
    }

    public Boolean contain(String key) {
        return BaseSp.getInstance().getSp().contains(key);
    }

    public Map<String, ?> getAll() {
        return BaseSp.getInstance().getSp().getAll();
    }

}
