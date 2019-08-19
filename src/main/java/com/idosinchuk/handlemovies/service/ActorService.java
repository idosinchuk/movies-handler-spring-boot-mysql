package com.idosinchuk.handlemovies.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.idosinchuk.handlemovies.dto.ActorRequestDTO;
import com.idosinchuk.handlemovies.dto.ActorResponseDTO;

/**
 * 
 * Service for manage actors
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ActorService {

	/**
	 * Retrieve list of all actors.
	 *
	 * @param pageable param for pagination
	 * @return Page of {@link ActorResponseDTO}
	 */
	public Page<ActorResponseDTO> findAllActors(Pageable pageable);

	/**
	 * Retrieve details of an actor by the id.
	 * 
	 * @param id of the actors
	 * @return {@link ActorResponseDTO}
	 */
	public ActorResponseDTO findActorById(Long id);

	/**
	 * Add an actor.
	 * 
	 * @param actorRequestDTO request object
	 * @return {@link ActorResponseDTO}
	 */
	public ActorResponseDTO addActor(ActorRequestDTO actorRequestDTO);

	/**
	 * Delete a actor by the id.
	 * 
	 * @param id of the actor
	 * @return response
	 */
	public void deleteActor(Long id);

}
