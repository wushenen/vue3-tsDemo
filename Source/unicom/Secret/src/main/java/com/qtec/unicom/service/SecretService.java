package com.qtec.unicom.service;

import com.qtec.unicom.pojo.Secret;

import java.util.List;

public interface SecretService {

    Secret createSecret(Secret secret) throws Exception;

    Secret deleteSecret(Secret secret) throws Exception;

    Secret describeSecret(Secret secret) throws Exception;

    Secret getSecretValue(Secret secret) throws Exception;

    Secret updateSecret(Secret secret) throws Exception;

    Secret restoreSecret(Secret secret) throws Exception;

    List<Secret> listSecretVersionIds(Secret secret) throws Exception;

    List<Secret> listSecrets(String creator) throws Exception;

    Secret putSecretValue(Secret secret) throws Exception;

    void autoDeleteSecret() throws Exception;
}
