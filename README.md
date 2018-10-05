# OpenICP-BR ktLib

[![Build Status](https://travis-ci.com/OpenICP-BR/ktLib.svg?branch=master)](https://travis-ci.com/OpenICP-BR/ktLib)
[![Code Coverage](https://codecov.io/gh/OpenICP-BR/ktLib/branch/master/graph/badge.svg)](https://codecov.io/gh/OpenICP-BR/ktLib)
![Semantic Version](https://img.shields.io/badge/semver-0.0.4--SNAPSHOT-blue.svg)
![JRE Version](https://img.shields.io/badge/jre-10-lightgrey.svg)
[![LGPL License](https://img.shields.io/badge/license-LGPL-green.svg)](https://www.gnu.org/licenses/lgpl-3.0.en.html)

A simple Kotlin library for [CAdES](https://en.wikipedia.org/wiki/CAdES_(computing)) digital signatures according to [ICP-Brasil](https://www.iti.gov.br) (Brazil's PKI) [rules](https://www.iti.gov.br/legislacao/61-legislacao/504-documentos-principais).

## Getting Started

### Via Maven

```
<dependencies>
    <dependency>
        <groupId>com.github.OpenICP_BR</groupId>
        <artifactId>ICP</artifactId>
        <version>x.y.z</version>
    </dependency>
</dependencies>
....
<repositories>
    <repository>
        <id>LibOpenICP-BR-mvn-repo</id>
        <url>https://raw.github.com/OpenICP-BR/ktLib/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

## Running the tests

Run: `mvn test`

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Maven](https://maven.apache.org/) - Dependency management
* [JUnit](https://junit.org/junit5/) - For automated tetsing
* [BouncyCastle](https://www.bouncycastle.org/) - Used for crypto stuff

<!--## Contributing

Publish maven repo: `mvn clean deploy`

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of 
conduct, and the process for submitting pull requests to us.-->


## Authors

* **G Queiroz** - *Initial work* - [gjvnq](https://github.com/gjvnq)

<!--See also the list of [contributors](https://github.com/your/project/contributors) who participated in this 
project.-->

## License

This project is licensed under the LGPL License - see the [LICENSE.md](LICENSE.md) file for details

<!--
## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
-->