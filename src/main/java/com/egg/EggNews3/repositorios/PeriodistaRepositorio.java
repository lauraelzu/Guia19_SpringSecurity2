package com.egg.EggNews3.repositorios;

import com.egg.EggNews3.entidades.Periodista;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PeriodistaRepositorio extends JpaRepository<Periodista, String>{
    
}
