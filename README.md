# OpenICP-BR ktLib

[![Build Status](https://travis-ci.com/OpenICP-BR/ktLib.svg?branch=master)](https://travis-ci.com/OpenICP-BR/ktLib)
[![Code Coverage](https://codecov.io/gh/OpenICP-BR/ktLib/branch/master/graph/badge.svg)](https://codecov.io/gh/OpenICP-BR/ktLib)
[![Download](https://api.bintray.com/packages/gjvnq/mvn/ktLib/images/download.svg)](https://bintray.com/gjvnq/mvn/ktLib/_latestVersion)
![JRE Version](https://img.shields.io/badge/jre-10-lightgrey.svg)
[![LGPL License](https://img.shields.io/badge/license-LGPL-green.svg)](https://www.gnu.org/licenses/lgpl-3.0.en.html)
[![Doar](https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=M5A72UW7FF87W)

A simple Kotlin library for [CAdES](https://en.wikipedia.org/wiki/CAdES_(computing)) digital signatures according to [ICP-Brasil](https://www.iti.gov.br) (Brazil's PKI) [rules](https://www.iti.gov.br/legislacao/61-legislacao/504-documentos-principais).

## Getting Started

### Via Maven

```
<dependencies>
    <dependency>
        <groupId>com.github.OpenICP_BR</groupId>
        <artifactId>ktLib</artifactId>
        <version>0.0.8</version>
    </dependency>
</dependencies>
....
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>bintray-gjvnq-mvn</id>
        <name>bintray</name>
        <url>https://dl.bintray.com/gjvnq/mvn</url>
    </repository>
</repositories>
```

## Running the tests

Run: `mvn test`

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