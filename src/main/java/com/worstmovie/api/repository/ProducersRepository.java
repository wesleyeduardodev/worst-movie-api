package com.worstmovie.api.repository;
import com.worstmovie.api.model.Producer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProducersRepository extends CrudRepository<Producer, Long> {
    Optional<Producer> findByName(String name);
}