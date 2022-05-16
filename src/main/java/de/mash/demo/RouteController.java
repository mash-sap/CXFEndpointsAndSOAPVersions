package de.mash.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("route")
public class RouteController {
    @Autowired
    CamelContext camelContext;
    boolean routeIsRunning =true;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public void disable() throws Exception {
        ((SpringCamelContext) camelContext).stopRoute("hello");
        System.out.println("route 'hello' deactivated");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void start() throws Exception {
        if(routeIsRunning){
            ((SpringCamelContext) camelContext).stopRoute("lazy");
            System.out.println("route 'lazy' stopped");
        }else{
            ((SpringCamelContext) camelContext).startRoute("lazy");
            System.out.println("route 'lazy' started");
        }
        routeIsRunning = !routeIsRunning;
    }

}
