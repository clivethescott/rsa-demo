# Generates PKCS#8 formatted key in PEM format
openssl genpkey -out privateKey.pem -algorithm RSA -pkeyopt rsa_keygen_bits:4096
