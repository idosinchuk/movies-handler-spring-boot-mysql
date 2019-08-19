package com.idosinchuk.handlemovies.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	@Cacheable // caches the result of findAllGenres() method
	public Page<GenreResponseDTO> findAllGenres(Pageable pageable) {

		Page<GenreEntity> entityResponse = genreRepository.findAll(pageable);
		// log.debug("The result of finding all the genres from database was: " +
		// entityResponse);

		// Convert Entity response to DTO
		Page<GenreResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<GenreResponseDTO>>() {
		}.getType());

		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Cacheable // caches the result of findGenreById() method
	public GenreResponseDTO findGenreById(Long id) {

		GenreResponseDTO response = null;

		log.debug("Call to find info about the genre into database by the id: " + id);

		// Find movie information by id
		Optional<GenreEntity> entityResponse = genreRepository.findById(id);
		log.debug("The result of finding info about the genre by the id was: " + entityResponse);

		if (entityResponse.isPresent()) {
			GenreEntity existingGenre = entityResponse.get();

			// Convert Entity response to DTO
			return response = modelMapper.map(existingGenre, new TypeToken<GenreResponseDTO>() {
			}.getType());
		} else {
			log.debug("There is no genre in the repo with the id: " + id);
			return response;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public GenreResponseDTO addGenre(GenreRequestDTO genreRequestDTO) {

		GenreEntity entityRequest = modelMapper.map(genreRequestDTO, GenreEntity.class);

		// Save the new genre
		log.debug("Call to save into database the new genre with the request: " + entityRequest);
		GenreEntity entityResponse = genreRepository.save(entityRequest);
		log.debug("The genre was saved successfully");

		return modelMapper.map(entityResponse, GenreResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteGenre(Long id) {

		log.debug("Call to database to delete the genrex by the id: " + id);
		genreRepository.deleteById(id);
		log.debug("The genre was deleted successfully");
	}

}
