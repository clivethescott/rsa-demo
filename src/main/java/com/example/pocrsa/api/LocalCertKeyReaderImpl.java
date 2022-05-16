package com.example.pocrsa.api;

import com.example.pocrsa.api.config.FieldEncryptionConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalCertKeyReaderImpl implements KeyReader {

    private final FieldEncryptionConfig encryptionConfig;


    @Override
    public PublicKey readPublic() throws IOException, GeneralSecurityException {
        byte[] decoded = decode(encryptionConfig.publicKey());

        KeyFactory keyFactory = KeyFactory.getInstance(encryptionConfig.algorithm().key());
        KeySpec keySpec = new X509EncodedKeySpec(decoded);

        return keyFactory.generatePublic(keySpec);
    }

    @Override
    public PrivateKey readPrivate() throws IOException, GeneralSecurityException {
        byte[] decoded = decode(encryptionConfig.privateKey());

        KeyFactory keyFactory = KeyFactory.getInstance(encryptionConfig.algorithm().key());
        KeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        return keyFactory.generatePrivate(keySpec);
    }

    private boolean isHeader(String value) {
        return StringUtils.contains(value, "BEGIN");
    }

    private boolean isFooter(String value) {
        return StringUtils.contains(value, "END");
    }

    private byte[] decode(Resource resource) throws IOException {

        try (var bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String pem = bufferedReader.lines()
                    .filter(value -> !value.isBlank() && !isHeader(value) && !isFooter(value))
                    .collect(Collectors.joining(""));
            return Base64.decodeBase64(pem);
        }
    }
}
