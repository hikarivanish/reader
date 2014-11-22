package me.s4h;

import me.s4h.entity.User;
import me.s4h.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
//no shit
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Configuration
class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/reader").setViewName("reader");
    }
}




//@Component
class Runner implements CommandLineRunner{
    @Autowired
    UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        try {
            userRepository.save(new User("aa@aa.com", "pass", "lis64i"));
            userRepository.save(new User("aab@aa.com", "pass", "li23si"));
            userRepository.save(new User("aac@aa.com", "pass", "li6si"));
            userRepository.save(new User("ae@aa.com", "pass", "li43si"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


