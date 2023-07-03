# FRAMEWORK
## 1. Presentation
Framework JAVA similaire à spring MVC <br>
Sur toute les plateformes Utilisant Tomcat

## 2. Pré-requis
- JDK version 17.0.6 au minimum
- Tomcat 10 au minimum
- Avoir le __fw.jar__ du projet

## 3. Configuration de l'environnement de travail
* Copier dans le repertoir <b>"Project/WEB-INF/lib/"</b> le <b>"fw.jar"</b>
* Dans le <b>"Project/WEB-INF/web.xml"</b> mettez : <br>
```xml
    <servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>etu1784.framework.servlet.FrontServlet</servlet-class>
        <init-param>
            <param-name>session</param-name>
            <param-value>Variable session dans HttpSession</param-value>
        </init-param>
    </servlet>

    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>
```
## 4. Utilisation

* Toute les fonction d'action doit retourner la classe <b>"ModeView"</b>

* Toute les fonction d'action doit etre annoté par <b>"MethodAnnotation" </b><br>
        <b>url</b> = L'url pour l'appeler avec l'extension ".do"</br>
        <b>paramName</b> = les nons des parametres separer par virgule
```java
    @ActionMethod( url = "save.do", paramName = "nom,dateNaissance")
    public ModelView test(String nom, String dateNaissance) {
        ...
    }
```
* La classes ModelView contient l'attribut <b>"view"</b> qui est la view qui contient la view que l'on souhaite être dirigé
* La classe ModelView a une fonction addItem pour envoyer les donner dans va view que l'on recuperera ex:

```java
@ActionMethod( url = "getData.do")
public ModelView getData() {
    ModelView mv = new ModelView();

    mv.setView("/test.jsp");
    mv.addItem("data", Object data);

    return mv;
}
``` 

* Pour les recuperer dans la view il suffit juste de faire:

```jsp
    request.getAttribute("data")
```
* Pour l'envoie des données depuis la View vers la model <br>
Il faut que les nom des paramètres correspond au nom de l'attribut du classe ou le nom de parametre.

* Si on veut qu'une class soit traité comme un singleton il faut l'annoter par:
```java
    @Scope( type = ScopeType.SINGLETON)
    public class SingletonTest {
        // Les attribut sont initialiser avant chaque appel de la method
        ...
    }
```
 
## <b> Remarque </b>
* Toute les class ayant une method d'action doit avoir des setter qui prend des arguments de type __Primitives__ ou  __Date__ comme argument 
<br>ex:
```java
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setDateNaissance(java.util.Date dtn) {
        this.dateNaissanve = dtn;
    }
```
* Toute les arguments des fonction d'action doit doit être de type __Primitives__ ou __Date__.
```java
    ...
    public ModelView findById(int id) {
        ...
    }
```
