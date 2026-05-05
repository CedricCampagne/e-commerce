# La relation oneToMany

## Côté “1” (le parent, celui qui n’a PAS la FK dans la table)

Exemple : Category, User, Product (selon le cas)

Dans l’entité côté 1, on met :

```java
@OneToMany(mappedBy = "category")
@JsonIgnore 
private List<Product> products = new ArrayList<>();
```

Caractéristiques :
- Aucune colonne FK dans cette table
- mappedBy = nom du champ dans l’entité enfant
- C’est le côté inverse (non‑propriétaire)
- Optionnel (mais recommandé pour naviguer dans l’objet)
- OneToMany = côté parent = @JsonIgnore

    Pour éviter :
    - boucles infinies
    - JSON trop lourd
    - erreurs 500

## Côté “N” (l’enfant, celui qui contient la FK)

Exemple : Product, CartItem, OrderItem…

Dans l’entité côté N, on met :

```java
@ManyToOne
@JoinColumn(name = "category_id", nullable = false)
private Category category = new ArrayList<>();
```

Caractéristiques :
- La FK est dans cette table (ex : category_id)
- C’est le côté propriétaire de la relation
- Obligatoire pour que la relation existe
- C’est lui qui décide de la valeur de la FK


