import base64
from typing import Any
from cryptography.hazmat.primitives import hashes, serialization
from cryptography.hazmat.primitives.asymmetric import padding


def load_private_key(pem_path: str) -> Any:
    with open(pem_path, 'rb') as f:
        private_key = serialization.load_pem_private_key(
            f.read(), password=None)
        return private_key


def decrypt(encrypted: str) -> str:
    private_key = load_private_key("../keys/privateKey.pem")
    b: bytes = private_key.decrypt(
        ciphertext=base64.b64decode(encrypted),
        padding=padding.OAEP(
            mgf=padding.MGF1(algorithm=hashes.SHA256()),
            algorithm=hashes.SHA256(),
            label=None
        )
    )
    return b.decode()


def main():
    encrypted = "aUFftojpPXtO6aOi6/9lAMc6X9mzLE3exjxZXBHfNfxgBhqbW8eAxW6a/wE84EZWRLj2EbRYweQzBBUctkMJmsw9d9vEQEdXsJwv0WWZZRXxAJgquW0nWatEs5+eD5Fd2yWlu4E1iv7xV977h/uRb2SNeZu/u7op6IkAoelAHaW23BvDtTYQFl9NvaMJqUZaizCJyLVD9hQZAUdh9U7W0JZGoLfIEbOFLyjtGzCdt2tfG0M6h5LTSSPJvrnHRVpvQp8h5YUr/VraFqEPK1ild3oK9qtGrrS/gchwBRuHJwgR4MIzncgOrUahNq1AgWRGTPbQcW8JOPcKzldy4QGguhwXMyXgmW8N2d2tAvUvNrfWZKnlM33f+8GDApmd8dST6gsLmwWKj5CEqGsSEFs9csMVZss2lSkPVpI/Ii1cfUvKXNVQfAEpnY4rnsJQvCTM1B4GdLWAlxGjns4H2TB1485/ylCv1dCpYUPV1mnYQ781Yq6OxIEm7uoSRXfdafJoyoOD9KFUu/KAlCZlXJYc9JnRZmPy+CIKofFDwrE/3MkPTARItkRznUnNSdsYgfNkV+ddn2NhaogI/Mj/8jvoTaaBJIt/aVLRR/z7AwYarTQyOp4oTBUXKe/t8DT+QvXoZmPOqlv6HQzGmG/B6mxy3XJI44LSU8zfYrcclhFHA4k="

    plain_text = decrypt(encrypted)
    print("Decrypted text:", plain_text)


if __name__ == "__main__":
    main()
