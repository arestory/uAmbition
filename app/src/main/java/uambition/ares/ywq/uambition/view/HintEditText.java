package uambition.ares.ywq.uambition.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import uambition.ares.ywq.uambition.R;

/**
 * Created by ares on 15/8/11.
 */
public class HintEditText extends LinearLayout implements TextWatcher,View.OnFocusChangeListener{

    private EditText editText;
    private TextView textView;
    private Context context;

    private  String doing;
    public HintEditText(Context context) {
        super(context);
        //initView(context);



    }

    /**
     * 设置提示信息
     */
    public void setHint(String hint){
        editText.setHint(hint);

    }
    /**
     * 设置提示信息
     */
    public void setTitle(String hint){
       // editText.setHint(hint);
        textView.setText(hint);

    }
    private void initView(Context context){
        this.context=context;
        this.setOrientation(LinearLayout.VERTICAL);
        textView=new TextView(context);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);


        //默认隐藏
        textView.setVisibility(View.GONE);
        this.addView(textView);
        editText=new EditText(context);

        textView.setPadding(10, 0, 0, 0);
        LinearLayout.LayoutParams params2 =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params2);
        this.addView(editText);


        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);


    }

    public String getTextString(){

        return editText.getText().toString();
    }


    public HintEditText(Context context, AttributeSet attrs) {
       this(context, attrs, 0);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.HintEditText);

        typedArray.recycle();
        initView(context);
    }

    public HintEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       // initView(context);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(s.length()==0){

            textView.setVisibility(View.GONE);
            doing=null;

        }else {
            if (doing==null){

//                doing= YoYo.with(Techniques.RollIn).interpolate(new AccelerateDecelerateInterpolator()
//                ).duration(1000).playOn(textView);
                textView.setVisibility(View.VISIBLE);
                textView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fab_out));
                doing="";
            }


        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus){
            //textView.setVisibility(View.VISIBLE);
        }


    }
}
