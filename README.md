# This repository is deprecated. 
## The actual repository is available here https://github.com/ydb-platform/ydb-java-sdk, you can use the [Migration Guide](https://github.com/ydb-platform/ydb-java-sdk/wiki/YDB-Java-v2-Migration-Guide) to update

# Java SDK for Yandex Database (YDB) ![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Fyandex%2Fydb%2Fydb-sdk-parent%2Fmaven-metadata.xml)

The Java SDK for YDB enables Java developers to work with Yandex Database.

* [Yandex Database Documentation][ydb-docs]
* [YDB SDK Documentation][sdk-docs]

## Getting Started

#### Connection setup ####

Before you begin, you need to create a database and setup authorization. Please see the [Prerequisites][prerequisites] section of the connection guide in documentation for information on how to do that.

#### Minimum requirements ####

To use YDB Java SDK you will need **Java 1.8+**. 

#### Install the SDK ####

The recommended way to use the YDB Java SDK in your project is to consume it from Maven. Specify the SDK Maven modules that your project needs in the
dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>com.yandex.ydb</groupId>
        <artifactId>ydb-sdk-core</artifactId>
        <version>1.14.9</version>
    </dependency>
    <dependency>
        <groupId>com.yandex.ydb</groupId>
        <artifactId>ydb-sdk-table</artifactId>
        <version>1.14.9</version>
    </dependency>
    <dependency>
        <groupId>com.yandex.ydb</groupId>
        <artifactId>ydb-sdk-auth-iam</artifactId>
        <version>1.14.9</version>
    </dependency>
</dependencies>
```

## Examples ##

#### Using Maven ####

In [examples/basic_example][basic_example] folder there is simple example application that uses YDB Java SDK from Maven.
See the [Connect to a database][connect-to-a-database] section of the documentation for an instruction on how to setup and launch it.

#### Generic examples ####

In [examples][generic-examples] folder you can find more example applications with YDB Java SDK usage.


[ydb-docs]: https://ydb.tech/docs/
[sdk-docs]: https://ydb.tech/en/docs/reference/ydb-sdk/
[prerequisites]: https://ydb.tech/en/docs/concepts/connect
[connect-to-a-database]: https://ydb.tech/en/docs/reference/ydb-sdk/auth#env
[basic_example]: https://github.com/yandex-cloud/ydb-java-sdk/tree/master/examples/basic_example
[generic-examples]: https://github.com/yandex-cloud/ydb-java-sdk/tree/master/examples
