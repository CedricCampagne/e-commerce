# Clé secrète

## Méthode recommandée (pro) : générer une clé Base64 de 256 bits

Tu ouvres un terminal (PowerShell, Bash, Git Bash, peu importe) et tu tapes :

- Générer une clé 256 bits (HS256)

```bash
openssl rand -base64 32
```

- Générer une clé 512 bits (HS512)

```bash
openssl rand -base64 64
```

**Résultat : une clé ultra solide, parfaite pour JWT**.

- Exemple de sortie :

```bash
k2JH9sP9z0Q3x8L1sA9FJ3k2H8sP0xL9q3JH2sP9xL0=
```

| Algo JWT | Taille clé recommandée |
|----------|--------------------------|
| HS256    | 32 bytes (256 bits)     |
| HS384    | 48 bytes (384 bits)     |
| HS512    | 64 bytes (512 bits)     |


## Méthode alternative (Java) : générer une clé dans un main()

Si on veut rester 100% Java :

```java
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyGen {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        System.out.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }
}
```


## avec JJWT 0.11.x et sup 

Il faut signer la clé comme ca :

```java
private SecretKey getSigningKey() {
        //lit cette chaîne
        //la décode en tableau de bytes byte[] ces bytes représentent la vraie clé HMAC que tu as générée avec openssl rand -base64 32.
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
```