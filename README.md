# Quarkus Azure Function

This project uses [Quarkus](https://quarkus.io/) to demonstrate deploying an Azure Function in Java
using `azure-functions-maven-plugin` version 1.36.0

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/fn-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.


## Deploying to azure

1. Login to Azure using `az` cli:
    ```
    az login
    ```
1. Retrieve the tenant ID and export it along with the existing resource group name to deploy to:
    ```
    TENANT_ID=<tenantId from az login output>
    RESOURCE_GROUP=fn-quarkus
    ```
1. Deploy to azure using this maven command:
    ```
    ./mvnw clean install -DskipTests -DtenantId=$TENANT_ID -DresourceGroup=$RESOURCE_GROUP azure-functions:deploy
    ```
1. Use function url from output and test deployment using `curl`:
    ```
    URL=https://fn-quarkus.azurewebsites.net/api/
    curl -k $URL/hello
    hello
    ```

## Related Guides

- Azure Functions HTTP ([guide](https://quarkus.io/guides/azure-functions-http)): Write Microsoft Azure functions
