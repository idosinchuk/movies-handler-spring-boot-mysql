package com.idosinchuk.handlemovies.microservice.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.idosinchuk.handlemovies.microservice.dto.ActorRequestDTO;
import com.idosinchuk.handlemovies.microservice.dto.ActorResponseDTO;
import com.idosinchuk.handlemovies.microservice.entity.ActorEntity;
import com.idosinchuk.handlemovies.microservice.repository.ActorRepository;
import com.idosinchuk.handlemovies.microservice.service.ActorService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation for manage actors
 * 
 * @author Igor Dosinchuk
 *
 */
@Slf4j
@Service
@CacheConfig(cacheNames = { "actors" }) // tells Spring where to store cache for this class
public class ActorServiceImpl implements ActorService {

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Cacheable // caches the result of getActors() method
	public ResponseEntity<PagedResources<ActorResponseDTO>> getActors(Pageable pageable,
			PagedResourcesAssembler assembler) {

		log.info("Fetching all actors");

		Page<ActorEntity> entityResponse = actorRepository.findAll(pageable);

		// Convert Entity response to DTO
		Page<ActorResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<ActorResponseDTO>>() {
		}.getType());

		return new ResponseEntity<>(assembler.toResource(response), HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<ActorResponseDTO> getActor(Long id) {

		ActorResponseDTO response = null;

		log.info("Call to find info about the actor into database by the ID {}" + id);

		// Find actor information by id
		Optional<ActorEntity> entityResponse = actorRepository.findById(id);

		if (entityResponse.isPresent()) {
			ActorEntity existingActor = entityResponse.get();

			// Convert Entity response to DTO
			response = modelMapper.map(existingActor, new TypeToken<ActorResponseDTO>() {
			}.getType());

			return new ResponseEntity<>(response, HttpStatus.OK);

		} else {
			log.error("There is no actor in the repo with the ID {} " + id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<ActorResponseDTO> addActor(ActorRequestDTO actorRequestDTO) {

		ActorEntity entityRequest = modelMapper.map(actorRequestDTO, ActorEntity.class);

		// Save the new actor
		log.info("Call to save into database the new actor with the request {} " + entityRequest);
		ActorEntity entityResponse = actorRepository.save(entityRequest);

		// Convert entity response to DTO
		ActorResponseDTO response = modelMapper.map(entityResponse, ActorResponseDTO.class);

		// Clear cache after adding the movie.
		evictCache();

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<ActorResponseDTO> deleteActor(Long id) {

		ActorResponseDTO response = new ActorResponseDTO();

		log.info("Call to database to delete the actor by the ID {}" + id);
		actorRepository.deleteById(id);

		// Clear cache after deleting the movie.
		evictCache();

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@Scheduled(fixedDelay = 3600000)
	@CacheEvict(value = "actors", allEntries = true)
	public void evictCache() {
		log.debug("Evicting all entries from actors.");
	}

}
