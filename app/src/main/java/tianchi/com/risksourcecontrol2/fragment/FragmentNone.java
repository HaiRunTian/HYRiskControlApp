package tianchi.com.risksourcecontrol2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tianchi.com.risksourcecontrol2.R;

/**
 * Created by Kevin on 2018/3/22.
 */

public class FragmentNone extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_none, container, false);
    }
}
