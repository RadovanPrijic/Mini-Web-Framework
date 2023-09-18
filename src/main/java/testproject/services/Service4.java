package testproject.services;

import framework.injection.annotations.Bean;
import framework.injection.annotations.Qualifier;

@Bean()
@Qualifier("Service4")
public class Service4 implements ServiceInterface {

    public Service4() {
    }
}