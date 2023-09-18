package server;

import framework.annotations.Controller;
import framework.annotations.Get;
import framework.annotations.Path;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Discovery {

    private ArrayList<Route> routesMap;
    private static Discovery instance;

    private Discovery(){}

    public static Discovery getInstance(){
        if(instance == null){
            instance = new Discovery();
            instance.routesMap = new ArrayList<>();
            instance.mapRoutes();
        }
        return instance;
    }

    public Set<Class> findAllClasses(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .collect(Collectors.toSet());
    }

    public void mapRoutes(){
        Set<Class> classes = instance.findAllClasses("testproject");
        for (Class cl : classes) {
            if (cl.isAnnotationPresent(Controller.class)) {
                Method[] classMethods = cl.getMethods();

                for (Method method : classMethods) {
                    if (method.isAnnotationPresent(Path.class)) {
                        String httpMethod;
                        Path p = method.getAnnotation(Path.class);

                        if (method.isAnnotationPresent(Get.class))
                            httpMethod = "GET";
                        else
                            httpMethod = "POST";

                        Route route = new Route(p.path(), httpMethod, cl, method.getName());
                        instance.routesMap.add(route);
                    }
                }
            }
        }
        for (Route r : routesMap) {
            System.out.println(r);
        }
    }

    public ArrayList<Route> getRoutesMap() {
        return routesMap;
    }
}
