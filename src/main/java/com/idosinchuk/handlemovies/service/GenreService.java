package com.idosinchuk.handlemovies.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.idosinchuk.handlemovies.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.dto.GenreResponseDTO;

/**
 * 
 * Service for manage genres
 * 
 * @author Igor Dosinchuk
 *
 */
public interface GenreService {

	/**
	 * Retrieve list of all genres.
	 * 
	 * @param pageable param for pagination
	 * @return Page of {@link GenreResponseDTO}
	 */
	public Page<GenreResponseDTO> findAllGenres(Pageable pageable);

	/**
	 * Retrieve details of a genre by the id.
	 * 
	 * @param id of the genre
	 * @return {@link GenreResponseDTO}
	 */
	public GenreResponseDTO findGenreById(Long id);

	/**
	 * Add a genre.
	 * 
	 * @param genreRequestDTO request object
	 * @return {@link GenreResponseDTO}
	 */
	public GenreResponseDTO addGenre(GenreRequestDTO genreRequestDTO);

	/**
	 * Delete a genre by the id.
	 * 
	 * @param id of the genre
	 * @return response
	 */
	public void deleteGenre(Long id);

}
