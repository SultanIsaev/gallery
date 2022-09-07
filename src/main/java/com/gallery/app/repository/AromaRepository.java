package com.gallery.app.repository;

import com.gallery.app.model.Aroma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AromaRepository extends JpaRepository<Aroma, Integer> {

    List<Aroma> findAromasByPerfumerId(Integer id);
}
