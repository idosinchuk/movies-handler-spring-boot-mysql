package com.idosinchuk.handlemovies.service.impl;

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

		log.info("Fetching all genres");

		Page<GenreEntity> entityResponse = genreRepository.findAll(pageable);

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

		log.info("Call to find info about the genre into database by the ID {}" + id);

		// Find movie information by id
		Optional<GenreEntity> entityResponse = genreRepository.findById(id);

		if (entityResponse.isPresent()) {
			GenreEntity existingGenre = entityResponse.get();

			// Convert Entity response to DTO
			response = modelMapper.map(existingGenre, new TypeToken<GenreResponseDTO>() {
			}.getType());

			return new ResponseEntity<>(response, HttpStatus.OK);

		} else {
			log.error("There is no genre in the repo with the ID {}" + id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<GenreResponseDTO> addGenre(GenreRequestDTO genreRequestDTO) {

		GenreEntity entityRequest = modelMapper.map(genreRequestDTO, GenreEntity.class);

		// Save the new genre
		log.debug("Call to save into database the new genre with the request {}" + entityRequest);
		GenreEntity entityResponse = genreRepository.save(entityRequest);

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

		log.info("Call to database to delete the genre by the ID {}" + id);
		genreRepository.deleteById(id);

		// Clear cache after deleting the movie.
		evictCache();

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@Scheduled(fixedDelay = 3600000)
	@CacheEvict(value = "genres", allEntries = true)
	public void evictCache() {
		log.info("Evicting all entries from genres.");
	}
}
