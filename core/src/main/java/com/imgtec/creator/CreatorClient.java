package com.imgtec.creator;


import com.google.gson.reflect.TypeToken;
import com.imgtec.creator.auth.AuthInterceptor;
import com.imgtec.creator.auth.AuthManager;
import com.imgtec.creator.auth.AuthManagerImpl;
import com.imgtec.creator.auth.AuthTokenProvider;
import com.imgtec.creator.navigation.Navigator;
import com.imgtec.creator.navigation.NavigatorImpl;
import com.imgtec.creator.pojo.Hateoas;
import com.imgtec.creator.pojo.Instances;
import com.imgtec.creator.request.DeleteRequest;
import com.imgtec.creator.request.GenericRequest;
import com.imgtec.creator.request.GetRequest;
import com.imgtec.creator.request.InstancesRequest;
import com.imgtec.creator.request.PostRequest;
import com.imgtec.creator.request.PutRequest;
import com.imgtec.creator.response.GsonResponseHandler;
import com.imgtec.creator.response.ResponseHandler;
import com.imgtec.creator.subscription.SubscriptionsManager;
import com.imgtec.creator.subscription.SubscriptionsManagerImpl;
import com.imgtec.creator.utils.GsonDeserializer;
import com.imgtec.creator.utils.GsonSerializer;
import com.imgtec.creator.utils.Serializer;
import com.imgtec.creator.utils.Verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * CreatorClient is a utility-type library designed to help interacting with Device Server.
 */
public class CreatorClient {

  private static final Logger logger = LoggerFactory.getLogger(CreatorClient.class.getSimpleName());
  private final HttpUrl url;
  private final Serializer requestSerializer;
  private final ResponseHandler responseHandler;
  private final AuthTokenProvider tokenProvider;

  private final AuthManagerImpl authManager;
  private final OkHttpClient okHttpClient;
  private final SubscriptionsManager subscriptionsManager;
  private final X509TrustManager trustManager;
  private final SSLSocketFactory sslSocketFactory;


  CreatorClient(final Builder builder) {
    super();

    this.url = builder.getUrl();
    this.requestSerializer = builder.getRequestSerializer();
    this.responseHandler = builder.getResponseHandler();
    this.tokenProvider = builder.getTokenProvider();
    this.authManager = new AuthManagerImpl(this);
    this.subscriptionsManager = new SubscriptionsManagerImpl(this);

    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

    if (builder.getCache() != null) {
      okBuilder.cache(builder.getCache());
    }

    okBuilder.addInterceptor(new AuthInterceptor(tokenProvider));
    okBuilder.authenticator(this.authManager);

    if (builder.getSslSocketFactory() != null && builder.getTrustManager() != null) {
      trustManager = builder.getTrustManager();
      sslSocketFactory = builder.getSslSocketFactory();
      okBuilder.sslSocketFactory(builder.getSslSocketFactory(), builder.getTrustManager());
    } else {
      sslSocketFactory = null;
      trustManager = null;
    }
    this.okHttpClient = okBuilder.build();
  }

  /**
   * Device Server URL.
   */
  public HttpUrl getUrl() {
    return url;
  }

  /**
   * Object used to handle responses.
   */
  public ResponseHandler getResponseHandler() {
    return responseHandler;
  }

  /**
   * Returns a token provider used to store authorization credentials.
   */
  public AuthTokenProvider getTokenProvider() {
    return tokenProvider;
  }

  /**
   * HTTP client used for requests.
   */
  public OkHttpClient getOkHttpClient() {
    return okHttpClient;
  }

  /**
   * Authorization manager.
   */
  public AuthManager getAuthManager() {
    return authManager;
  }

  /**
   * Performs GET request.
   *
   * @param url   to be executed
   * @param clazz the class of T
   * @param <T>   the type of the desired object
   * @return the object of class T or null
   * @throws IOException          if the request could not be executed due to e.g. connectivity problems
   * @throws NullPointerException if url is null
   */
  public <T extends Hateoas> T get(String url, Class<T> clazz)
      throws IOException, NullPointerException {

    logger.debug("GET: {}", url);
    return new GetRequest<T>(url, responseHandler).execute(okHttpClient, clazz);
  }

  /**
   * Generic http GET wrapper, used mainly by {@link Navigator}.
   * @param url to be executed
   * @param <T> the type of desired object
   * @return object of class T or null
   * @throws IOException if the request could not be executed due to e.g. connectivity problems
   * @throws NullPointerException if url is null
   */
  public <T> T get(String url)
      throws IOException, NullPointerException {

    logger.debug("GET - GENERIC : {}", url);
    return (T) new GenericRequest(url, responseHandler){}.execute(okHttpClient);
  }

  /**
   * Dedicated function to request for object instances.
   *
   * @param url       to be executed
   * @param typeToken generic type
   * @param <T>       the type of the desired object
   * @return an instances of objects of type T
   * @throws IOException          if the request could not be executed due to e.g. connectivity problems
   * @throws NullPointerException if url is null
   */
  public <T extends Hateoas> Instances<T> getInstances(String url, TypeToken<Instances<T>> typeToken)
      throws IOException, NullPointerException {

    logger.debug("GET: {}", url);
    return new InstancesRequest<T>(url, responseHandler).execute(okHttpClient, typeToken);
  }

  /**
   * Performs PUT request.
   *
   * @param url    to be executed
   * @param object to be updated
   * @param <T>    the type of object
   * @return true if operation succeed, rise an exception instead
   * @throws IOException          if the request could not be executed due to e.g. connectivity problems.
   * @throws NullPointerException if url is null
   */
  public <T extends Hateoas> boolean put(String url, T object)
      throws IOException, NullPointerException {

    logger.debug("PUT: {} -> {}", url, object);
    new PutRequest<T>(url, object, requestSerializer, responseHandler)
        .execute(okHttpClient, (Class<T>) object.getClass());
    return true;
  }

  /**
   * Perform POST request.
   *
   * @param url    to be executed.
   * @param object to be created
   * @param <T>    the type of object
   * @param <R>    the type of expected response
   * @return an instance of type R
   * @throws IOException          if the request could not be executed due to e.g. connectivity problems
   * @throws NullPointerException if url is null
   */
  public <T extends Hateoas, R extends Hateoas> R post(String url, T object, Class<R> resultType)
      throws IOException, NullPointerException {

    logger.debug("PUT: {} -> {}", url, object);

    return new PostRequest<T, R>(url, object, requestSerializer, responseHandler)
        .execute(okHttpClient, resultType);
  }

  /**
   * Perform DELETE request.
   *
   * @param url to execute
   * @throws IOException          if the request could not be executed due to e.g. connectivity problems
   * @throws NullPointerException if url is null
   */
  public void delete(String url) throws IOException, NullPointerException {

    logger.debug("DELETE: {} ", url);

    new DeleteRequest(url).execute(okHttpClient);
  }

  /**
   * Returns a {@link SubscriptionsManager}.
   */
  public SubscriptionsManager getSubscriptionsManager() {
    return subscriptionsManager;
  }

  /**
   * Creates a {@link Navigator} instance.
   * @param rootUrl url to be used as a root when traversing links
   * @return new instance of {@link Navigator}
   */
  public Navigator createNavigator(String rootUrl) {
    return new NavigatorImpl(this, rootUrl);
  }

  public X509TrustManager getTrustManager() {
    return trustManager;
  }

  public SSLSocketFactory getSocketFactory() {
    return sslSocketFactory;
  }

  /**
   * Build a new {@link CreatorClient}.
   * Calling {@link #setUrl} is required before calling {@link #build}. Other methods are optional.
   */
  public static final class Builder {

    private HttpUrl url;
    private Cache cache;

    private AuthTokenProvider tokenProvider;
    private ResponseHandler responseHandler;
    private SSLSocketFactory socketFactory;
    private X509TrustManager trustManager;
    private Serializer requestSerializer;

    public Builder() {
      super();
    }

    public HttpUrl getUrl() {
      return url;
    }

    public Builder setUrl(String url) {
      Verify.checkNotNull(url, "url == null");
      HttpUrl httpUrl = HttpUrl.parse(url);
      if (httpUrl == null) {
        throw new IllegalArgumentException("Illegal URL: " + url);
      }
      this.url = httpUrl;
      return this;
    }

    public Builder setUrl(HttpUrl url) {
      Verify.checkNotNull(url, "url == null");
      this.url = url;
      return this;
    }

    public Cache getCache() {
      return cache;
    }

    public Builder setCache(Cache cache) {
      this.cache = cache;
      return this;
    }

    public AuthTokenProvider getTokenProvider() {
      return tokenProvider;
    }

    public Builder setTokenProvider(AuthTokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
      return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
      return socketFactory;
    }

    public X509TrustManager getTrustManager() {
      return trustManager;
    }

    public Builder setSslSocketFactory(SSLSocketFactory socketFactory, X509TrustManager trustManager) {
      this.socketFactory = socketFactory;
      this.trustManager = trustManager;
      return this;
    }

    public Serializer getRequestSerializer() {
      return requestSerializer;
    }

    public void setRequestSerializer(Serializer requestSerializer) {
      this.requestSerializer = requestSerializer;
    }

    public ResponseHandler getResponseHandler() {
      return responseHandler;
    }

    public Builder setResponseHandler(ResponseHandler responseHandler) {
      this.responseHandler = responseHandler;
      return this;
    }

    public CreatorClient build() throws IllegalStateException {
      if (url == null) {
        throw new IllegalStateException("URL required.");
      }
      if (requestSerializer == null) {
        requestSerializer = new GsonSerializer();
      }
      if (responseHandler == null) {
        responseHandler = new GsonResponseHandler(new GsonDeserializer());
      }
      if (tokenProvider == null) {
        tokenProvider = new AuthTokenProvider();
      }
      return new CreatorClient(this);
    }
  }
}
