package org.example.keycloak;

import org.example.keycloak.external.HashMapUserStore;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class SpringUserStorageProviderFactory implements UserStorageProviderFactory<SpringUserStorageProvider>{

    UserApiService userApiService = new UserApiService();
    HashMapUserStore hashMapUserStore = new HashMapUserStore(userApiService);
    public static final String PROVIDER_ID = "keycloak-user-management";

    @Override
    public SpringUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new SpringUserStorageProvider(session,model,userApiService,hashMapUserStore);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

}

