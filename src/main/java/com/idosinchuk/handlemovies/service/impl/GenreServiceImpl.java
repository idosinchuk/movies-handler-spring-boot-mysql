package com.idosinchuk.handlemovies.service.impl;

import java.util.Objects;
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

import com.idosinchuk.handlemovies.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.dto.GenreResponseDTO;
import com.idosinchuk.handlemovies.entity.GenreEntity;
import com.idosinchuk.handlemovies.repository.GenreRepository;
import com.idosinchuk.handlemovies.service.GenreService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation for manage genres
 * 
 * @author Igor Dosinchuk
 *
 */
@Slf4j
@Service
@CacheConfig(cacheNames = { "genres" }) // tells Spring where to store cache for this class
public class GenreServiceImpl implements GenreService {

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Cacheable // caches the result of findAllGenres() method
	public ResponseEntity<PagedResources<GenreResponseDTO>> findAllGenres(Pageable pageable,
			PagedResourcesAssembler assembler) {

		Page<GenreEntity> entityResponse = genreRepository.findAll(pageable);

		if (Objects.isNull(entityResponse)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		// Convert Entity response to DTO
		Page<GenreResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<GenreResponseDTO>>() {
		}.getType());

		return new ResponseEntity<>(assembler.toResource(response), HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@Cacheable // caches the result of findGenreById() method
	public ResponseEntity<GenreResponseDTO> findGenreById(Long id) {

		GenreResponseDTO response = null;

		log.debug("Call to find info about the genre into database by the id: " + id);

		// Find movie information by id
		Optional<GenreEntity> entityResponse = genreRepository.findById(id);
		log.debug("The result of finding info about the genre by the id was: " + entityResponse);

		if (entityResponse.isPresent()) {
			GenreEntity existingGenre = entityResponse.get();

			// Convert Entity response to DTO
			response = modelMapper.map(existingGenre, new TypeToken<GenreResponseDTO>() {
			}.getType());

			return new ResponseEntity<>(response, HttpStatus.OK);

		} else {
			log.debug("There is no genre in the repo with the id: " + id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<GenreResponseDTO> addGenre(GenreRequestDTO genreRequestDTO) {

		GenreEntity entityRequest = modelMapper.map(genreRequestDTO, GenreEntity.class);

		// Save the new genre
		log.debug("Call to save into database the new genre with the request: " + entityRequest);
		GenreEntity entityResponse = genreRepository.save(entityRequest);
		log.debug("The genre was saved successfully");

		GenreResponseDTO response = modelMapper.map(entityResponse, GenreResponseDTO.class);

		// Clear cache after adding the movie.
		evictCache();

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return
	 */
	public ResponseEntity<GenreResponseDTO> deleteGenre(Long id) {

		GenreResponseDTO response = new GenreResponseDTO();

		log.debug("Call to database to delete the genrex by the id: " + id);
		genreRepository.deleteById(id);
		log.debug("The genre was deleted successfully");

		// Clear cache after deleting the movie.
		evictCache();

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@Scheduled(fixedDelay = 3600000)
	@CacheEvict(value = "genres", allEntries = true)
	public void evictCache() {
		log.debug("Evicting all entries from movies.");
	}
}
