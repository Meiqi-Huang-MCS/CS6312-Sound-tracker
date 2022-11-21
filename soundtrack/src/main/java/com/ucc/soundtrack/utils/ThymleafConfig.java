package com.ucc.soundtrack.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import java.util.HashMap;
import java.util.Map;

@Component
public class ThymleafConfig {
    @Autowired
    @Qualifier("thymeleafViewResolver")
    private void myViewConfig(ThymeleafViewResolver thymeleafViewResolver){
        if(thymeleafViewResolver != null){
            Map<String, Object> vars = new HashMap<>();
            vars.put("staticpath", "http://localhost:8086/");
            thymeleafViewResolver.setStaticVariables(vars);
        }}
}

