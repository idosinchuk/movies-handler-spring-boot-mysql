package com.idosinchuk.handlemovies.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	 * @param pageable param for pagination
	 * @return Page of {@link MovieResponseDTO}
	 */
	public Page<MovieResponseDTO> findAllMovies(Pageable pageable);

	/**
	 * Retrieve details of a movie by the id.
	 * 
	 * @param id of the movie
	 * @return {@link MovieResponseDTO}
	 */
	public MovieResponseDTO findMovieById(Long id);

	/**
	 * Add a movie.
	 * 
	 * @param movieRequestDTO request object
	 * @return {@link MovieResponseDTO}
	 */
	public MovieResponseDTO addMovie(MovieRequestDTO movieRequestDTO);

	/**
	 * Delete a movie by the id.
	 * 
	 * @param id of the movie
	 * @return response
	 */
	public void deleteMovie(Long id);
}
