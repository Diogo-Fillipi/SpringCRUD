package org.example.crud.repository;

import org.example.crud.model.Plants;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlantRepository extends CrudRepository<Plants, Integer> {
    public List<Plants> findByHasFruitTrue();
    public List<Plants> findByHasFruitFalse();
    public List<Plants> findByName(String name);
    public List<Plants> findByNameAndHasFruitTrue(String name);
    public List<Plants> findByNameAndHasFruitFalse(String name);
}
