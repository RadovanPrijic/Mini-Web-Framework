package testproject.services;

import framework.injection.annotations.Autowired;
import framework.injection.annotations.Qualifier;
import framework.injection.annotations.Service;

@Service
@Qualifier("Service2")
public class Service2 implements ServiceInterface {
    @Autowired(verbose = true)
    private Component2 component2;

    public Service2() {
    }
}
