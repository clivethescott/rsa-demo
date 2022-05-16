package com.example.pocrsa.api;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface Encryptor {
    String encrypt(String text, PublicKey key) throws GeneralSecurityException;

    String decrypt(String text, PrivateKey key) throws GeneralSecurityException;
}
