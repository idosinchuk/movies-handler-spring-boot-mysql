package com.idosinchuk.handlemovies.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.idosinchuk.handlemovies.dto.MovieResponseDTO;
import com.idosinchuk.handlemovies.service.MovieService;

public class MovieControllerTest {

	private static final String FIND_ALL_MOVIES_PATH = "api/v1/movies";

	private MockMvc mockMvc;

	@InjectMocks
	private MovieController movieController;

	@Mock
	private MovieService movieService;

	@Mock
	private ModelMapper modelMapper;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	MovieResponseDTO movieResponseDTO;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(movieController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();

	}

	@Test
	@Ignore
	public void findAllMovies_Test_Return_OK() throws Exception {

		Pageable pageableImpl = PageRequest.of(0, 3);

		MovieResponseDTO movie = new MovieResponseDTO();
		movie.setId(1L);
		movie.setTitle("title");
		movie.setYear("2019");
		movie.setActors(new ArrayList<>());
		movie.setGenres(new ArrayList<>());

		List<MovieResponseDTO> movies = new ArrayList<MovieResponseDTO>();
		movies.add(movie);

		Page<MovieResponseDTO> returnPage = new PageImpl<MovieResponseDTO>(movies, pageableImpl, movies.size());

		PagedResources<MovieResponseDTO> pagedResources = new PagedResources<>(movies,
				new PagedResources.PageMetadata(movies.size(), 1, movies.size(), 1));

		new ResponseEntity<PagedResources<MovieResponseDTO>>(pagedResources, HttpStatus.OK);

		// when(movieService.findAllMovies(ArgumentMatchers.any(),
		// ArgumentMatchers.any()).thenReturn(response));

	}

	@Test
	public void FindMovieById_Test_Return_OK() throws Exception {

		MovieResponseDTO movie = new MovieResponseDTO();
		movie.setId(1L);
		movie.setTitle("title");
		movie.setYear("2019");
		movie.setActors(new ArrayList<>());
		movie.setGenres(new ArrayList<>());

		when(movieService.findMovieById(ArgumentMatchers.any()))
				.thenReturn(new ResponseEntity<MovieResponseDTO>(movie, HttpStatus.OK));

		mockMvc.perform(get(FIND_ALL_MOVIES_PATH).param("id", "id").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
