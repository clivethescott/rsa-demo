package main

import (
	"crypto/rand"
	"crypto/rsa"
	"crypto/sha256"
	"crypto/x509"
	"encoding/base64"
	"encoding/pem"
	"fmt"
	"io/ioutil"
	"log"
)

func readPrivateKey(path string) (*rsa.PrivateKey, error) {

	key, err := ioutil.ReadFile(path)
	if err != nil {
		return nil, fmt.Errorf("read file: %w", err)
	}

	block, _ := pem.Decode(key)
	parseResult, err := x509.ParsePKCS8PrivateKey(block.Bytes)
	if err != nil {
		return nil, fmt.Errorf("parse PKSC#8 priv key: %w", err)
	}

	privateKey, ok := parseResult.(*rsa.PrivateKey)
	if !ok {
		return nil, fmt.Errorf("not an RSA private key: %w", err)
	}

	return privateKey, nil
}

func decrypt(payload string) (string, error) {

	payloadDecoded, err := base64.StdEncoding.DecodeString(payload)
	if err != nil {
		return "", fmt.Errorf("decode base64: %w", err)
	}

	key, err := readPrivateKey("../keys/privateKey.pem")
	if err != nil {
		return "", fmt.Errorf("read priv key: %w", err)
	}

	decrypted, err := rsa.DecryptOAEP(sha256.New(), rand.Reader, key, payloadDecoded, nil)
	if err != nil {
		return "", fmt.Errorf("decrypt OAEP: %w", err)
	}
	return string(decrypted), nil
}

func main() {
	encrypted := "aUFftojpPXtO6aOi6/9lAMc6X9mzLE3exjxZXBHfNfxgBhqbW8eAxW6a/wE84EZWRLj2EbRYweQzBBUctkMJmsw9d9vEQEdXsJwv0WWZZRXxAJgquW0nWatEs5+eD5Fd2yWlu4E1iv7xV977h/uRb2SNeZu/u7op6IkAoelAHaW23BvDtTYQFl9NvaMJqUZaizCJyLVD9hQZAUdh9U7W0JZGoLfIEbOFLyjtGzCdt2tfG0M6h5LTSSPJvrnHRVpvQp8h5YUr/VraFqEPK1ild3oK9qtGrrS/gchwBRuHJwgR4MIzncgOrUahNq1AgWRGTPbQcW8JOPcKzldy4QGguhwXMyXgmW8N2d2tAvUvNrfWZKnlM33f+8GDApmd8dST6gsLmwWKj5CEqGsSEFs9csMVZss2lSkPVpI/Ii1cfUvKXNVQfAEpnY4rnsJQvCTM1B4GdLWAlxGjns4H2TB1485/ylCv1dCpYUPV1mnYQ781Yq6OxIEm7uoSRXfdafJoyoOD9KFUu/KAlCZlXJYc9JnRZmPy+CIKofFDwrE/3MkPTARItkRznUnNSdsYgfNkV+ddn2NhaogI/Mj/8jvoTaaBJIt/aVLRR/z7AwYarTQyOp4oTBUXKe/t8DT+QvXoZmPOqlv6HQzGmG/B6mxy3XJI44LSU8zfYrcclhFHA4k="

	decrypted, err := decrypt(encrypted)
	if err != nil {
		log.Fatalf("failed to decrypt: %v\n", err)
	}

	log.Println("decrypted", decrypted)
}
