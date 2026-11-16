package org.delcom.ifs25019springbootcashflow.adapter.repository;

import org.delcom.ifs25019springbootcashflow.domain.repository.IRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/** Generic in-memory adapter for unit tests; inject an ID extractor. */
public class InMemoryRepository<T, ID> implements IRepository<T, ID> {
    private final Map<ID, T> data = new ConcurrentHashMap<>();
    private final Function<T, ID> idExtractor;
    public InMemoryRepository(Function<T, ID> idExtractor) { this.idExtractor = idExtractor; }
    public Optional<T> findById(ID id) { return Optional.ofNullable(data.get(id)); }
    public List<T> findAll() { return new ArrayList<>(data.values()); }
    public T save(T entity) { data.put(idExtractor.apply(entity), entity); return entity; }
    public void deleteById(ID id) { data.remove(id); }
}
