cd "D:\ITU\Web Dyn\Framework\out\production\Framework\"
jar cvf fw.jar *
copy "./fw.jar" "D:\ITU\Web Dyn\TestFramework\WEB-INF\lib\"
cd "D:\ITU\Web Dyn\TestFramework\WEB-INF\classes"
javac -cp "D:\ITU\Web Dyn\TestFramework\WEB-INF\lib\fw.jar" -d . *.java
cd "D:\ITU\Web Dyn\TestFramework"
jar cvf "TestFramework.war" *
copy "TestFramework.war" "C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps\"
del "TestFramework.war"