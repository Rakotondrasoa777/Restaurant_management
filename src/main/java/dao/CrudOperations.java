package dao;

import java.util.List;

public interface CrudOperations<E> {
    List<E> getAll(int page, int size);
    E findByName(String name);
    List<E> saveAll(List<E> entities);
}
