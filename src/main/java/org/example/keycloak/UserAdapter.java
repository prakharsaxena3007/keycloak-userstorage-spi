package org.example.keycloak;

import org.example.keycloak.external.User;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.storage.adapter.AbstractUserAdapter;

public class UserAdapter extends AbstractUserAdapter.Streams {

    private final User users ;
    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, User users) {
        super(session, realm, model);
        this.users = users;
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public void setUsername(String username) {
       users.setUsername(username);
    }

    @Override
    public String getFirstName(){
        return users.getFirstName();
    }
    @Override
    public String getLastName(){
        return users.getLastName();
    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return new LegacyUserCredentialManager(session,realm,this);
    }


}
