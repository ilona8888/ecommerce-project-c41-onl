package by.tms.ecommerceprojectc41onl.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestMain {

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String pwdBuyer = "1234";
        String pwdSeller = "1234";
        String pwdAdmin = "1234";

        System.out.println("buyer:    " + encoder.encode(pwdBuyer));
        System.out.println("seller:   " + encoder.encode(pwdSeller));
        System.out.println("admin:    " + encoder.encode(pwdAdmin));
    }


}
