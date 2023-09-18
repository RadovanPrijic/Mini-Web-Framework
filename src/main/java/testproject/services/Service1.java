package testproject.services;

import framework.injection.annotations.Autowired;
import framework.injection.annotations.Bean;
import framework.injection.annotations.Qualifier;

@Bean()
@Qualifier("Service1")
public class Service1 implements ServiceInterface {
    @Autowired(verbose = true)
    private Component1 component1;

    public Service1() {
    }
}
