package beacons.leto.com.letoibeacons.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orm.SugarRecord;

import java.io.UnsupportedEncodingException;

import beacons.leto.com.letoibeacons.core.AppPreferences;
import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;
import beacons.leto.com.letoibeacons.models.TogglUser;
import beacons.leto.com.letoibeacons.utils.MyUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "LOGIN";


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private FrameLayout mProgressView;
    private View mLoginFormView;
    Button mEmailSignInButton;
    CheckBox mShowCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    // Store values at the time of the login attempt.
                    String email = mEmailView.getText().toString();
                    String password = mPasswordView.getText().toString();
                    attemptLogin(email,password,false);
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                attemptLogin(email,password,false);
            }
        });
        MyUtils.applyRoundedCorners(mEmailSignInButton);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = (FrameLayout) findViewById(R.id.login_progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        if (TogglUser.first(TogglUser.class)!=null){
            launchMainActivity(false);
        }

        mShowCheckbox = (CheckBox) findViewById(R.id.show_checkBox);
        mShowCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    mPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        findViewById(R.id.googleBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(LoginActivity.this)
                        .title(R.string.action_sign_in_short)
                        .content(R.string.token_message)
                        .negativeText(R.string.cancel)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                               attemptLogin(input.toString(),"api_token", true);
                            }
                        }).show();
            }
        });
    }


    private void disableSignInButton(){
        mEmailSignInButton.setEnabled(false);
        mEmailSignInButton.setBackgroundColor(ContextCompat.getColor(this,R.color.light_grey_text_color));
    }

    private void enableSignInButton(){
        mEmailSignInButton.setEnabled(true);
        mEmailSignInButton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String email, String password, Boolean usingAPIToken) {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (!usingAPIToken) {
            // Check for a valid password, if the user entered one.
            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid email address.
            if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!isEmailValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            try {
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                String stringToEncode = email+":"+password;
                byte[] data = stringToEncode.getBytes("UTF-8");
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                AppPreferences.setAuthToken("Basic " + base64);

                disableSignInButton();
                LetoTogglRestClient.getPureApi().signin(new Callback<TogglUser>() {
                    @Override
                    public void success(TogglUser togglUser, Response response) {
                        Log.d(TAG, "SUCCESS: Logged in as: " + togglUser.getData().getFullname());
                        SugarRecord.save(togglUser.getData());
                        togglUser.save();
                        String stringToEncode = togglUser.getData().getApiToken()+":api_token";
                        byte[] data = new byte[0];
                        try {
                            data = stringToEncode.getBytes("UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                        AppPreferences.setAuthToken("Basic " + base64);
                        launchMainActivity(true);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "ERROR");
                        AppPreferences.setAuthToken("");
                        mProgressView.setVisibility(View.GONE);
                        enableSignInButton();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_sign_in), Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.flat_red));
                        snackbar.show();
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchMainActivity(boolean animate){
        Intent intent = new Intent(this, MainActivity.class);
        if (!animate){
            overridePendingTransition(0, 0);
        }
        startActivity(intent);
        if (!animate){
            overridePendingTransition(0,0);
        }
        finish();
    }

    private boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 1;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}

