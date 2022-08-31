package com.gallery.app.repository;

import com.gallery.app.model.Aroma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AromaRepository extends JpaRepository<Aroma, Integer> {
}
