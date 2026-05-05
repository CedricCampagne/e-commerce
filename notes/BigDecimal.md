# les opérations avec ``BigDecimal`` essentielles

1. Addition

```java
total = total.add(value);
```

2️. Soustraction

```java
total = total.subtract(value);
```

3. Multiplication

```java
total = total.multiply(value);
```

4. Division
Attention : division doit souvent préciser un arrondi

```java
total = total.divide(value, RoundingMode.HALF_UP);
```

## Conversion d’un int → BigDecimal

On a utilisé :

```java
BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
```

C’est la meilleure façon.

Ne jamais utiliser ``new BigDecimal(double)``.

## Initialiser a 0 un BigDecimal

``BigDecimal.ZERO`` est la référence standard dans tous les projets pro.

```java
BigDecimal total = BigDecimal.ZERO;
```

