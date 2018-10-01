# libICP

## Maven

```
<dependencies>
    <dependency>
        <groupId>com.github.OpenICP_BR</groupId>
        <artifactId>ICP</artifactId>
        <version>0.0.4-SNAPSHOT</version>
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


## Developer commands

Publish maven repo: `mvn clean deploy`