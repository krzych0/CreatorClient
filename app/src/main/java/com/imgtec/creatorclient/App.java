package com.imgtec.creatorclient;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.imgtec.di.HasComponent;

/**
 *
 */
public class App extends Application implements HasComponent<ApplicationComponent> {

  private ApplicationComponent component;

  @Override
  public void onCreate() {
    super.onCreate();

    component = ApplicationComponent.Initializer.init(this);
    component.inject(this);

    if (BuildConfig.DEBUG) {
      Stetho.initialize(
          Stetho.newInitializerBuilder(this)
              .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
              .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
              .build());
    }
  }

  @Override
  public ApplicationComponent getComponent() {
    return component;
  }
}
