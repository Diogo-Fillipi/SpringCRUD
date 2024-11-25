package org.example.crud.controller;
import org.apache.coyote.Response;
import org.example.crud.model.Plants;
import org.example.crud.repository.PlantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/plants")
public class PlantController {
    private final PlantRepository plantRepository;
    public PlantController(final PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }



    @GetMapping("/search/all")
    public ResponseEntity<?> searchAllPlants(){
        Iterable<Plants> plantsList = this.plantRepository.findAll();
        if(!plantsList.iterator().hasNext()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(plantsList);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Plants> searchPlantById(@PathVariable(name = "id") Integer id){
        Optional<Plants> plantById = this.plantRepository.findById(id);
        if(!plantById.isPresent()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(plantById.get());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> findByAttribute(@RequestParam(name="name", required = false) String name, @RequestParam(name="hasFruit", required = false) Boolean hasFruit){
        if(name != null && Boolean.TRUE.equals(hasFruit)){
            return ResponseEntity.ok(this.plantRepository.findByNameAndHasFruitTrue(name));
        }
        if(name != null && Boolean.FALSE.equals(hasFruit)){
            return ResponseEntity.ok(this.plantRepository.findByNameAndHasFruitFalse(name));
        }
        if(name != null){
            return ResponseEntity.ok(this.plantRepository.findByName(name));
        }
        if(Boolean.TRUE.equals(hasFruit)){
            return ResponseEntity.ok(this.plantRepository.findByHasFruitTrue());
        }
        if(Boolean.FALSE.equals(hasFruit)){
            return ResponseEntity.ok(this.plantRepository.findByHasFruitFalse());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Plants> createPlant(@RequestBody Plants plant){
        Plants newPlant = this.plantRepository.save(plant);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPlant.getId())
                .toUri();
        return ResponseEntity.created(location).body(newPlant);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Plants> updateById(@PathVariable(name = "id") Integer id, @RequestBody Plants plants){
        Optional<Plants> plantToCompare = this.plantRepository.findById(id);

        if(plantToCompare.isPresent()){
            Plants newPlant = plantToCompare.get();
            newPlant.setName(plants.getName());
            newPlant.setHasFruit(plants.getHasFruit());
            this.plantRepository.save((newPlant));
            return ResponseEntity.ok(newPlant);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable(name = "id") Integer id){
        if(!this.plantRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        this.plantRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
