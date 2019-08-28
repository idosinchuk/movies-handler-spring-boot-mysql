package com.idosinchuk.handlemovies.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;

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
	 * @param pageable  object for pagination
	 * @param assembler object for pagination
	 * @return PagedResources of {@link GenreResponseDTO}
	 */
	@SuppressWarnings("rawtypes")
	public ResponseEntity<PagedResources<GenreResponseDTO>> findAllGenres(Pageable pageable,
			PagedResourcesAssembler assembler);

	/**
	 * Retrieve details of a genre by the id.
	 * 
	 * @param id of the genre
	 * @return {@link GenreResponseDTO}
	 */
	public ResponseEntity<GenreResponseDTO> findGenreById(Long id);

	/**
	 * Add a genre.
	 * 
	 * @param genreRequestDTO request object
	 * @return {@link GenreResponseDTO}
	 */
	public ResponseEntity<GenreResponseDTO> addGenre(GenreRequestDTO genreRequestDTO);

	/**
	 * Delete a genre by the id.
	 * 
	 * @param id of the genre
	 * @return {@link GenreResponseDTO}
	 */
	public ResponseEntity<GenreResponseDTO> deleteGenre(Long id);

}
