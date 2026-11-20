import java.util.*;
public interface Dao<T,K> {
    int insert(T entity) throws Exception;
    Optional<T> findById(K id) throws Exception;
    List<T> findAll() throws Exception;
    boolean update(T entity) throws Exception;
    boolean delete(K id) throws Exception;
}