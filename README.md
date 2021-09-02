# Process Message Operation Helper

This project is a library to add the $process-message operation to HAPI-based FHIR servers.
Due to the many different ways HAPI servers can be setup, there is some configuration required.

## Installation

This project can be added to an existing Maven-based project, add this dependency to `pom.xml`:

```xml
<dependency>
  <groupId>org.mitre.hapifhir</groupId>
  <artifactId>process-message-operation</artifactId>
  <version>0.0.2</version>
</dependency>
```

Or for a Gradle-based project, add this to `build.gradle`:

```gradle
compile 'org.mitre.hapifhir:process-message-operation:0.0.2'
```

## Usage

This library provides only the skeleton of an operation to register on the HAPI, along with some basic minimal validation. The actual action taken when the $process-message operation is called is left up to the user to implement. This implementation should passed into the constuctor of the ProcessMessageProvider as a function that takes in the request Bundle and MessageHeader and returns the response Bundle (specifically, a `Function<MessageContext, Bundle>`).

For example, using a JPA Starter HAPI FHIR Server:

```java
import org.mitre.hapifhir.ProcessMessageProvider;

...

public class JpaRestfulServer extends RestfulServer {

  private static Bundle processMessage(MessageContext messageContext) {
    // do the requested action, and produce a response bundle
    // for example, save the MessageHeader to the FHIR server
    DaoRegistry daoRegistry = appCtx.getBean(DaoRegistry.class);
    IFhirResourceDao<Bundle> bundleDao = daoRegistry.getResourceDao(ResourceType.Bundle.name());
    IFhirResourceDao<MessageHeader> messageDao = daoRegistry.getResourceDao(ResourceType.MessageHeader.name());
    bundleDao.create(messageContext.bundle);
    messageDao.create(messageContext.messageHeader);

    return ...
  }

  protected void initialize() {
    ...
    FhirContext fhirContext = appCtx.getBean(FhirContext.class);
    ProcessMessageProvider pmp = new ProcessMessageProvider(fhirContext, JpaRestfulServer::processMessage);
    registerProvider(pmp);
    ...
  }
}
```

## Development

To install the current working version to your local Maven repo, run

```sh
./gradlew publishToMavenLocal
```

### Publishing New Versions

To publish new versions to Maven Central, first update the version in `build.gradle`:

```gradle
def mavenVersion = '0.0.2'
```

Then tag the version as appropriate in GitHub, for example:

```sh
git tag v0.0.2
git push origin v0.0.2
```

The CI process `deploy.yml` will run to publish the new version.

Coordinate with [@dehall](https://github.com/dehall) (dehall@mitre.org) to ensure the published artifacts are released.

## License

Copyright 2021 The MITRE Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

```text
http://www.apache.org/licenses/LICENSE-2.0
```

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
