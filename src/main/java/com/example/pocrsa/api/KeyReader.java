package com.example.pocrsa.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyReader {
    PublicKey readPublic() throws IOException, GeneralSecurityException;

    PrivateKey readPrivate() throws IOException, GeneralSecurityException;
}
