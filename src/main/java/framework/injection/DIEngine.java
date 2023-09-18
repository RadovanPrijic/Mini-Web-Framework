package framework.injection;

import framework.exceptions.AutowiredException;
import framework.exceptions.InterfaceQualifierException;
import framework.injection.annotations.*;
import testproject.controllers.Controller1;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DIEngine {

    private HashMap<String, Object> quasiSingletonInstances;
    private DependencyContainer dependencyContainer;
    private static DIEngine instance;

    private DIEngine(){}

    public static DIEngine getInstance(){
        if(instance == null){
            instance = new DIEngine();
            instance.quasiSingletonInstances = new HashMap<>();
            instance.dependencyContainer = new DependencyContainer();
            instance.dependencyContainer.mapQualifiers();
        }
        return instance;
    }

    public void initialiseDependencies(String controllerClassName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class cl = Class.forName(controllerClassName);
        Object controllerObj = instance.returnClassInstance(cl);
        instance.quasiSingletonInstances.put(controllerObj.getClass().getName(), controllerObj);
        System.out.println("Hashcode pozvanog kontrolera: " + Objects.requireNonNull(controllerObj).hashCode());
        Field[] controllerFields = cl.getDeclaredFields();
        recursiveInitialisation(controllerObj, controllerFields);
    }

    public void recursiveInitialisation(Object parentObj, Field[] fields) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        System.out.println("----------------------- POZIV REKURZIVNE INICIJALIZACIJE -----------------------");
        for(Field field : fields){
            System.out.println("POLJE: " + field);

            if(field.isAnnotationPresent(Autowired.class)){
                Object obj = null;
                Class cl = null;
                Constructor constructor;

                if(field.getType().isInterface()){
                    Qualifier qualifier = field.getAnnotation(Qualifier.class);
                    if(qualifier != null){
                        cl = instance.dependencyContainer.returnImplementation(qualifier.value());
                    } else try {
                        throw new InterfaceQualifierException("An interface attribute is missing a @Qualifier annotation.");
                    } catch (InterfaceQualifierException e) {
                        e.printStackTrace();
                    }
                } else {
                    String[] str = field.toString().split(" ");
                    cl = Class.forName(str[1]);
                }
                constructor = Objects.requireNonNull(cl).getDeclaredConstructor();

                if(cl.isAnnotationPresent(Bean.class)){
                    Bean bean = (Bean) cl.getAnnotation(Bean.class);
                    if(bean.scope().equals("singleton")){
                        obj = instance.returnClassInstance(cl);
                        instance.quasiSingletonInstances.put(obj.getClass().getName(), obj);
                    } else if(bean.scope().equals("prototype"))
                        obj = constructor.newInstance();
                } else if (cl.isAnnotationPresent(Service.class)){
                    obj = instance.returnClassInstance(cl);
                    instance.quasiSingletonInstances.put(obj.getClass().getName(), obj);
                } else if (cl.isAnnotationPresent(Component.class)){
                    obj = constructor.newInstance();
                } else try {
                    throw new AutowiredException("The @Autowired annotation is used on an invalid attribute.");
                } catch (AutowiredException e) {
                    e.printStackTrace();
                }
                field.setAccessible(true);
                field.set(parentObj, obj);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if(autowired.verbose()){
                    System.out.println("Initialized " + field.getType() + " " + field.getName() + " in " + parentObj.getClass().getName() + " on "
                            + LocalDateTime.now() + " with hash code " + Objects.requireNonNull(obj).hashCode());
                }
                recursiveInitialisation(obj, cl.getDeclaredFields());
            }
        }
    }

    public Object returnClassInstance(Class cl) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(quasiSingletonInstances.containsKey(cl.getName()))
            return quasiSingletonInstances.get(cl.getName());
        else {
            Constructor constructor = cl.getDeclaredConstructor();
            return constructor.newInstance();
        }
    }

}
