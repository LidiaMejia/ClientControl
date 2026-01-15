package app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncrypter {

    public static void main(String[] args) {        
        var password1 = "adminPass";
        var password2 = "userPass";
        
        System.out.println("");
        System.out.println("password1 = " + password1);
        System.out.println("password1 Encrypted = " + encryptPassword(password1));
        System.out.println("");
        
        System.out.println("password2 = " + password2);        
        System.out.println("password2 Encrypted = " + encryptPassword(password2));
        System.out.println("");
    }
    
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        return encoder.encode(password);
    }
    
}
