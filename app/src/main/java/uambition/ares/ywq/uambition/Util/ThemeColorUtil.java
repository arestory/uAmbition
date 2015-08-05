package uambition.ares.ywq.uambition.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ares on 15/7/28.
 */
public class ThemeColorUtil {






    /**
     * 保存数据
     *
     * @param context
     * @param preferencesName
     * @param key
     * @param value
     */
    public static void saveColor(Context context, String preferencesName,
                                 String key, String value) {
        try{
            SharedPreferences MyPreferences = context.getSharedPreferences(
                    preferencesName, Activity.MODE_PRIVATE);
            if(MyPreferences!=null){
                SharedPreferences.Editor editor = MyPreferences.edit();
                editor.putString(key, value);

                editor.commit();
            }
        }catch (NullPointerException e) {
            // TODO: handle exception
        }


    }




    /**
     * 取出数据
     *
     * @param context
     * @param preferencesName
     * @param key
     * @return
     */
    public static String getData(Context context, String preferencesName,
                                 String key) {
        if (context == null) {
            return "";
        }
        SharedPreferences preferences = context.getSharedPreferences(
                preferencesName, Activity.MODE_PRIVATE);
        return preferences.getString(key, "");
    }
    /**
     * 清除数据
     *
     * @param context
     *
     */
    public static void clearData(Context context
    ) {
        SharedPreferences MyPreferences = context.getSharedPreferences(
                "ambition", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = MyPreferences.edit();
        editor.clear();
        editor.commit();
    }
    /**
     * 取出颜色
     *
     * @param context
     * @return
     */
    public static String getThemeColor(Context context) {
        if (context == null) {
            return "";
        }
        SharedPreferences preferences = context.getSharedPreferences(
                "ambition", Activity.MODE_PRIVATE);
        return preferences.getString("theme_color", "");
    }

}
