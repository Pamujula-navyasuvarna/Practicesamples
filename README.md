# minimal-java-app

Minimal Java HTTP app (JDK only). Produces an executable jar.

## Build
Requires Java 17 and Maven.

```bash
# from project root
mvn -q -DskipTests package
# resulting jar: target/minimal-java-app-0.1.0.jar (shaded executable)
