package org.example.keycloak;

import org.example.keycloak.external.HashMapUserStore;
import org.example.keycloak.external.User;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.*;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SpringUserStorageProvider implements
        UserLookupProvider,
        UserStorageProvider,
        UserQueryProvider{
    private KeycloakSession session;
    private UserApiService userService;
    private HashMapUserStore hashMapUserStore;
    private ComponentModel model;
    protected Map<String, UserModel> loadedUsers = new HashMap<>();

    public SpringUserStorageProvider(KeycloakSession session, ComponentModel model, UserApiService userService,HashMapUserStore hashMapUserStore){
        this.session =session;
        this.model = model;
        this.hashMapUserStore=hashMapUserStore;
        this.userService=  userService;
    }

    @Override
    public void close() { }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, String search) {
        return hashMapUserStore.findUsers(search).stream()
                .map(user -> new UserAdapter(session, realm, model, user));
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realmModel, String s, Integer integer, Integer integer1) {
        return searchForUserStream(realmModel, s);
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realmModel, Map<String, String> map, Integer integer, Integer integer1) {
        return hashMapUserStore.getAllUsers().stream()
                .map(user -> new UserAdapter(session, realmModel, model, user));
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realmModel, GroupModel groupModel, Integer integer, Integer integer1) {
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realmModel, String s, String s1) {
        return Stream.empty();
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        return UserQueryProvider.super.getUsersCount(realm);
    }

    @Override
    public UserModel getUserById(RealmModel realmModel, String id) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return new UserAdapter(session,realmModel,model,hashMapUserStore.findUserById(username));
    }

    private UserModel createAdapter(RealmModel realm, String username) {
        return new UserAdapter(session, realm, model, hashMapUserStore.findUserByUsernameOrEmail(username));
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String username) {
        UserModel adapter = loadedUsers.get(username);
        if (adapter == null) {
            User user = hashMapUserStore.findUserByUsernameOrEmail(username);
            if (user != null) {
                adapter = createAdapter(realmModel, username);
                loadedUsers.put(username, adapter);
            }
        }
            return adapter;
    }
    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String email) {
        return null;
    }

}
