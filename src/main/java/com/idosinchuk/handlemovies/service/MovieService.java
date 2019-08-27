package com.idosinchuk.handlemovies.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;

import com.idosinchuk.handlemovies.dto.MovieRequestDTO;
import com.idosinchuk.handlemovies.dto.MovieResponseDTO;

/**
 * 
 * Service for manage movies
 * 
 * @author Igor Dosinchuk
 *
 */
public interface MovieService {

	/**
	 * Retrieve list of all movies.
	 * 
	 * @param pageable  object for pagination
	 * @param assembler object for pagination
	 * @return Page of {@link MovieResponseDTO}
	 */
	@SuppressWarnings("rawtypes")
	public ResponseEntity<PagedResources<MovieResponseDTO>> findAllMovies(Pageable pageable,
			PagedResourcesAssembler assembler);

	/**
	 * Retrieve details of a movie by the id.
	 * 
	 * @param id of the movie
	 * @return {@link MovieResponseDTO}
	 */
	public ResponseEntity<MovieResponseDTO> findMovieById(Long id);

	/**
	 * Add a movie.
	 * 
	 * @param movieRequestDTO request object
	 * @return {@link MovieResponseDTO}
	 */
	public ResponseEntity<MovieResponseDTO> addMovie(MovieRequestDTO movieRequestDTO);

	/**
	 * Delete a movie by the id.
	 * 
	 * @param id of the movie
	 * @return {@link MovieResponseDTO}
	 */
	public ResponseEntity<MovieResponseDTO> deleteMovie(Long id);
}
