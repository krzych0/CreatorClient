package com.imgtec.creatorclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.CreatorClient;
import com.imgtec.creator.auth.AuthTokenProvider;
import com.imgtec.creator.pojo.AccessKey;
import com.imgtec.creator.pojo.Api;
import com.imgtec.creator.pojo.Bootstrap;
import com.imgtec.creator.pojo.Client;
import com.imgtec.creator.pojo.Clients;
import com.imgtec.creator.pojo.Configuration;
import com.imgtec.creator.pojo.Identities;
import com.imgtec.creator.pojo.Instances;
import com.imgtec.creator.pojo.ObjectDefinitions;
import com.imgtec.creator.pojo.ObjectType;
import com.imgtec.creator.pojo.ObjectTypes;
import com.imgtec.creator.pojo.Versions;
import com.imgtec.creator.response.GsonResponseHandler;
import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.utils.GsonDeserializer;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();

        new Thread(new Runnable() {
          @Override
          public void run() {
            HttpUrl url = HttpUrl.parse("https://deviceserver.creatordev.io");
            AuthTokenProvider authTokenProvider = new AuthTokenProvider();
            ResponseHandler responseHandler = new GsonResponseHandler(new GsonDeserializer());

            CreatorClient client = new CreatorClient
                .Builder()
                .setUrl(url)
                .setTokenProvider(authTokenProvider)
                .setResponseHandler(responseHandler)
                .build();

            try {

              try {
                client.getAuthManager().authorize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJPcmdJRCI6MTE0LCJSVCI6IjEiLCJleHAiOjE0OTA4NjY5ODh9.xfNIMS5otLDz24gujE5v9h8ZntXVgZHHxhtItOQemLQ");

              } catch (IOException e) {
                e.printStackTrace();
              }

              Api api = client.get(url.toString(), Api.class);
              api.toString();

              Identities identities = client.get(api.getLinkByRel("identities").getHref(), Identities.class);
              Configuration cfg = client.get(api.getLinkByRel("configuration").getHref(), Configuration.class);
              Bootstrap bootstrap = client.get(cfg.getLinkByRel("bootstrap").getHref(), Bootstrap.class);
              Versions versions = client.get(api.getLinkByRel("versions").getHref(), Versions.class);

              authTokenProvider.getAuthToken().setAccessToken(authTokenProvider.getAuthToken().getAccessToken()+"1");
              Clients clients = client.get(api.getLinkByRel("clients").getHref(), Clients.class);
              clients.toString();

              Client temperatureClient = null;
              for (Client c: clients.getItems()) {
                if (c.getName().equals("TemperatureDeviceClient")) {
                  temperatureClient = c;
                  break;
                }
              }

              if (temperatureClient == null) {
                return;
              }

              ObjectTypes types =
                  client.get(temperatureClient.getLinkByRel("objecttypes").getHref(), ObjectTypes.class);
              ObjectType tempType = null;
              for (ObjectType t: types.getItems()) {
                if (t.getObjectTypeID().equals("3308")) {
                  tempType = t;
                  break;
                }
              }
              if (tempType == null) {
                return;
              }

              //Testing code showing usage of custom instance
              Instances<PointValue> instances = client.getInstances(tempType.getLinkByRel("instances").getHref(),
                  new TypeToken<Instances<PointValue>>(){});
              PointValue i = instances.getItems().get(0);
              i.setPointValue(10.1f);

              boolean status = client.put(i.getLinkByRel("update").getHref(), i);
              if(status);

              AccessKey k = new AccessKey();
              k.setName("MyNewAccessKey");
              AccessKey keys = client.post(api.getLinkByRel("accesskeys").getHref(), k, AccessKey.class);
              keys.toString();

              ObjectDefinitions definitions = client.get(api.getLinkByRel("objectdefinitions").getHref(),
                  ObjectDefinitions.class);
              definitions.getItems();

            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }).start();

      }
    });

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
