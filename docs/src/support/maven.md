## 模版
```java [src/main/java-templates/com/xiesx/fastboot/Version.java]
package com.xiesx.fastboot;

public class Version {

    /** 应用名称 */
    public static final String APPLICATION = "${project.name}";

    /** 当前版本 */
    public static final String VERSION = "${project.version}";

    /** 构建分支 */
    public static final String BRANCH = "${buildBranch}";

    /** 构建版本 */
    public static final String BUILD = "${buildNumber}";

    /** 构建时间 */
    public static final String TIMESTAMP = "${buildTimestamp}";
}

```

## 插件
```xml
<build>
    <plugins>
        <!-- 测试 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <skipTests>${skip.test}</skipTests>
            </configuration>
        </plugin>

        <!-- 编译 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
        </plugin>

        <!-- 版本 -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>versions-maven-plugin</artifactId>
            <configuration>
                <generateBackupPoms>false</generateBackupPoms>
            </configuration>
        </plugin>
        
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>buildnumber-maven-plugin</artifactId>
            <executions>
                <execution>
                    <phase>validate</phase>
                    <goals>
                        <goal>create</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <doCheck>false</doCheck>
                <doUpdate>false</doUpdate>
                <shortRevisionLength>8</shortRevisionLength>
                <scmBranchPropertyName>buildBranch</scmBranchPropertyName>
                <timestampPropertyName>buildTimestamp</timestampPropertyName>
                <timestampFormat>yyyy-MM-dd HH:mm:ss</timestampFormat>
            </configuration>
        </plugin>

        <!-- 模版 -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>templating-maven-plugin</artifactId>
            <version>1.0.0</version>
            <executions>
                <execution>
                    <id>filtering-java-templates</id>	
                    <goals>
                        <goal>filter-sources</goal>
                    </goals>
                    <configuration>
                        <sourceDirectory>src/main/java-templates</sourceDirectory>
                    </configuration>
                </execution>
            </executions>
        </plugin>

        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>build-helper-maven-plugin</artifactId>
            <executions>
                <execution>
                    <id>add-generated-sources</id>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>add-source</goal>
                    </goals>
                    <configuration>
                        <sources>
                            <source>${project.build.directory}/generated-sources</source>
                        </sources>
                    </configuration>
                </execution>
            </executions>
        </plugin>

        <!-- 打包 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <configuration>
                <archive>
                    <manifest>
                        <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
                    </manifest>
                    <manifestEntries>
                        <Implementation-Version>${project.version}</Implementation-Version>
                        <Implementation-Branch>${buildBranch}</Implementation-Branch>
                        <Implementation-Build>${buildNumber}</Implementation-Build>
                        <Implementation-Timestamp>${buildTimestamp}</Implementation-Timestamp>
                    </manifestEntries>
                </archive>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 插件

```bash
mvn clean test jacoco:report-aggregate coveralls:report -Preport >x.log
```