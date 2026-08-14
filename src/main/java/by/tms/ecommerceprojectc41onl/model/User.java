package by.tms.ecommerceprojectc41onl.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {

    private Long id;
    private String userName;
    private String email;
    private String password;
    private boolean status;
    private String firstName;
    private String lastName;
    private LocalDateTime birthday;
    private UserRole role;
    private LocalDateTime createdDate;
    private List<Favorite> favorites = new ArrayList<>();
    private List<Purchase> purchases = new ArrayList<>();

    // for authorization
    public User(Long id, String email, String password)
    {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // for register
    public User(Long id, String userName, String email, String password, String firstName, String lastName, LocalDateTime birthday, UserRole role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.role = role;
    }
}

