package testproject.services;

import framework.injection.annotations.Autowired;
import framework.injection.annotations.Component;
import framework.injection.annotations.Qualifier;

@Component
@Qualifier("Component3")
public class Component3 implements ComponentInterface {
    @Autowired(verbose = true)
    @Qualifier("Service3")
    private ServiceInterface service3;

    public Component3() {
    }
}
