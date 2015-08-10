package uambition.ares.ywq.uambition.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import uambition.ares.ywq.uambition.Activity.MainActivity;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.bean.Feedback;
import uambition.ares.ywq.uambition.bean.User;

/**
 * Created by ares on 15/8/1.
 */
public class FeedbackFragment extends Fragment {


    private EditText feedbackET;
    private Button commitBtn;
    private User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_feedback, container, false);

        user= BmobUser.getCurrentUser(getActivity(),User.class);
        initView(view);
        return view;
    }


    public void initView(View v ){
        final MainActivity activity = (MainActivity)getActivity();
        feedbackET=(EditText)v.findViewById(R.id.feedback_content);
        commitBtn=(Button)v.findViewById(R.id.commit);
        commitBtn.setBackgroundColor(activity.getThemeColor());
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在提交反馈");
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Feedback  feedback=new Feedback();
                String content = feedbackET.getText().toString();
                if(TextUtils.isEmpty(content)){
                    ToastUtil.showMessage(getActivity(),"说点建议嘛");
                    return;
                }else{
                    dialog.show();;
                    feedback.setContent(content);
                    feedback.setFeedbacker(user);
                    feedback.save(getActivity(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            dialog.dismiss();

                            feedbackET.setText("");
                            JSONObject jsonObject = new JSONObject();
                            try {

                                jsonObject.put("feedbacker",user.getNickName());
                                jsonObject.put("feedback",feedback.getContent());
                                jsonObject.put("type","feedback");
                                final BmobPushManager bmobPushManager = new BmobPushManager(activity);

                                BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
                                query.addWhereEqualTo("receiverId","50b61f1075");
                                bmobPushManager.setQuery(query);

                                bmobPushManager.pushMessage(jsonObject, new PushListener() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                          }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ToastUtil.showMessage(getActivity(), "提交成功，感谢您的宝贵建议。");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ToastUtil.showMessage(getActivity(),"提交失败，请检查网络");
                            dialog.dismiss();
                        }
                    });
                }




            }
        });





    }
}
