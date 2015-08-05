package uambition.ares.ywq.uambition.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import uambition.ares.ywq.uambition.Util.Logs;

/**
 * Created by ares on 15/7/22.
 */
public class PersonFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toast.makeText(getActivity(),"aaaa",Toast.LENGTH_LONG).show();;
        TextView textView=new TextView(getActivity());

        return textView;
    }
}
