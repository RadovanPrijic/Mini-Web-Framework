package testproject.controllers;

import framework.annotations.Controller;
import framework.annotations.Get;
import framework.annotations.Path;
import framework.annotations.Post;
import framework.injection.annotations.Autowired;
import framework.injection.annotations.Qualifier;
import testproject.services.Service1;
import testproject.services.Service2;
import testproject.services.ServiceInterface;

@Controller
public class Controller1 {
    @Autowired(verbose = true)
    private Service1 service1;

    @Autowired(verbose = true)
    private Service2 service2;

    @Autowired(verbose = true)
    @Qualifier("Service3")
    private ServiceInterface service3;

    @Path(path = "/test1")
    @Get
    public void testMethod1(){

    }

    @Path(path = "/test2")
    @Post
    public void testMethod2(){

    }
}
