# ``@Builder``

``@Builder`` n’est jamais obligatoire, mais c’est un outil qui devient super utile dès que tu montes en niveau  
et que tes entités commencent à être utilisées dans des services, des DTO, des tests, etc.


## 1. Création d’objets dans les services

Sans builder :

```java
User u = new User();
u.setUsername("Alice");
u.setEmail("alice@example.com");
u.setPassword("1234");
```

Avec builder :

```java
User u = User.builder()
    .username("Alice")
    .email("alice@example.com")
    .password("1234")
    .build();
```

- plus lisible
- plus propre
- moins de lignes
- moins d’erreurs

## 2. Valeur par default

Dans une entité si tu passe une valeru par défaut il faut utiliser ``@Builder.Default``

```java
@Builder.Default
@Column(nullable = false)
private String role = "USER";
```