## Sample App
Sample app to show how to use library.

In order to run app you need follow those steps:

1. Register in https://sandbox.hspconsortium.org/

2. Open Apps tab of your sandbox

3. Clone "My Web App" config

4. Set Launch URI to "http://localhost:8080/sample". ("/sample" points to controller endpoint)

5. Set Redirect URI to "http://localhost:8080/login/oauth2/code/ehr-client" (Corresponds to Oauth 2.0 Spring plugin, ehr-client could be change but that require changes in application properties)

6. Copy Client Id to application.yaml

7. Go to "Settings" tab of sandbox anc copy fhir server uri to application.yaml

8. Run Gradle ```springBoot```

9. Launch your App configuration from "Apps" tab.

10. Pick persona

11. Pick any Patient

12. You should be navigated to authorization page. Allow access.

13. You should be navigated to SMART app landing page showing patient data read from fhir server.  