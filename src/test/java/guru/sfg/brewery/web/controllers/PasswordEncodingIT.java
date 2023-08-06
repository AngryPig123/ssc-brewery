package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

public class PasswordEncodingIT {


    static final String PASSWORD = "password";

    @Test
    void bcryptTest() {
        PasswordEncoder pe = new BCryptPasswordEncoder();
        System.out.println(pe.encode("guru"));
    }


    @Test
    void sha256Test() {
        PasswordEncoder pe = new StandardPasswordEncoder();
        System.out.println(pe.encode("password"));
    }

    @Test
    void ldapTest() {
        PasswordEncoder pe = new LdapShaPasswordEncoder();
        String encode = pe.encode("tiger");
        System.out.println(encode);
    }

    @Test
    void noopTest() {
        PasswordEncoder pe = NoOpPasswordEncoder.getInstance();
        String encode = pe.encode(PASSWORD);
        System.out.println(encode);
    }

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
    }

}
