package com.example.pocrsa;

import com.example.pocrsa.api.Encryptor;
import com.example.pocrsa.api.KeyReader;
import com.example.pocrsa.api.config.FieldEncryptionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.security.PrivateKey;
import java.security.PublicKey;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties(FieldEncryptionConfig.class)
public class PocRsaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocRsaApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(Encryptor encryptor, KeyReader keyReader) {
        return args -> {

            PublicKey publicKey = keyReader.readPublic();
            PrivateKey privateKey = keyReader.readPrivate();
            String text = "clive";

            String encrypted = encryptor.encrypt(text, publicKey);
            copyToClipboard(encrypted);
            log.info("Encrypted data {}", encrypted);

            String decrypted = encryptor.decrypt(encrypted, privateKey);
            log.info("Decrypted data {}", decrypted);
        };
    }

    private void copyToClipboard(String encrypted) {
        System.setProperty("java.awt.headless", "false");

        var stringSelection = new StringSelection(encrypted);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
