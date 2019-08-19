package com.idosinchuk.handlemovies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idosinchuk.handlemovies.entity.GenreEntity;

/**
 * Repository for manage genres
 * 
 * @author Igor Dosinchuk
 *
 */
@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

}
