package util;


import etu1904.framework.FileUpload;
import etu1904.framework.Mapping;
import etu1904.framework.ModelView;
import etu1904.framework.annotation.ActionMethod;
import etu1904.framework.annotation.Scope;
import etu1904.framework.type.ScopeType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.util.ArrayList;


public class Util {

    public ModelView invokeMethod(HttpServletRequest request, Mapping mapping, HashMap<String, Object> singleton) throws Exception {
        ArrayList<Class<?>> type = new ArrayList<>();
        ArrayList<Object> value = new ArrayList<>();
        this.setArgValue(request, mapping, type, value);

        Object o = this.setObjectByRequest(request, mapping, singleton);

        return (ModelView) o.getClass().getMethod(mapping.getMethod(), type.toArray(Class[]::new)).invoke(o, value.toArray(Object[]::new));
    }

    public void setArgValue(HttpServletRequest request, Mapping mapping, ArrayList<Class<?>> type, ArrayList<Object> value) throws Exception {
        Method m = this.getMethodByClassName(mapping.getClassName(), mapping.getMethod());

        if(m.isAnnotationPresent(ActionMethod.class) && !m.getAnnotation(ActionMethod.class).paramName().equals("") ) {
            type.addAll(List.of(m.getParameterTypes()));

            String[] paramName = m.getAnnotation(ActionMethod.class).paramName().split(",");

            if(paramName.length != type.size()) throw new Exception("Number of argument exception \n" +
                    "\t" + paramName.length + " declared but " + type.size() + " expected");

            String value_temp;
            for (int i=0; i< paramName.length; i++) {
                value_temp = request.getParameter(paramName[i].trim());
                value.add(this.castPrimaryType(value_temp, type.get(i)));
            }
        }
    }

    public Method getMethodByClassName(String className, String method) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        Object o = clazz.getDeclaredConstructor().newInstance();

        Method result = null;
        Method[] allMethod = o.getClass().getDeclaredMethods();
        for (Method m : allMethod) {
            if(m.getName() .equals(method)) {
                result = m;
                break;
            }
        }
        return result;
    }

    public void setAttributeRequest(HttpServletRequest request, ModelView mv) {
        HashMap<String, Object> donne = mv.getData();
        for(String key : donne.keySet()) {
            request.setAttribute(key, donne.get(key));
        }
    }

    public Object setObjectByRequest(HttpServletRequest request, Mapping map, HashMap<String, Object> singleton) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ServletException, IOException, ParseException {
        Object o = singleton.get(map.getClassName());
        if(o == null) {
            Class<?> clazz = Class.forName(map.getClassName());
            o = clazz.getDeclaredConstructor().newInstance();
        }
        this.initObject(o);

        Field[] allField = o.getClass().getDeclaredFields();
        String field_name;
        Object value_temp;
        Object value;

        for(Field f : allField) {
            field_name = f.getName();
            value_temp = (f.getType().equals(FileUpload.class)) ? Util.getValueUploadedFile(request, field_name) : request.getParameter(field_name);

            if(value_temp != null) {
                try {
                    if(!f.getType().equals(FileUpload.class)) value = this.castPrimaryType(value_temp.toString(), f.getType());
                    else value = value_temp;
                    o.getClass()
                            .getMethod("set"+this.casse(field_name), f.getType())
                            .invoke(o, value);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return o;
    }

    private static FileUpload getValueUploadedFile(HttpServletRequest request, String field_name) throws ServletException, IOException {
        Part filePart = request.getPart(field_name);
        FileUpload result = new FileUpload();
        result.setName(filePart.getSubmittedFileName());
        result.setFile(filePart.getInputStream().readAllBytes());

        return result;
    }

    private void initObject(Object o) throws IllegalAccessException, ParseException {
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            field.set(o, castPrimaryType("", field.getType()));
            field.setAccessible(false);
        }
    }

    public void loadMapping(String path, String tomPath, HashMap<String, Mapping> mappingUrls, HashMap<String, Object> singleton) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class<?>> allClass = this.getAllClass(path, tomPath);
        Mapping mapping;
        Method[] allMethods;

        for(Class<?> c : allClass) {
            allMethods = c.getMethods();

            if (c.isAnnotationPresent(Scope.class)) {
                if (c.getAnnotation(Scope.class).type().equals(ScopeType.SINGLETON)){
                    Class<?> clazz = Class.forName(c.getName());
                    Object temp = clazz.getDeclaredConstructor().newInstance();
                    singleton.put(c.getName(), temp);
                }
            }

            for(Method m : allMethods) {
                if(m.isAnnotationPresent(ActionMethod.class)) {
                    mapping = new Mapping();
                    mapping.setClassName(c.getName());
                    mapping.setMethod(m.getName());
                    mappingUrls.put(m.getAnnotation(ActionMethod.class).url(), mapping);
                }
            }
        }
    }

    public List<Class<?>> getAllClass(String path, String tomPath) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        File directory = new File(path);

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                List<Class<?>> innerClasses = this.getAllClass(file.getAbsolutePath(), tomPath);
                classes.addAll(innerClasses);
            } else if (file.getName().endsWith(".class")) {
                String absolute_path_class = file.getPath().replace("\\", "/");
                int tom_int_path = absolute_path_class.indexOf(tomPath);

                String className = absolute_path_class
                        .substring(tom_int_path + tomPath.length())
                        .replace(".class", "")
                        .replace("/", ".");
                Class<?> clazz = Class.forName(className);

                classes.add(clazz);
            }
        }
        return classes;
    }

    public Object castPrimaryType(String data, Class<?> type) throws ParseException {
        if(data == null || type == null) return null;
        if(data.equals("")) {
            if(type.equals(Date.class) || type.equals(String.class)) return null;
            else return 0;
        }

        if (type.equals(Date.class)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return type.cast(format.parse(data));
        }else if(type.equals(int.class)) return Integer.parseInt(data);
        else if(type.equals(float.class)) return Float.parseFloat(data);
        else if(type.equals(double.class)) return Double.parseDouble(data);
        else if(type.equals(boolean.class)) return Boolean.getBoolean(data);

        return data;
    }

    public String processUrl(String url_input, String ctx) {
        ctx+="/";
        int ctx_ind = url_input.indexOf(ctx);

        return url_input.substring(ctx_ind + ctx.length());
    }

    public String casse(String input) {
        char[] strrep = input.toCharArray();
        strrep[0] = Character.toUpperCase(strrep[0]);

        return new String(strrep);
    }

}
