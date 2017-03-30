package com.imgtec.creator.authenticator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AuthenticatorActivity extends AppCompatActivity {

  public static final String PARAM_AUTHTOKEN_TYPE = "AUTH_TOKEN_TYPE";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authenticator);
  }
}
