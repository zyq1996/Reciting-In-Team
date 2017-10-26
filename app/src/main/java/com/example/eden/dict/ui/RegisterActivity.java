package com.example.eden.dict.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;


import com.example.eden.dict.R;
import com.example.eden.dict.utils.NetWorkUtils;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class RegisterActivity extends SwipeBackActivity {
    private EditText editTextUserName;
    private EditText editTextPassWord;
    private EditText editTextPhoneNumber;
    private Button registerButton;
    private TextView usernameTips;
    private TextView passwordTips;
    private TextView phonenumberTips;
    //post data to server
    private String usernameWaitForPost;
    private String passwordWaitForPost;
    private String phonenumberWaitForPost;
    private String response;

    private SwipeBackLayout mSwipeBackLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mSwipeBackLayout = getSwipeBackLayout();
        initView();
        hidePassword();
        cancelKeyboard();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUserName.getText().toString();
                String password = editTextPassWord.getText().toString();
                String phonenumber = editTextPhoneNumber.getText().toString();
                cleanUsernameTips();
                cleanPasswordTips();
                cleanPhoneNumberTips();
                checkRegisterInformation(username, password, phonenumber);
                if (username.length() == 0) {
                    setUsernameTips("用户名不能为空");
                }
                if (password.length() == 0) {
                    setPasswordTips("密码不能为空");
                }
                if (phonenumber.length() == 0) {
                    setPhoneNumberTips("手机号不能为空");
                }
                postDataToServer(username, password, phonenumber);
            }
        });
    }

    private void initView() {
        editTextUserName = (EditText) findViewById(R.id.et_registerUserName);
        editTextPassWord = (EditText) findViewById(R.id.et_registerPassword);
        editTextPhoneNumber = (EditText) findViewById(R.id.et_registerPhoneNumber);
        registerButton = (Button) findViewById(R.id.registerButton_registerLayout);
        usernameTips = (TextView) findViewById(R.id.usernameTips_registerLayout);
        passwordTips = (TextView) findViewById(R.id.passwordTips_registerLayout);
        phonenumberTips = (TextView) findViewById(R.id.phonenumberTips_registerLayout);
    }

    private void cleanUsernameTips() {
        usernameTips.setText("");
    }

    private void cleanPasswordTips() {
        passwordTips.setText("");
    }

    private void cleanPhoneNumberTips() {
        phonenumberTips.setText("");
    }

    private void setUsernameTips(String usernameTipsText) {
        usernameTips.setText(usernameTipsText);
    }

    private void setPasswordTips(String passwordTipsText) {
        passwordTips.setText(passwordTipsText);
    }

    private void setPhoneNumberTips(String phoneNumberTipsText) {
        phonenumberTips.setText(phoneNumberTipsText);
    }

    private static boolean checkUsernameMark(String account) {
        String all = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        Pattern pattern = Pattern.compile(all);
        return pattern.matches(all, account);
    }

    private static boolean checkPassword(String password) {
        Pattern p = Pattern
                .compile("^[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    private static boolean checkPhonenumber(String phonenumber) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    private void hidePassword() {
        editTextPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void checkUsernameInformation(String usernameText) {
        if (!checkUsernameMark(usernameText)) {
            setUsernameTips("用户名只能包含中文、数字、字母");
        } else if (usernameText.length() < 6 && usernameText.length() > 0 || usernameText.length() > 16) {
            setUsernameTips("用户名不符合要求(6-16字符)");
        }
    }

    private void checkPasswordInformation(String passwordText) {
        if (!checkPassword(passwordText)) {
            setPasswordTips("密码过于简单");
        } else if (passwordText.length() < 6 && passwordText.length() > 0 || passwordText.length() > 16) {
            setPasswordTips("密码不符合要求(6-16字符)");
        }
    }

    private void checkPhonenumberInformation(String phonenumberText) {
        if (!checkPhonenumber(phonenumberText) || phonenumberText.length() != 11) {
            setPhoneNumberTips("请输入正确的手机号");
        }
    }

    private void checkRegisterInformation(String usernameText, String passwordText, String phonenumberText) {
        checkUsernameInformation(usernameText);
        checkPasswordInformation(passwordText);
        checkPhonenumberInformation(phonenumberText);
    }

    private void saveRegisterInformationByPreferences(String username, String password, String phonenumber) {
        SharedPreferences.Editor editor = getSharedPreferences("PersonalInformation", Context.MODE_PRIVATE).edit();
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.putString("Phonenumber", phonenumber);
        editor.apply();
    }

    private void postDataToServer(String username, String password, String phonenumber) {
        usernameWaitForPost = username;
        passwordWaitForPost = password;
        phonenumberWaitForPost = phonenumber;
        new PostAsyncTask().execute();
    }

    private void cancelKeyboard() {
        findViewById(R.id.registerLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    class PostAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                NetWorkUtils netWorkUtils = new NetWorkUtils();
                String url = "http://118.89.166.247:7000/UserInfo/RegisterPage/";
                String json = netWorkUtils.bowlingJson(usernameWaitForPost, passwordWaitForPost, phonenumberWaitForPost);
                response = netWorkUtils.post(url, json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.length() > 0) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                if (response.compareTo("Success") == 0) {

                    if (usernameTips.length() == 0 && passwordTips.length() == 0 && phonenumberTips.length() == 0) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        saveRegisterInformationByPreferences(usernameWaitForPost, passwordWaitForPost, phonenumberWaitForPost);
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "请完善个人信息", Toast.LENGTH_SHORT).show();
                    }
                }
                if (response.compareTo("UsernameIsRegistered") == 0) {
                    setUsernameTips("用户名已被注册");
                }
                if (response.compareTo("PhonenumberIsRegistered") == 0) {
                    setPhoneNumberTips("手机号已被注册");
                }
            } else {
                Toast.makeText(RegisterActivity.this, "请检查网络连接", Toast.LENGTH_LONG).show();
            }
        }
    }
}
