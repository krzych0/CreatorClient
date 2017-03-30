package com.imgtec.creator.authenticator.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 *
 */
public class AuthenticatorService extends Service {

  private AbstractAccountAuthenticator authenticator;

  @Nullable  @Override
  public IBinder onBind(Intent intent) {
    if (intent.getAction().equals(AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
      return getAuthenticator().getIBinder();
    }
    return null;
  }

  AbstractAccountAuthenticator getAuthenticator() {
    if (authenticator == null) {
      authenticator = new AccountAuthenticator(this);
    }
    return authenticator;
  }
}
