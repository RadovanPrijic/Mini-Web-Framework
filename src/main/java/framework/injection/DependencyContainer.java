package framework.injection;

import framework.exceptions.QualifierNotUniqueException;
import framework.exceptions.RegistrationException;
import framework.injection.annotations.Qualifier;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DependencyContainer {
    private HashMap<String, Class> specToImplMap;

    public DependencyContainer() {
        this.specToImplMap = new HashMap<>();
    }

    public Class returnImplementation(String qualifier){
        if(this.specToImplMap.containsKey(qualifier))
            return specToImplMap.get(qualifier);
        else try {
            throw new RegistrationException("This qualifier does not exist in the dependency container.");
        } catch (RegistrationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<Class> findAllClasses(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .collect(Collectors.toSet());
    }

    public void mapQualifiers(){
        Set<Class> classes = this.findAllClasses("testproject");
        for (Class cl : classes) {
            if(cl.getAnnotation(Qualifier.class)!=null) {
                Qualifier qualifier = (Qualifier) cl.getAnnotation(Qualifier.class);
                if(this.specToImplMap.containsKey(qualifier.value())){
                    try {
                        throw new QualifierNotUniqueException("Value of every class @Qualifier annotation must be unique.");
                    } catch (QualifierNotUniqueException e) {
                        e.printStackTrace();
                    }
                } else
                    this.specToImplMap.put(qualifier.value(), cl);
            }
        }
    }
}
