package com.gallery.app.repository;

import com.gallery.app.model.Perfumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfumerRepository extends JpaRepository<Perfumer, Integer> {
}
