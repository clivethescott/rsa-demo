import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) throws GeneralSecurityException, IOException {

        String cipherText = "aUFftojpPXtO6aOi6/9lAMc6X9mzLE3exjxZXBHfNfxgBhqbW8eAxW6a/wE84EZWRLj2EbRYweQzBBUctkMJmsw9d9vEQEdXsJwv0WWZZRXxAJgquW0nWatEs5+eD5Fd2yWlu4E1iv7xV977h/uRb2SNeZu/u7op6IkAoelAHaW23BvDtTYQFl9NvaMJqUZaizCJyLVD9hQZAUdh9U7W0JZGoLfIEbOFLyjtGzCdt2tfG0M6h5LTSSPJvrnHRVpvQp8h5YUr/VraFqEPK1ild3oK9qtGrrS/gchwBRuHJwgR4MIzncgOrUahNq1AgWRGTPbQcW8JOPcKzldy4QGguhwXMyXgmW8N2d2tAvUvNrfWZKnlM33f+8GDApmd8dST6gsLmwWKj5CEqGsSEFs9csMVZss2lSkPVpI/Ii1cfUvKXNVQfAEpnY4rnsJQvCTM1B4GdLWAlxGjns4H2TB1485/ylCv1dCpYUPV1mnYQ781Yq6OxIEm7uoSRXfdafJoyoOD9KFUu/KAlCZlXJYc9JnRZmPy+CIKofFDwrE/3MkPTARItkRznUnNSdsYgfNkV+ddn2NhaogI/Mj/8jvoTaaBJIt/aVLRR/z7AwYarTQyOp4oTBUXKe/t8DT+QvXoZmPOqlv6HQzGmG/B6mxy3XJI44LSU8zfYrcclhFHA4k=";
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        decryptCipher.init(Cipher.DECRYPT_MODE, readPrivate(), getOAEPParameterSpec());
        byte[] plainText = decryptCipher.doFinal(Base64.getDecoder().decode(cipherText));

        String decrypted = new String(plainText, StandardCharsets.UTF_8);
        System.out.println("Decrypted = " + decrypted);
    }

    private static OAEPParameterSpec getOAEPParameterSpec() {

        // To add a label https://stackoverflow.com/questions/59487777/providing-value-for-label-in-rsa-oaep-encryption-using-java
        return new OAEPParameterSpec("SHA-256",
                "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
    }

    private static PrivateKey readPrivate() throws IOException, GeneralSecurityException {
        byte[] decoded = decode("/keys/privateKey.pem"); // path may depend on what source root is

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        return keyFactory.generatePrivate(keySpec);
    }

    private static boolean isHeader(String value) {
        return StringUtils.contains(value, "BEGIN");
    }

    private static boolean isFooter(String value) {
        return StringUtils.contains(value, "END");
    }

    private static byte[] decode(String pemPath) throws IOException {

        final InputStream resourceAsStream = Test.class.getResourceAsStream(pemPath);
        Objects.requireNonNull(resourceAsStream, "Resource stream required");

        try (var bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String pem = bufferedReader.lines()
                    .filter(value -> !value.isBlank() && !isHeader(value) && !isFooter(value))
                    .collect(Collectors.joining(""));
            return Base64.getDecoder().decode(pem);
        }
    }

}
