The echopoint dependency (which is not available from Maven Central) was installed here using these commands:

mvn deploy:deploy-file -Dfile=/PATH/TO/echopoint-3.0.0b5.jar \
    -DgroupId=echopoint -DartifactId=echopoint -Dversion=3.0.0b5 -Dpackaging=jar \
    -Durl=file:///PATH/TO/web-address-book/local-repo/
mvn deploy:deploy-file -Dfile=/PATH/TO/echopoint-3.0.0b5-sources.jar \
    -DgroupId=echopoint -DartifactId=echopoint -Dversion=3.0.0b5 -Dpackaging=jar -Dclassifier=sources \
    -Durl=file:///PATH/TO/web-address-book/local-repo/

It can be used afterwards by defining a custom repository in the POM:

<repositories>
    <repository>
        <id>local-repo</id>
        <url>file://${basedir}/local-repo</url>
    </repository>
</repositories>
