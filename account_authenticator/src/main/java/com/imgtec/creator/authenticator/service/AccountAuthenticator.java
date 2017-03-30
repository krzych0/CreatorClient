package com.imgtec.creator.authenticator.service;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.imgtec.creator.authenticator.AuthenticatorActivity;

/**
 *
 */
class AccountAuthenticator extends android.accounts.AbstractAccountAuthenticator {

  private final Context context;

  public AccountAuthenticator(Context context) {
    super(context);
    this.context = context;
  }

  @Override
  public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
    return null;
  }

  @Override
  public Bundle addAccount(AccountAuthenticatorResponse response,
                           String accountType,
                           String authTokenType,
                           String[] requiredFeatures,
                           Bundle options) throws NetworkErrorException {

    final Intent intent = new Intent(context, AuthenticatorActivity.class);
    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
    intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
    final Bundle bundle = new Bundle();
    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
    return bundle;
  }

  @Override
  public Bundle confirmCredentials(AccountAuthenticatorResponse response,
                                   Account account,
                                   Bundle options) throws NetworkErrorException {
    return null;
  }

  @Override
  public Bundle getAuthToken(AccountAuthenticatorResponse response,
                             Account account,
                             String authTokenType,
                             Bundle options) throws NetworkErrorException {
    return null;
  }

  @Override
  public String getAuthTokenLabel(String authTokenType) {
    return null;
  }

  @Override
  public Bundle updateCredentials(AccountAuthenticatorResponse response,
                                  Account account, String authTokenType,
                                  Bundle options) throws NetworkErrorException {
    return null;
  }

  @Override
  public Bundle hasFeatures(AccountAuthenticatorResponse response,
                            Account account,
                            String[] features) throws NetworkErrorException {
    return null;
  }
}
