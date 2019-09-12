package com.idosinchuk.handlemovies.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idosinchuk.handlemovies.microservice.entity.ActorEntity;

/**
 * Repository for manage actors
 * 
 * @author Igor Dosinchuk
 *
 */
@Repository
public interface ActorRepository extends JpaRepository<ActorEntity, Long> {

}
