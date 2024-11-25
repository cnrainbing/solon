#### 配置

数据源

```yaml
solon.dataSources:
  main!: # 数据源名称
    class: "com.zaxxer.hikari.HikariDataSource"
    jdbcUrl: "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL;DATABASE_TO_LOWER=TRUE;IGNORECASE=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE"
    driverClassName: "org.h2.Driver"
  sub: # 数据源名称
    class: "com.zaxxer.hikari.HikariDataSource"
    jdbcUrl: "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL;DATABASE_TO_LOWER=TRUE;IGNORECASE=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE"
    driverClassName: "org.h2.Driver"
```

完整配置

```yaml
solon.data.sqlink:
  # 用到几个就配几个
  # 使用@inject注入，不填名称的情况下默认为第一个
  main: # 名称与数据源对应
    printSql: true # 是否打印sql
    printBatch: true # 是否打印执行批量sql
    ignoreUpdateNoWhere: false # 是否允许无where更新
    ignoreDeleteNoWhere: false # 是否允许无where删除
  sub:
    dsName: h2ds
    printSql: false
    printBatch: false
    ignoreUpdateNoWhere: true
    ignoreDeleteNoWhere: true

```

简易配置

```yaml
solon.data.sqlink:
  main:
```

maven配置
```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${maven-compiler.version}</version>
            <configuration>
                <!--必须要配置，否则不生效-->
                <compilerArgs>
                    <arg>-Xplugin:ExpressionTree</arg>
                </compilerArgs>
                <annotationProcessorPaths>
                    <!--必须要配置，否则会有意外情况-->
                    <path>
                        <groupId>org.noear</groupId>
                        <artifactId>solon-data-sqlink</artifactId>
                        <version>${solon.version}</version>
                    </path>
                    <!-- lombok -->
                    <!--<path>-->
                    <!--    <groupId>org.projectlombok</groupId>-->
                    <!--    <artifactId>lombok</artifactId>-->
                    <!--    <version>${lombok.version}</version>-->
                    <!--</path>-->
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>

```

#### 代码

```java

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.data.sqlink.SqLink;
import org.noear.solon.data.sqlink.core.sqlExt.SqlFunctions;

//应用
@Mapping("/demo")
@Controller
public class DemoController {
    @Inject // or @Inject("main")
    SqLink sqLink;

    @Mapping("/hello")
    public String hello(String name) {
        // SELECT CONCAT_WS(' ','hello', {name})
        return sqLink.queryEmptyTable().endSelect(() -> SqlFunctions.join(" ", "hello", name)).first();
    }
}
```