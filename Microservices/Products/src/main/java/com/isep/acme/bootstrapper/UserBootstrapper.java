package com.isep.acme.bootstrapper;

import com.isep.acme.model.User;
import com.isep.acme.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
//@Profile("bootstrap")
public class UserBootstrapper implements CommandLineRunner {

    @Autowired
    private UserRepository uRepository;

    @Override
    public void run(String... args) throws Exception {
        
        if (uRepository.findByUsername("u1@u1.com").isEmpty()) {
            User u1 = new User("u1@u1.com","u1", "u1", "123456789", "xpto");
            uRepository.save(u1);
        }
    }
}
