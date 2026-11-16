package org.delcom.ifs25019springbootcashflow.domain.repository;
import java.util.*;
public interface IRepository<T,ID> { Optional<T> findById(ID id); List<T> findAll(); T save(T entity); void deleteById(ID id); }
