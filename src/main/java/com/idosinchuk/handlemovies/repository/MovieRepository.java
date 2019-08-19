package com.idosinchuk.handlemovies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idosinchuk.handlemovies.entity.MovieEntity;

/**
 * Repository for manage movies
 * 
 * @author Igor Dosinchuk
 *
 */
@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

}
