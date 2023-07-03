cd "X:\NainaGIt\Nouveau_dossier\Framework-main\Framework\"
jar cvf fw.jar *
copy "./fw.jar" "X:\NainaGIt\Nouveau_dossier\Framework-main\TestFramework\WEB-INF\lib\"
cd "X:\NainaGIt\Nouveau_dossier\Framework-main\TestFramework\WEB-INF\classes"
javac -cp "X:\NainaGIt\Nouveau_dossier\Framework-main\TestFramework\WEB-INF\lib\fw.jar" -d . *.java
cd "X:\NainaGIt\Nouveau_dossier\Framework-main\TestFramework"
jar cvf "TestFramework.war" *
copy "TestFramework.war" "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\"
del "TestFramework.war"