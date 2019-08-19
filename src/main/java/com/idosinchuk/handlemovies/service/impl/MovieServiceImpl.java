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

import com.idosinchuk.handlemovies.dto.MovieRequestDTO;
import com.idosinchuk.handlemovies.dto.MovieResponseDTO;
import com.idosinchuk.handlemovies.entity.MovieEntity;
import com.idosinchuk.handlemovies.repository.MovieRepository;
import com.idosinchuk.handlemovies.service.MovieService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation for manage movies
 * 
 * @author Igor Dosinchuk
 *
 */
@Slf4j
@Service
@CacheConfig(cacheNames = { "movies" }) // tells Spring where to store cache for this class
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	@Cacheable // caches the result of findAllMovies() method
	public Page<MovieResponseDTO> findAllMovies(Pageable pageable) {

		Page<MovieEntity> entityResponse = movieRepository.findAll(pageable);
		// log.debug("The result of finding all the movies from database was: " +
		// movies);

		// Convert Entity response to DTO
		Page<MovieResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<MovieResponseDTO>>() {
		}.getType());

		return response;

	}

	/**
	 * {@inheritDoc}
	 */
	@Cacheable // caches the result of findMovieById() method
	public MovieResponseDTO findMovieById(Long id) {

		MovieResponseDTO response = null;

		log.debug("Call to find info about the movie into database by the id: " + id);

		// Find movie information by id
		Optional<MovieEntity> entityResponse = movieRepository.findById(id);
		// log.debug("The result of finding info about the movie by the id was: " +
		// movie);

		if (entityResponse.isPresent()) {
			MovieEntity existingMovie = entityResponse.get();

			// Convert Entity response to DTO
			return response = modelMapper.map(existingMovie, new TypeToken<MovieResponseDTO>() {
			}.getType());
		} else {
			log.debug("There is no actor in the repo with the id: " + id);
			return response;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public MovieResponseDTO addMovie(MovieRequestDTO movieRequestDTO) {

		MovieEntity actorEntity = modelMapper.map(movieRequestDTO, MovieEntity.class);

		// Save the new movie
		log.debug("Call to save into database the new movie with the request: " + actorEntity);
		MovieEntity entityResponse = movieRepository.save(actorEntity);
		log.debug("The movie was saved successfully");

		return modelMapper.map(entityResponse, MovieResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteMovie(Long id) {

		log.debug("Call to database to delete the movie by the id: " + id);
		movieRepository.deleteById(id);
		log.debug("The movie was deleted successfully");
	}

}
