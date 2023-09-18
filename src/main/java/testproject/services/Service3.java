package testproject.services;

import framework.injection.annotations.Autowired;
import framework.injection.annotations.Qualifier;
import framework.injection.annotations.Service;

@Service
@Qualifier("Service3")
public class Service3 implements ServiceInterface {
    @Autowired(verbose = true)
    @Qualifier("Service4")
    private ServiceInterface service4;

    public Service3() {
    }
}
