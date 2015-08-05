package uambition.ares.ywq.uambition.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ares on 15/7/24.
 */
public class ToastUtil {


    public static  void showMessage(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }


}
