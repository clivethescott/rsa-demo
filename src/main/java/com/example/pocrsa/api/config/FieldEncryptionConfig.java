package com.example.pocrsa.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "field-encryption")
public record FieldEncryptionConfig(@NestedConfigurationProperty Algorithm algorithm,
                                    Resource publicKey,
                                    Resource privateKey) {
}
