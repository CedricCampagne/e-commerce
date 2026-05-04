# Les annotations de validation (Bean Validation / Jakarta Validation)

## Dans le DTO, on peut ajouter des contraintes :

```java
public record UserCreateDto(
    @NotBlank(message = "Le username est obligatoire")
    String username,

    @NotBlank(message = "Le mot de passe est obligatoire")
    String password,

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    String email
) {}
```

Ces annotations ne font rien toutes seules.
Elles indiquent juste : “ce champ doit respecter telle règle”.

## ``@Valid`` active la validation dans le controller

Dans le controller :

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public UserDto createUser(@Valid @RequestBody UserCreateDto dto) {
    return userService.createUser(dto);
}
```

``@Valid``, Spring va :
- Lire le JSON reçu
- Le convertir en UserCreateDto
- Vérifier toutes les annotations du DTO
- Si une règle n’est pas respectée → Spring bloque la requête
- Et renvoie automatiquement une erreur 400 Bad Request avec les messages

Rien à coder soi-même.

## Résumé Simple

| Élément                     | Rôle                                           |
|-----------------------------|------------------------------------------------|
| `@NotBlank`, `@Email`, etc. | Définissent les règles de validation           |
| `@Valid`                    | Active la validation automatique dans Spring   |
| Spring                      | Vérifie les règles et renvoie 400 si erreur   |
| Service                     | Reçoit uniquement des DTO valides             |
