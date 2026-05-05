# ManyToMany

## Avec table de jointure (recommandé)

```java
@ManyToMany
@JoinTable(
    name = "product_tags",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
)
private List<Tag> tags = new ArrayList<>();
```

## Côté inverse

```java
@ManyToMany(mappedBy = "tags")
private List<Product> products = new ArrayList<>();
```

## Notes rapides

- Utilisé pour : tags, rôles, catégories multiples
- Toujours une table intermédiaire
- Attention aux boucles JSON => souvent @JsonIgnore d’un côté



# Pense‑bête ManyToMany avec table de jonction (clé composite)

1. Toujours créer une table intermédiaire

Exemple : product_tags

```bash
product_tags
-------------
product_id (FK → products.id)
tag_id     (FK → tags.id)
PRIMARY KEY (product_id, tag_id)
```

C’est la version la plus propre, la plus performante, la plus maîtrisable.

2. Créer une entité pour la table de jonction (recommandé)

Exemple :

```java
@Entity
@Table(name = "product_tags")
public class ProductTag {

    @EmbeddedId
    private ProductTagId id;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    @ManyToOne
    @MapsId("tagId")
    private Tag tag;
}
```

Et la clé composite :

```java
@Embeddable
public class ProductTagId implements Serializable {
    private Long productId;
    private Long tagId;
}
```

- fait dans un autre projet(site-auteur-api).
- C’est la version pro, utilisée dans les vraies applis.

3. Ne pas utiliser @ManyToMany direct si tu veux garder le contrôle

@ManyToMany simple :

```java
@ManyToMany
private List<Tag> tags;
```

- Facile mais limité  
- Impossible d’ajouter des colonnes dans la table de jonction (ex : date d’ajout, ordre, metadata)
- Moins flexible
- Risque de boucles JSON

4. Quand utiliser quoi ?

- ManyToMany simple
    - Pour des relations très simples
    - Sans colonnes supplémentaires
    - Exemple : rôles d’un utilisateur

- Table de jonction + entité + clé composite
    - Pour des relations évolutives
    - Quand on veut ajouter des colonnes (quantité, date, etc.)
    - Exemple :
        - product_tags
        - order_items
        - cart_items (d’ailleurs c’est un ManyToMany déguisé en OneToMany/ManyToOne)

## Résumé express (à garder sous la main)

ManyToMany simple :
- @ManyToMany + @JoinTable
- Pas de colonne supplémentaire

ManyToMany avancé :
- Table de jonction avec PK composite
- Entité dédiée + @EmbeddedId
- @ManyToOne + @MapsId
- Plus flexible et plus propre