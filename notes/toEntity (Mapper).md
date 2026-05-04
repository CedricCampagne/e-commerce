# toEntity

``toEntity()`` est un constructeur propre, sans logique métier

Il fait juste :
- DTO → Entity
- sans validation
- sans sécurité
- sans règles métier

C’est exactement ce qu’on veut.

```java
public static User toEntity(UserCreateDto dto) {
    return User.builder()
        .username(dto.username())
        .email(dto.email())
        .password(dto.password())
        .role("USER") // rôle par défaut
        .build();
}
```

1. ``User.builder()``

Avec Lombok on use builder.
C’est la manière la plus propre de créer une entité.

2. ``.username(dto.username())``

On prends la valeur envoyée par le front.  
Le DTO est la source de vérité pour la création.  

3. ``.email(dto.email())``

Même logique :  
Le front envoie l’email => dans l’entité.

4. ``.password(dto.password())``

Attention : on encodera le password dans le service, pas dans le mapper.

- Le mapper ne doit pas contenir de logique métier
- Le service gère la sécurité
- Le mapper ne fait que transformer des données

Donc dans ton service tu feras :

```java
user.setPassword(passwordEncoder.encode(dto.password()));
```

5. ``.role("USER")``

Très important :
- Le front ne doit jamais choisir le rôle
- Le rôle est défini par le backend
- On peut mettre "USER" par défaut
- Plus tard, faire un endpoint admin pour changer le rôle

6. ``.build()``

Construire l’objet final.