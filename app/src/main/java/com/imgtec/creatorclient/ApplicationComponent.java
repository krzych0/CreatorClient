package com.imgtec.creatorclient;

import android.content.SharedPreferences;
import android.os.Handler;

import com.imgtec.di.PerApp;

import javax.inject.Named;

import dagger.Component;
import okhttp3.OkHttpClient;

@PerApp
@Component(
    modules = {
        ApplicationModule.class
    }
)
public interface ApplicationComponent {

  final class Initializer {

    private Initializer() {}

    static ApplicationComponent init(App application) {
      return DaggerApplicationComponent
          .builder()
          .applicationModule(new ApplicationModule(application))
          .build();
    }
  }

  App inject(App app);

  @Named("Main") Handler getHandler();

}

