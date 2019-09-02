package com.idosinchuk.handlemovies.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import com.idosinchuk.handlemovies.dto.ActorRequestDTO;
import com.idosinchuk.handlemovies.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.dto.MovieRequestDTO;
import com.idosinchuk.handlemovies.dto.MovieResponseDTO;
import com.idosinchuk.handlemovies.entity.ActorEntity;
import com.idosinchuk.handlemovies.entity.GenreEntity;
import com.idosinchuk.handlemovies.entity.MovieEntity;
import com.idosinchuk.handlemovies.repository.ActorRepository;
import com.idosinchuk.handlemovies.repository.GenreRepository;
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
	private ActorRepository actorRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Cacheable // caches the result of getMovies() method
	public ResponseEntity<PagedResources<MovieResponseDTO>> getMovies(Pageable pageable,
			PagedResourcesAssembler assembler) {

		log.info("Fetching all movies");

		Page<MovieEntity> entityResponse = movieRepository.findAll(pageable);

		// Convert Entity response to DTO
		Page<MovieResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<MovieResponseDTO>>() {
		}.getType());

		return new ResponseEntity<>(assembler.toResource(response), HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@Cacheable // caches the result of getMovie() method
	public ResponseEntity<MovieResponseDTO> getMovie(Long id) {

		MovieResponseDTO movieResponseDTO;

		log.debug("Call to find info about the movie into database by the id: " + id);

		// Find movie information by id
		Optional<MovieEntity> entityResponse = movieRepository.findById(id);

		if (entityResponse.isPresent()) {
			MovieEntity existingMovie = entityResponse.get();

			// Convert Entity response to DTO
			movieResponseDTO = modelMapper.map(existingMovie, new TypeToken<MovieResponseDTO>() {
			}.getType());

			return new ResponseEntity<>(movieResponseDTO, HttpStatus.OK);

		} else {
			log.error("There is no actor in the repo with the id {}" + id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<MovieResponseDTO> addMovie(MovieRequestDTO movieRequestDTO) {
		MovieResponseDTO response = new MovieResponseDTO();

		// Check if movie request contains duplicates actors and genres
		deleteDuplicatesActorsAndGenres(movieRequestDTO, movieRequestDTO.getActors(), movieRequestDTO.getGenres());

		MovieEntity movieEntityRequest = modelMapper.map(movieRequestDTO, MovieEntity.class);

		log.info("Call to find actors into database");
		List<ActorEntity> actorEntityListFromDB = actorRepository.findAll();

		// Check if the requested actor exists in the database, if not exists delete it
		// from
		// request
		List<ActorEntity> actorRequestList = deleteNonExistentActors(actorEntityListFromDB,
				movieEntityRequest.getActors());

		// If there is no actor present in the database, we return Bad Request
		if (actorRequestList.isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		log.info("Call to find genres into database");
		List<GenreEntity> genreEntityListFromDB = genreRepository.findAll();

		// Check if the requested genre exists in the database, if not exists delete it
		// from
		// request
		List<GenreEntity> genreRequestList = deleteNonExistentGenres(genreEntityListFromDB,
				movieEntityRequest.getGenres());

		// If there is no genre present in the database, we return Bad Request
		if (genreRequestList.isEmpty()) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Save the new movie
		log.info("Call to save into database the new movie with the request: " + movieEntityRequest);
		MovieEntity movieEntityResponse = movieRepository.save(movieEntityRequest);

		response = modelMapper.map(movieEntityResponse, MovieResponseDTO.class);

		// Clear cache after adding the movie.
		evictCache();

		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<MovieResponseDTO> deleteMovie(Long id) {

		MovieResponseDTO response = new MovieResponseDTO();

		log.debug("Call to database to delete the movie by the id: " + id);
		movieRepository.deleteById(id);
		log.debug("The movie was deleted successfully");

		// Clear cache after deleting the movie.
		evictCache();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Scheduled(fixedDelay = 3600000)
	@CacheEvict(value = "movies", allEntries = true)
	public void evictCache() {
		log.debug("Evicting all entries from movies.");
	}

	private static MovieRequestDTO deleteDuplicatesActorsAndGenres(MovieRequestDTO movieRequestDTO,
			List<ActorRequestDTO> actors, List<GenreRequestDTO> genres) {

		HashSet<Object> seenActor = new HashSet<>();
		HashSet<Object> seenGenre = new HashSet<>();

		// removeIf will remove an element if it meets the specified criteria.
		// Set.add will return false if it did not modify the Set, i.e. already contains
		// the value.
		actors.removeIf(a -> !seenActor.add(a.getId()));
		genres.removeIf(g -> !seenGenre.add(g.getId()));

		movieRequestDTO.setActors(actors);
		movieRequestDTO.setGenres(genres);

		return movieRequestDTO;
	}

	private static List<ActorEntity> deleteNonExistentActors(List<ActorEntity> actorEntityListFromDB,
			List<ActorEntity> actorRequestList) {

		List<ActorEntity> toRemove = new ArrayList<>();

		// Loop actorRequestList items
		for (ActorEntity actorFromRequest : actorRequestList) {

			// Loop actorEntityListFromDB items
			boolean found = false;

			for (ActorEntity actorFromDB : actorEntityListFromDB) {
				if (actorFromRequest.getId().equals(actorFromDB.getId())) {
					found = true;
				}
			}

			// If the id does not exist, we save the array with non existent actor.
			// request.
			if (!found) {
				toRemove.add(actorFromRequest);
			}

		}

		// Remove non existent actor array from the request.
		actorRequestList.removeAll(toRemove);

		return actorRequestList;

	}

	private static List<GenreEntity> deleteNonExistentGenres(List<GenreEntity> genreEntityListFromDB,
			List<GenreEntity> genreRequestList) {

		List<GenreEntity> toRemove = new ArrayList<>();

		// Loop genreRequestList items
		for (GenreEntity genreFromRequest : genreRequestList) {

			// Loop genreEntityListFromDB items
			boolean found = false;

			for (GenreEntity genreFromDB : genreEntityListFromDB) {
				if (genreFromRequest.getId().equals(genreFromDB.getId())) {
					found = true;
				}
			}

			// If the id does not exist, we save the array with non existent actor.
			// request.
			if (!found) {
				toRemove.add(genreFromRequest);
			}

		}

		// Remove non existent genre array from the request.
		genreRequestList.removeAll(toRemove);

		return genreRequestList;

	}

}
