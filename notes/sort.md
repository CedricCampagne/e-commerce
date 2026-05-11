# sort = "name,asc"

1) split → ["name", "asc"]
2) sortField = "name"
3) sortDirection = "asc"
4) sorting = Sort.by("name").ascending()
5) Pageable = PageRequest.of(page, size, sorting)
6) findAll(pageable) → Spring applique ORDER BY name ASC
