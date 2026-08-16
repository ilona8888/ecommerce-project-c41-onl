package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {

    private Long id;
    private String userName;
    private String email;
    private String passwordHash;
    private boolean status;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private UserRole role;
    private LocalDateTime createdDate;
    private List<Favorite> favorites = new ArrayList<>();
    private List<Purchase> purchases = new ArrayList<>();

    // for authorization
    public User(Long id, String email, String passwordHash)
    {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // for register
    public User(Long id, String userName, String email, String passwordHash, String firstName, String lastName, LocalDate birthday, UserRole role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.role = role;
    }
}

