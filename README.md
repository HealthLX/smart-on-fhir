# smart-on-fhir
Library that makes it easy to start developing an app using [SMART App Launch Framework](http://www.hl7.org/fhir/smart-app-launch/).

Our goals are:

* Enable faster development. Developers could quickly start coding.
* Hide all boilerplate code. Error prone details are implemented once and hidden.
* Stay flexible. We provide auto-configuration for early stage of development, and enhanced beans for your custom configuration at later stages.

## Getting Started

### Prerequisites
* Since [SMART App Launch Framework](http://www.hl7.org/fhir/smart-app-launch/) is based on [Oauth 2.0](https://tools.ietf.org/html/rfc6749#section-4.1) with [OpenID](https://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth) you need to get familiar a bit.
* We are using [Spring Security 5 Oauth 2.0 Client  implementation](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#oauth2login) under the hood.
* We are testing against java 8

## Installation
* Just add dependency:

At the moment we are working on publishing to maven central. So for now please use [JitPack](https://www.jitpack.io):

```groovy
repositories {
    maven { url 'https://www.jitpack.io' }
}
dependencies {
    implementation 'com.github.HealthLX:smart-on-fhir:reference-impl-split-SNAPSHOT'
}
```

### Auto configuration for your Spring Boot app
If you want to start new project quickly you need to follow those steps:
* Enable our configurations.
```java
@EnableSmartOnFhir //<---Add this one
@Configuation
class YourConfiguration {
}
```
* Configure your server (EHR) auth data:

This is example for Public client.
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          ehr-client:
            client-id: <YOUR_CLIENT_ID>
            client-name: <NAME>
            provider: ehr-provider
            redirect-uri: "{baseUrl}/login/oauth2/code/ehr-client"
            client-authentication-method: none
            authorization-grant-type: authorization_code
            scope: <SCOPE>
        provider:
          ehr-provider:
            token-uri: <TOKEN_URI>
            authorization-uri: <AUTH_URI>
            user-info-uri: <USER_INFO_URI>
            jwk-set-uri: <JWK_URI>
            userNameAttribute: <USER_NAME_ATTRIBUTE>
```
For other options to configure look [here](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#oauth2login-boot-property-mappings)

### Custom configuration
In case you want to have custom security configuration you need to register and wire our beans:
* Register them:
```java
  @Bean
  SmartOnFhirContext smartOnFhirContext(HttpSession session) {
    return new SmartOnFhirContext(session);
  }

  @Bean
  public SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient(SmartOnFhirContext context) {
    return new SmartOnFhirAccessTokenResponseClient(context);
  }
``` 
* Wire them
```java
@Configuration
@EnableWebSecurity
public class YourConfiguration extends WebSecurityConfigurerAdapter {
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        //your config

        //Our AuthRequest resolver could be created on flight
        .oauth2Login()
        .authorizationEndpoint()
        .authorizationRequestResolver(new SmartOnFhirAuthRequestResolver(clientRegistrationRepository))

        .and()

        //Our TokenResponse client
        .tokenEndpoint()
        .accessTokenResponseClient(this.smartOnFhirAccessTokenResponseClient);
  }
}
```
## Usage
To gain access to SMART Context just wire one bean that is created for you:
```java
class Foo{
  private SmartOnFhirContext smartOnFhirContext;

    @Autowired
    public Foo(SmartOnFhirContext smartOnFhirContext) {
        this.smartOnFhirContext = smartOnFhirContext;
    }
    
    void bar (){
      // and use it
      smartOnFhirContext.getPatient();
      smartOnFhirContext.getProfile();
      //etc...
    }
 }
```
But be sure user is authorized to use it. In auto configured setup we require authorization for all pages. Usually if app is started from EHR it is enough.

### Extra use cases
To use data from launch context you might need to have FHIR client that is authorized to launching EHR. Or if you have one you might need to provide access token for it. To obtain it use  you should use Spring provided ```OAuth2AuthorizedClientService```:
```java
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     OAuth2AuthorizedClient authorizedClient = this.authorizedClientService.loadAuthorizedClient("ehr-client",authentication.getName());
     OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
     accessToken.getTokenValue();
```
Read more [here](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#oauth2Client-authorized-repo-service)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details
