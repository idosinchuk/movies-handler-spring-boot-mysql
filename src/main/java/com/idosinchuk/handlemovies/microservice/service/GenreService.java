package com.idosinchuk.handlemovies.microservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;

import com.idosinchuk.handlemovies.microservice.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.microservice.dto.GenreResponseDTO;

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
	 * @return ResponseEntity of paged {@link GenreResponseDTO}
	 */
	@SuppressWarnings("rawtypes")
	public ResponseEntity<PagedResources<GenreResponseDTO>> getGenres(Pageable pageable,
			PagedResourcesAssembler assembler);

	/**
	 * Retrieve details of a genre by the id.
	 * 
	 * @param id of the genre
	 * @return ResponseEntity of {@link GenreResponseDTO}
	 */
	public ResponseEntity<GenreResponseDTO> getGenre(Long id);

	/**
	 * Add a genre.
	 * 
	 * @param genreRequestDTO request object
	 * @return ResponseEntity of {@link GenreResponseDTO}
	 */
	public ResponseEntity<GenreResponseDTO> addGenre(GenreRequestDTO genreRequestDTO);

	/**
	 * Delete a genre by the id.
	 * 
	 * @param id of the genre
	 * @return ResponseEntity of {@link GenreResponseDTO}
	 */
	public ResponseEntity<GenreResponseDTO> deleteGenre(Long id);

}
