package ru.bratchin.javaCore25.repository.api;

import ru.bratchin.javaCore25.specification.MySpecification;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MyRepository<E, K> {

    Map<K, E> findAll(MySpecification<E> specification);

    Map<K, E> findAll();

    Optional<E> findOne(MySpecification<E> specification);

    void create(List<E> e);

    void create(E e);

/*    void update(E e);

    void update(List<E> e);*/

    void delete(E e);

    void deleteAll();

}
