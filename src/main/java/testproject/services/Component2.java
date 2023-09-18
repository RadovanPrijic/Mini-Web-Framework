package testproject.services;

import framework.injection.annotations.Autowired;
import framework.injection.annotations.Component;
import framework.injection.annotations.Qualifier;

@Component
@Qualifier("Component2")
public class Component2 implements ComponentInterface {
    @Autowired(verbose = true)
    @Qualifier("Component3")
    private ComponentInterface component3;

    public Component2() {
    }
}
