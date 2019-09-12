package com.idosinchuk.handlemovies.microservice.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.idosinchuk.handlemovies.microservice.dto.MovieRequestDTO;
import com.idosinchuk.handlemovies.microservice.dto.MovieResponseDTO;
import com.idosinchuk.handlemovies.microservice.service.MovieService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller for handle movies
 * 
 * @author Igor Dosinchuk
 *
 */
@RestController
@Timed
@Api(value = "Movie API Rest")
@RequestMapping({ "api/v1" })
public class MovieController {

	public static final Logger logger = LoggerFactory.getLogger(MovieController.class);

	@Autowired
	public MovieService movieService;

	/**
	 * Retrieve list of all movies according to the search criteria.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity of paged list of all movies
	 */
	@SuppressWarnings({ "rawtypes" })
	@GetMapping(path = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all movies according to the search criteria.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 503, message = "Service Unavailable") })
	public ResponseEntity<PagedResources<MovieResponseDTO>> getMovies(Pageable pageable,
			PagedResourcesAssembler assembler) {

		return movieService.getMovies(pageable, assembler);
	}

	/**
	 * Retrieve details of a movie by the id.
	 * 
	 * @param id movie id
	 * @return Information of the actor
	 */
	@Timed(longTask = true)
	@GetMapping(path = "/movies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Retrieve details of a movie by the id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 503, message = "Service Unavailable") })
	public ResponseEntity<MovieResponseDTO> getMovies(@PathVariable Long id) {

		return movieService.getMovie(id);

	}

	/**
	 * Add a movie.
	 * 
	 * @param MovieResponseDTO object to save
	 * @return ResponseEntity of {@link MovieResponseDTO}
	 */
	@PostMapping(path = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Add a movie.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 503, message = "Service Unavailable") })
	public ResponseEntity<MovieResponseDTO> addMovies(@Valid @RequestBody MovieRequestDTO movieRequestDTO) {

		return movieService.addMovie(movieRequestDTO);

	}

	/**
	 * Delete a movie by the id.
	 * 
	 * @param id movie id
	 * @return ResponseEntity of {@link MovieResponseDTO}
	 */
	@DeleteMapping(path = "/movies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "Delete a movie by the id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 503, message = "Service Unavailable") })
	public ResponseEntity<MovieResponseDTO> deleteMovies(@PathVariable Long id) {

		return movieService.deleteMovie(id);
	}
}