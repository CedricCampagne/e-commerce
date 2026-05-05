# OneToOne

## Côté propriétaire (celui qui a la FK)

```java
@OneToOne
@JoinColumn(name = "user_id")
private User user;
```

- La FK est ici
- C’est le côté qui “possède” la relation

## Côté inverse (pas de FK)

```java
@OneToOne(mappedBy = "user")
private Profile profile;
```

- Pas de FK
- mappedBy obligatoire

## Notes rapides

- Rare en e‑commerce
- Souvent utilisé pour : profil utilisateur, adresse, détails étendus
- Peut être optional = false si obligatoire