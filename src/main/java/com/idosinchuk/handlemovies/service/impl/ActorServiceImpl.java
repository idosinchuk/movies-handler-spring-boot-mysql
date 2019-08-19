package com.idosinchuk.handlemovies.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.idosinchuk.handlemovies.dto.ActorRequestDTO;
import com.idosinchuk.handlemovies.dto.ActorResponseDTO;
import com.idosinchuk.handlemovies.entity.ActorEntity;
import com.idosinchuk.handlemovies.repository.ActorRepository;
import com.idosinchuk.handlemovies.service.ActorService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation for manage actors
 * 
 * @author Igor Dosinchuk
 *
 */
@Slf4j
@Service
public class ActorServiceImpl implements ActorService {

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ActorResponseDTO> findAllActors(Pageable pageable) {

		Page<ActorEntity> entityResponse = actorRepository.findAll(pageable);
		// log.debug("The result of finding all the actors from database was: " +
		// actors);

		// Convert Entity response to DTO
		Page<ActorResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<ActorResponseDTO>>() {
		}.getType());

		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	public ActorResponseDTO findActorById(Long id) {

		ActorResponseDTO response = null;

		log.debug("Call to find info about the actor into database by the id: " + id);

		// Find actor information by id
		Optional<ActorEntity> entityResponse = actorRepository.findById(id);
		// log.debug("The result of finding info about the actor by the id was: " +
		// entityResponse);

		if (entityResponse.isPresent()) {
			ActorEntity existingActor = entityResponse.get();

			// Convert Entity response to DTO
			return response = modelMapper.map(existingActor, new TypeToken<ActorResponseDTO>() {
			}.getType());
		} else {
			log.debug("There is no actor in the repo with the id: " + id);
			return response;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public ActorResponseDTO addActor(ActorRequestDTO actorRequestDTO) {

		ActorEntity entityRequest = modelMapper.map(actorRequestDTO, ActorEntity.class);

		// Save the new actor
		log.debug("Call to save into database the new actor with the request: " + entityRequest);
		ActorEntity entityResponse = actorRepository.save(entityRequest);
		log.debug("The actor was saved successfully");

		return modelMapper.map(entityResponse, ActorResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteActor(Long id) {

		log.debug("Call to database to delete the actor by the id: " + id);
		actorRepository.deleteById(id);
		log.debug("The actor was deleted successfully");

	}

}
