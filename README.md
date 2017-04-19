# Creator Client

'Creator Client' is a java/Android library that allows developers to use [The Creator IoT Framework] (https://docs.creatordev.io/deviceserver/guides/iot-framework/)
and access Device Server REST API.

## Build status

Master
[![Build Status](https://www.travis-ci.org/krzych0/CreatorClient.svg?branch=master)](https://www.travis-ci.org/krzych0/CreatorClient)


Dev
[![Build Status](https://www.travis-ci.org/krzych0/CreatorClient.svg?branch=development)](https://www.travis-ci.org/krzych0/CreatorClient)

## Table of Contents

  * [Download](#download)
  * [Getting started](#getting-started)
  * [Accessing resources](#accessing-resources)
    * [Using raw http requests](#using-raw-http-requests)
    * [Using navigator](#using-navigator)
  * [Help](#help)

## Download

Download [the latest jar](https://github.com/krzych0/CreatorClient/releases) or grab via Gradle:

    compile compile("com.github.krzych0:CreatorClient:<latest version>")

Don't forget to [add JitPack maven repository](https://github.com/jitpack/jitpack.io).

## Getting started

In order to be able to utilize this library you will have to sign up for a creator account. 
You can do so going through the [Creator Developer Console](https://id.creatordev.io/).

After signing up, you will need to create an access key and secret pair from the API keys section of the console. 
These will gain you access to the Creator REST API while using this library.

Now you can create an instance of 'CreatorClient' as follows:

```java
HttpUrl url = HttpUrl.parse("https://deviceserver.creatordev.io");
AuthTokenProvider authTokenProvider = new AuthTokenProvider();  
CreatorClient creatorClient = new CreatorClient.Builder()
        .setUrl(url)
        .setTokenProvider(authTokenProvider)
        .build();
```

Having ```client``` instance ready we can authorize using access keys or refresh token (if available):

```java
try {   
    creatorClient.getAuthManager().authorize(key, secret);
} catch (IOException e) {
    e.printStackTrace();
}
```
After successful authorization you can access your resources RESTfully.
As you may already know, creator rest api is using what is called the HATEOAS REST application structure 
which means that you need to follow links in order to reach to certain resources. This library will do that for you.


!!! IMPORTANT !!!

As a user you need to ba aware that `CreatorClient` library internally is using [OkHttp](http://square.github.io/okhttp/) for networking
and [GSON](https://github.com/google/gson) for marshaling/de-marshaling. It may conflict with different libraries, responsible got converting 
Java Objects to JSON representation, that you may be using.   

## Accessing resources

`CreatorClient` gives you an easy way accessing resources. It's exposing a set of
 methods wrapping http request: 
```   
   creatorClient.get(...);
   creatorClient.post(...);
   creatorClient.put(...);
   creatorClient.delete(...);
```

Following code snippets shows typical usage of the library. 
Remember, to get full access to your resources you need to be authorized. Once it is done 
client will take care of refreshing tokens in your behalf. 


From security perspective you may wish to store refresh token instead of your 
access keys. You can do that by extracting particular data from `authTokenProvider` instance
 that was used to build `creatorClient` instance.
 ```java
 final String refreshToken = authTokenProvider.getAuthToken().getRefreshToken();
 ```

### Using raw http requests
 
 * Accessing root entry-point:
     ```java
     HttpUrl url = HttpUrl.parse("https://deviceserver.creatordev.io");
     AuthTokenProvider authTokenProvider = new AuthTokenProvider();
     CreatorClient creatorClient = new CreatorClient
             .Builder()
             .setUrl(url)
             .setTokenProvider(authTokenProvider)
             .build();
     
     
     creatorClient.getAuthManager().authorize(key, secret);
  
     Api api = creatorClient.get(url.toString(), Api.class);
     ```
 * Accessing first level entities:
     ```java
     Identities identities = creatorClient.get(api.getLinkByRel("identities").getHref(), Identities.class);
     Configuration cfg = creatorClient.get(api.getLinkByRel("configuration").getHref(), Configuration.class);
     Bootstrap bootstrap = creatorClient.get(cfg.getLinkByRel("bootstrap").getHref(), Bootstrap.class);
     Versions versions = creatorClient.get(api.getLinkByRel("versions").getHref(), Versions.class);
     ```
 * Requesting `Clients`:
     ```java
      Clients clients = creatorClient.get(api.getLinkByRel("clients").getHref(), Clients.class);
     ```
 Having `Clients` instance you can match specific client:
   ```java
   Client temperatureClient = null;
   for (Client c: clients.getItems()) {
      if (c.getName().equals("TemperatureDeviceClient")) {
          temperatureClient = c;
          break;
      }
   }
   ```
 * Accessing `Instances`:
 
    With `Client` instance ready we can try access its instance value. Following
    example shows how to find `ObjectType` with ID 3308 and extract user defined
    value from it (in this case it will be temperature value):
    
    ```java
    ObjectTypes types = creatorClient.get(temperatureClient.getLinkByRel("objecttypes").getHref(), ObjectTypes.class);
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
    Instances<PointValue> instances = creatorClient.getInstances(tempType.getLinkByRel("instances").getHref(),
        new TypeToken<Instances<PointValue>>(){});
    PointValue i = instances.getItems().get(0);
    ```
    
    where `PointValue` is a simple pojo class:
    ```java
    public class PointValue extends Hateoas {
    
    	@SerializedName("SetPointValue")
    	@Expose
    	private float pointValue;
    
    	@SerializedName("InstanceID")
    	@Expose
    	private String instanceID;
    
    
    	public void setPointValue(float pointValue){
    		this.pointValue = pointValue;
    	}
    
    	public float getPointValue(){
    		return pointValue;
    	}
    
    	public void setInstanceID(String instanceID){
    		this.instanceID = instanceID;
    	}
    
    	public String getInstanceID(){
    		return instanceID;
    	}
    
    }
    ```
 
 * Updating `Instance`:
    
    If we take an `Instance` object from previous example we can easily update its value:  
     ```java
      PointValue i = instances.getItems().get(0);
      boolean status = creatorClient.put(i.getLinkByRel("update").getHref(), i);
     ```

 * Requesting new pair of `AccessKeys`:
 
    ```java
    AccessKey k = new AccessKey();
    k.setName("MyNewAccessKey");
    AccessKey keys = creatorClient.post(api.getLinkByRel("accesskeys").getHref(), k, AccessKey.class);
    ```
### Using navigator 

 To simplify traversing across links 'CreatorClient' provides you with a `Navigator` interface.
 Here is an example of accessing `PointValue` instance shown earlier:
 ```java
 try {
    Navigator navigator = client.createNavigator(client.getUrl().toString());

    PointValue value = navigator
                      .findByRel("clients")
                      .filter((propertyName, propertyValue) -> propertyName.equals("Name") && propertyValue.equals("TemperatureDeviceClient"))
                      .findByRel("objecttypes")
                      .filter((propertyName, propertyValue) -> propertyName.equals("ObjectTypeID") && propertyValue.equals("3308"))
                      .findByRel("instances")
                      .filter((propertyName, propertyValue) -> propertyName.equals("InstanceID") && propertyValue.equals("0"))
                      .get(new TypeToken<PointValue>() {
                      }, new GsonDeserializer());
  
    } catch (Exception e) {
        e.printStackTrace();
    }
 ``` 

## Test

To run tests execute command below:
```
./gradlew :core:clean :core:test
```
## Help
  