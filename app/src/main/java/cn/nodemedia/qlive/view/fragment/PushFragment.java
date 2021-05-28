package cn.nodemedia.qlive.view.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import cn.nodemedia.qlive.MyApplication;
import cn.nodemedia.qlive.R;

import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.BaseRequest;
import cn.nodemedia.qlive.view.CreateRoomActivity;
import cn.nodemedia.qlive.view.MyRequest.GetSingleUserInfoRequest;
import cn.nodemedia.qlive.view.PushActivity;


public class PushFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.pref_push,null);
        GetSingleUserInfoRequest getSingleUserInfoRequest=new GetSingleUserInfoRequest();
        GetSingleUserInfoRequest.getUserParam param=new GetSingleUserInfoRequest.getUserParam();
        param.userId="123";
        getSingleUserInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<UserInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(getActivity(), "获取用户信息失败！"+msg, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(UserInfo userInfo) {
                Toast.makeText(getActivity(), "获取用户信息成功！", Toast.LENGTH_SHORT).show();
                MyApplication.getApplication().setSelfProfile(userInfo);

            }
        });
        getSingleUserInfoRequest.request(param);

        Intent intent=new Intent(getActivity(), CreateRoomActivity.class);
        intent.putExtra("userId",123);
        intent.putExtra("userName","哈哈哈浩");
        intent.putExtra("userAvatar","http://qsz313e9w.hn-bkt.clouddn.com/image/user.png");
        Button push_btn=view.findViewById(R.id.push_btn);
        push_btn.setOnClickListener(v -> startActivity(intent));
        return view;
    }


    //    private SharedPreferences sp;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
//        addPreferencesFromResource(R.xml.pref_push);
//    }
//
//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//
//    }
//
//    @Override
//    public void onViewCreated(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//
//
//    }
//
//    private void initPreference(String key, String defValue, boolean isListener) {
//        Preference preference = findPreference(key);
//        if (preference instanceof ListPreference) {
//            ListPreference listPreference = (ListPreference) preference;
//            String stringValue = sp.getString(key, defValue);
//            setListSummary(listPreference, stringValue);
//        } else if (preference instanceof SwitchPreference) {
//            SwitchPreference switchPreference = (SwitchPreference) preference;
//            boolean defBooleanValue = Boolean.parseBoolean(defValue);
//            boolean booleanValue = sp.getBoolean(key, defBooleanValue);
//            setSwitchChecked(switchPreference, booleanValue);
//        } else if (preference instanceof EditTextPreference) {
//            EditTextPreference editTextPreference = (EditTextPreference) preference;
//            String stringValue = sp.getString(key, defValue);
//            setEditSummary(editTextPreference, stringValue);
//        }
//        if (isListener) {
//            preference.setOnPreferenceChangeListener(this);
//        }
//    }
//
//    @Override
//    public boolean onPreferenceChange(Preference preference, Object value) {
//        Log.e("preference.getKey():" + preference.getKey() + "  value:" + value);
//        String stringValue = value.toString();
//        if (preference instanceof ListPreference) {
//            ListPreference listPreference = (ListPreference) preference;
//            setListSummary(listPreference, stringValue);
//        } else if (preference instanceof EditTextPreference && !preference.getKey().equals("push_stream_url")) {
//            preference.setSummary(stringValue);
//        }else if(preference.getKey().equals("push_stream_url")){
//            preference.setSummary(stringValue);
//        }
//        return true;
//    }
//
//    private void setListSummary(ListPreference listPreference, String stringValue) {
//        int index = listPreference.findIndexOfValue(stringValue);
//        listPreference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
//    }
//
//    private void setSwitchChecked(SwitchPreference switchPreference, boolean booleanValue) {
//        switchPreference.setChecked(booleanValue);
//    }
//
//    private void setEditSummary(EditTextPreference editTextPreference, String stringValue) {
//        editTextPreference.setSummary(stringValue);
//    }
}
