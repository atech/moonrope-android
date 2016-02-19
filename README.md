# Moonrope Android Client

This is a simple client for communicating with a [Moonrope API](http://github.com/adamcooke/moonrope)
using Java. Simply add this library as a new module in your project.

## Installation

Add the library as a new module in your project.

## Usage

This example shows how to set up the library to download some data. In practice, it is recommended that the call to makeRequest() is not performed on the UI thread.

```java
  // Create a moonrope client instance
  MoonropeClient client = new MoonropeClient("api.yourapp.com", "https");
  client.addHeader("X-Auth-Token", "SomeToken");
  client.addHeader("X-Auth-Secret", "SomeSecret");
  
  // Make a request to the API
  MoonropeResponse response = client.makeRequest("controller/action");
  
  switch (response.getType())
  {
    case Success:
      MoonropeResponseSuccess responseSuccess = (MoonropeResponseSuccess)response;
      // The request has been completed successfully and the data is now
      // available for your use via getData().
      break;
      
    case Error:
      MoonropeResponseError responseError = (MoonropeResponseError)response;
      // The API returned an error. The type of the error and the data which
      // was provided is available.
      break;
      
    case Failure:
      MoonropeResponseError responseFailure = (MoonropeResponseFailure)response;
      // There was a serious error communicating with the API server. This is a
      // connection issue or a fatal error. The human readable error is provided.
      break;
  }
```
