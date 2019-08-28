package com.idosinchuk.handlemovies.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;

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
	 * @param pageable  object for pagination
	 * @param assembler object for pagination
	 * @return PagedResources of {@link ActorResponseDTO}
	 */
	@SuppressWarnings("rawtypes")
	public ResponseEntity<PagedResources<ActorResponseDTO>> findAllActors(Pageable pageable,
			PagedResourcesAssembler assembler);

	/**
	 * Retrieve details of an actor by the id.
	 * 
	 * @param id of the actors
	 * @return {@link ActorResponseDTO}
	 */
	public ResponseEntity<ActorResponseDTO> findActorById(Long id);

	/**
	 * Add an actor.
	 * 
	 * @param actorRequestDTO request object
	 * @return {@link ActorResponseDTO}
	 */
	public ResponseEntity<ActorResponseDTO> addActor(ActorRequestDTO actorRequestDTO);

	/**
	 * Delete a actor by the id.
	 * 
	 * @param id of the actor
	 * @return {@link ActorResponseDTO}
	 */
	public ResponseEntity<ActorResponseDTO> deleteActor(Long id);

}
