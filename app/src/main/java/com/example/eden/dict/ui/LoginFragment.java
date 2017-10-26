package com.example.eden.dict.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden.dict.R;
import com.example.eden.dict.utils.NetWorkUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {
    private Button registerButton;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private TextView usernameTips;
    private TextView passwordTips;
    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String response;
    private String usernameWaitForPost;
    private String passwordWaitForPost;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View loginLayout = inflater.inflate(R.layout.fragment_login, container, false);
        return loginLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        hidePassword();
        cancelKeyboard();
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        rememberPass = (CheckBox) getActivity().findViewById(R.id.rememberPassword);
        boolean isRemember = pref.getBoolean("remember_password", false);

        if (isRemember) {
            String account_setRemember = pref.getString("account", "");
            String password_setRemember = pref.getString("password", "");
            username.setText(account_setRemember);
            password.setText(password_setRemember);
            rememberPass.setChecked(true);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUsernameContext = username.getText().toString();
                String getPassWordContext = password.getText().toString();
                cleanUsernameTips();
                cleanPasswordTips();
                setRememberPassword(getUsernameContext, getPassWordContext);
                if (!checkAccountMark(getUsernameContext)) {
                    usernameTips.setText("请输入正确的用户名");
                }
                if (!checkPassword(getPassWordContext)) {
                    passwordTips.setText("请输入正确的密码");
                }
                if (getUsernameContext.length() == 0 && getPassWordContext.length() == 0) {
                    setUsernameTips();
                    setPasswordTips();
                }
                if (getUsernameContext.length() == 0 && getPassWordContext.length() != 0) {
                    cleanPasswordTips();
                    setUsernameTips();
                }
                if (getUsernameContext.length() != 0 && getPassWordContext.length() == 0) {
                    cleanUsernameTips();
                    setPasswordTips();
                }
                postDataToServer(getUsernameContext, getPassWordContext);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FragmentManager fragmentManager = getFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.contentContainer, new RegisterFragment()).commit();
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

    }


    private void initView() {
        registerButton = (Button) getActivity().findViewById(R.id.registerButton_loginLayout);
        loginButton = (Button) getActivity().findViewById(R.id.loginButton_loginLayout);
        username = (EditText) getActivity().findViewById(R.id.et_username);
        password = (EditText) getActivity().findViewById(R.id.et_password);
        usernameTips = (TextView) getActivity().findViewById(R.id.usernameTips_loginLayout);
        passwordTips = (TextView) getActivity().findViewById(R.id.passwordTips_loginLayout);
    }

    private void setUsernameTips() {
        usernameTips.setText("用户名不能为空");
    }

    private void cleanUsernameTips() {
        usernameTips.setText("");
    }

    private void setPasswordTips() {
        passwordTips.setText("密码不能为空");
    }

    private void cleanPasswordTips() {
        passwordTips.setText("");
    }

    private boolean checkAccountMark(String account) {
        String all = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        Pattern pattern = Pattern.compile(all);
        return pattern.matches(all, account);
    }

    private boolean checkPassword(String psd) {
        Pattern p = Pattern
                .compile("^[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher m = p.matcher(psd);
        return m.matches();
    }

    private void hidePassword() {
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    //读取saveRegisterInformationByPreferences中的信息
    private boolean readPersonalInformationByPreferences(String username, String password) {
        SharedPreferences pref = getActivity().getSharedPreferences("PersonalInformation", Context.MODE_PRIVATE);
        String usernameFromPreferences = pref.getString("Username", "");
        String passwordFromPreferences = pref.getString("Password", "");
        if (usernameFromPreferences.equals(username) && passwordFromPreferences.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    private void setRememberPassword(String getAccountText, String getPasswordText) {
        editor = pref.edit();
        if (rememberPass.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("account", getAccountText);
            editor.putString("password", getPasswordText);
        } else {
            editor.clear();
        }
        editor.apply();
    }

    private void postDataToServer(String username, String password) {
        usernameWaitForPost = username;
        passwordWaitForPost = password;
        new PostAsyncTask().execute();
    }

    private void cancelKeyboard() {
        getActivity().findViewById(R.id.loginLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    class PostAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                NetWorkUtils netWorkUtils = new NetWorkUtils();
                String url = "http://118.89.166.247:7000/UserInfo/LoginPage/";
                String json = netWorkUtils.bowlingJson(usernameWaitForPost, passwordWaitForPost, "");
                response = netWorkUtils.post(url, json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                if (response.compareTo("Success") == 0) {
                    Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                    setRememberPassword(usernameWaitForPost, passwordWaitForPost);
                }
                if (response.compareTo("Failed") == 0) {
                    Toast.makeText(getActivity(), "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
