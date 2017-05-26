package com.mdc.dktran.demogoogleplus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {
    private static final int CODE_SIGN_IN = 123;
    private Button btnLogin, btnSignOut;
    private TextView tvUserName;
    private GoogleApiClient my_googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
    }

    private void initViews() {
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnSignOut = (Button)findViewById(R.id.btn_sign_out);
        tvUserName = (TextView)findViewById(R.id.tv_name_user);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        my_googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        btnLogin.setOnClickListener(singInOut);
        btnSignOut.setOnClickListener(singInOut);
    }

    private View.OnClickListener singInOut = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_login:
                    Intent signIn = Auth.GoogleSignInApi.getSignInIntent(my_googleApiClient);
                    startActivityForResult(signIn,CODE_SIGN_IN);
                    break;
                case R.id.btn_sign_out:
                    Auth.GoogleSignInApi.signOut(my_googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            tvUserName.setText("User has been sign out!");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.i("login:::: ","Click login button");
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            tvUserName.setText(account.getDisplayName());
        }
        else {
            Toast.makeText(this,"Login error",Toast.LENGTH_LONG).show();
        }


    }
}
