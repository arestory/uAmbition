package uambition.ares.ywq.uambition.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

/**
 * Created by ares on 15/8/14.
 */
public class AmbitionDialog extends Dialog {


    public AmbitionDialog(Context context) {

        super(context, android.R.style.Theme );
        setOwnerActivity((Activity) context);
    }

    public AmbitionDialog(Context context, int theme) {
        super(context, theme);
        setOwnerActivity((Activity) context);
    }

    protected AmbitionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
