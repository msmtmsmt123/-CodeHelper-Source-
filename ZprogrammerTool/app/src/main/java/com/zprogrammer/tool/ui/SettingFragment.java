package com.zprogrammer.tool.ui;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.zprogrammer.tool.MyApplication;
import com.zprogrammer.tool.R;
import com.zprogrammer.tool.bean.Feedback;

import cn.bmob.v3.listener.SaveListener;
import de.psdev.licensesdialog.LicensesDialog;

public class SettingFragment extends PreferenceFragment implements OnPreferenceChangeListener {

    //设置界面
    private CheckBoxPreference update;
    private EditTextPreference feedback;
    private MyApplication me;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        init();
    }

    private void init() {
        me = (MyApplication) getActivity().getApplication();
        update = (CheckBoxPreference) findPreference("update");
        feedback = (EditTextPreference) findPreference("feedback");
        update.setOnPreferenceChangeListener(this);
        feedback.setOnPreferenceChangeListener(this);
        findPreference("notices").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new LicensesDialog.Builder(getActivity()).setNotices(R.raw.notices).setIncludeOwnLicense(true).build().show();
                return true;
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference p1, Object p2) {
        switch (p1.getKey()) {
            case "update":
                if (me.getBoolean("update", true)) {
                    update.setChecked(false);
                } else {
                    update.setChecked(true);
                }
                break;
            case "feedback":
                feedback("feedback", p2.toString());
                break;
        }
        return false;
    }

    private void feedback(String Data, String Message) {
        Feedback bmobdata = new Feedback();
        bmobdata.setfeedback(Message);
        bmobdata.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                me.showToast("反馈成功!");
            }

            @Override
            public void onFailure(int code, String arg0) {
                me.showToast("反馈失败:" + arg0);
            }
        });
    }
}
