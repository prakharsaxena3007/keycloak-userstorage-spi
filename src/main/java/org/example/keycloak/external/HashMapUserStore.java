package org.example.keycloak.external;

import org.example.keycloak.UserApiService;

import java.util.List;
import java.util.stream.Collectors;

public class HashMapUserStore {
    private UserApiService userApiService;
    private List<User> users;
    private static final String API_URL = "http://localhost:8081/api/v1/users/all-users";

    public HashMapUserStore(UserApiService userApiService){
            this.userApiService = userApiService;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User findUserById(String id) {
        System.out.println("Id : - " + id);
        System.out.println(users.stream().filter(user -> user.getUsername().equals(id)));
        return users.stream().filter(user -> user.getUsername().equals(id)).findFirst().orElse(null);
    }
    public User findUserByUsernameOrEmail(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst().orElse(null);
    }
    public List<User> findUsers(String query) {
            this.users = userApiService.getAllUsers(API_URL);
            System.out.println("Users refreshed from API.");

        return users.stream()
                .filter(user -> query.equalsIgnoreCase("*") || user.getUsername().contains(query))
                .collect(Collectors.toList());
    }

}
