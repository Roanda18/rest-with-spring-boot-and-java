package br.com.matheus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Startup {

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);

        Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);



        /*Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256));
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256));
        String result = passwordEncoder.encode("admin123");
        String senhaMatheus = passwordEncoder.encode("admin556");

        System.out.println("My hash " + result);
        System.out.println("My hash 2 " + senhaMatheus);*/

    }

}
