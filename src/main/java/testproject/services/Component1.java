package testproject.services;

import framework.injection.annotations.Autowired;
import framework.injection.annotations.Bean;
import framework.injection.annotations.Qualifier;

@Bean(scope = "prototype")
@Qualifier("Component1")
public class Component1 implements ComponentInterface {
    @Autowired(verbose = true)
    private Component3 component3;

    public Component1() {
    }
}
