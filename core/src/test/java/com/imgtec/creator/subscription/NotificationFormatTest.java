package com.imgtec.creator.subscription;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.BaseTest;
import com.imgtec.creator.MockResponseProvider;
import com.imgtec.creator.pojo.Notifications;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Unit test for notifications format.
 */
@RunWith(MockitoJUnitRunner.class)
public class NotificationFormatTest extends BaseTest {

  @Test
  public void testNotificationFormat() throws Exception {

    final String notificationBody = MockResponseProvider
        .DATA_PROVIDER.getFile("temperature_change_mock_response.json");

    try {
      Notifications<DummyValue> notifications = new GsonBuilder().create().fromJson(notificationBody,
          new TypeToken<Notifications<DummyValue>>() {
          }.getType());
      assertNotNull(notifications);

    } catch (Exception e) {
      fail();
    }

  }
}
