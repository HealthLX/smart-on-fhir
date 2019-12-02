## Sample App
Sample app to show how to use library.

In order to run app you need follow those steps:

1. Register in https://sandbox.hspconsortium.org/

2. Open Apps tab of your sandbox

3. Clone "My Web App" config

4. Set Launch URI to "http://localhost:8080/sample". ("/sample" points to controller endpoint)

5. Set Redirect URI to "http://localhost:8080/login/oauth2/code/ehr-client" (Corresponds to Oauth 2.0 Spring plugin, ehr-client could be change but that require changes in application properties)

6. Set scope: 
``` launch/patient,openid,profile,launch,fhirUser,launch/encounter,patient/Patient.read,patient/*.read,offline_access,patient/Observation.read,patient/*.write,patient/Encounter.read,patient/Observation.write,offline_access ```
*Note: Should be comma-separated in application.yaml but whitespace-separated in sandbox*

7. Copy Client Id to application.yaml

8. Go to "Settings" tab of sandbox anc copy fhir server uri to application.yaml

9. Run Gradle ```springBoot```

10. Launch your App configuration from "Apps" tab.

11. Pick persona.

12. Pick any Patient.

13. You should be navigated to authorization page. Allow access.

14. You should be navigated to SMART app landing page showing patient data read from fhir server.  