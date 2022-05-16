package com.example.pocrsa.api;

import com.example.pocrsa.api.config.FieldEncryptionConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptorImpl implements Encryptor {

    private static final String MGF_NAME = "MGF1";
    private final FieldEncryptionConfig encryptionConfig;

    @Override
    public String encrypt(String text, PublicKey key) throws GeneralSecurityException {

        Cipher encryptCipher = Cipher.getInstance(encryptionConfig.algorithm().cipher());
        // Make it easier for other languages https://stackoverflow.com/questions/55525628/rsa-encryption-with-oaep-between-java-and-javascript
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, getOAEPParameterSpec());
        byte[] cipherText = encryptCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    @Override
    public String decrypt(String text, PrivateKey key) throws GeneralSecurityException {

        Cipher decryptCipher = Cipher.getInstance(encryptionConfig.algorithm().cipher());
        decryptCipher.init(Cipher.DECRYPT_MODE, key, getOAEPParameterSpec());
        byte[] plainText = decryptCipher.doFinal(Base64.getDecoder().decode(text));

        return new String(plainText, StandardCharsets.UTF_8);
    }

    private OAEPParameterSpec getOAEPParameterSpec() {

        // To add a label https://stackoverflow.com/questions/59487777/providing-value-for-label-in-rsa-oaep-encryption-using-java
        return new OAEPParameterSpec(encryptionConfig.algorithm().oaepDigest(),
                MGF_NAME, new MGF1ParameterSpec(encryptionConfig.algorithm().oaepDigest()), PSource.PSpecified.DEFAULT);
    }
}
