package com.idosinchuk.handlemovies.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.idosinchuk.handlemovies.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.dto.GenreResponseDTO;
import com.idosinchuk.handlemovies.service.GenreService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller for handle genres
 * 
 * @author Igor Dosinchuk
 *
 */
@RestController
@Api(value = "API Rest for handle genres.")
@RequestMapping({ "api/v1/genres" })
public class GenreController {

	@Autowired
	public GenreService genreService;

	/**
	 * Retrieve list of all genres according to the search criteria.
	 * 
	 * @param pageable paging fields
	 * @return List of actors found with support hateoas and pagination
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all genres.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 503, message = "Service Unavailable") })
	public HttpEntity<PagedResources<GenreResponseDTO>> findAllGenres(Pageable pageable,
			PagedResourcesAssembler assembler) {

		Page<GenreResponseDTO> response = genreService.findAllGenres(pageable);

		if (response == null || ObjectUtils.isEmpty(response)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(assembler.toResource(response), HttpStatus.OK);
		}
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve details of a genre by the id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 503, message = "Service Unavailable") })
	public ResponseEntity<GenreResponseDTO> findGenreById(@PathVariable Long id) {

		GenreResponseDTO response = genreService.findGenreById(id);

		if (response == null || ObjectUtils.isEmpty(response)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add a genre.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 503, message = "Service Unavailable") })
	public ResponseEntity<GenreResponseDTO> addGenre(@Valid @RequestBody GenreRequestDTO genreRequestDTO) {

		GenreResponseDTO response = genreService.addGenre(genreRequestDTO);

		if (response == null || ObjectUtils.isEmpty(response)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
	}

	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Delete a genre by the id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 503, message = "Service Unavailable") })
	public void deleteGenre(@PathVariable Long id) {

		genreService.deleteGenre(id);
	}
}
