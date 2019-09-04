package com.idosinchuk.handlemovies.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.idosinchuk.handlemovies.dto.ActorRequestDTO;
import com.idosinchuk.handlemovies.dto.ActorResponseDTO;
import com.idosinchuk.handlemovies.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.dto.GenreResponseDTO;
import com.idosinchuk.handlemovies.dto.MovieRequestDTO;
import com.idosinchuk.handlemovies.dto.MovieResponseDTO;
import com.idosinchuk.handlemovies.service.MovieService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MovieController.class)
public class MovieControllerTest {

	private static final String MOVIES_PATH = "/api/v1/movies";
	private static final String MOVIE_ID_PATH = "/api/v1/movies/1";

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private MovieController movieController;

	@MockBean
	private MovieService movieService;

	@Mock
	private ModelMapper modelMapper;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	private MovieRequestDTO movieRequestDTO;
	private MovieResponseDTO movieResponseDTO;
	private GenreRequestDTO genreRequestDTO;
	private GenreResponseDTO genreResponseDTO;
	private ActorResponseDTO actorResponseDTO;
	private ActorRequestDTO actorRequestDTO;
	private ArrayList<ActorRequestDTO> actorRequestDTOList;
	private ArrayList<ActorResponseDTO> actorResponseDTOList;
	private ArrayList<GenreRequestDTO> genreRequestDTOList;
	private ArrayList<GenreResponseDTO> genreResponseDTOList;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(movieController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();

		// Fill actorRequestDTO
		actorRequestDTO = new ActorRequestDTO();
		actorRequestDTO.setName("name");
		actorRequestDTO.setSurname("surname");

		// Fill actorResponseDTO
		actorResponseDTO = new ActorResponseDTO();
		actorResponseDTO.setId(1L);
		actorResponseDTO.setName("name");
		actorResponseDTO.setSurname("surname");

		// Fill actorRequestDTOList
		actorRequestDTOList = new ArrayList<>();
		actorRequestDTOList.add(actorRequestDTO);

		// Fill actorResponseDTOList
		actorResponseDTOList = new ArrayList<>();
		actorResponseDTOList.add(actorResponseDTO);

		// Fill genreRequestDTO
		genreRequestDTO = new GenreRequestDTO();
		genreRequestDTO.setName("name");

		// Fill genreResponseDTO
		genreResponseDTO = new GenreResponseDTO();
		genreResponseDTO.setId(1L);
		genreResponseDTO.setName("name");

		// Fill genreRequestDTOList
		genreRequestDTOList = new ArrayList<>();
		genreRequestDTOList.add(genreRequestDTO);

		// Fill genreResponseDTOList
		genreResponseDTOList = new ArrayList<>();
		genreResponseDTOList.add(genreResponseDTO);

		// Fill movieRequestDTO
		movieRequestDTO = new MovieRequestDTO();
		movieRequestDTO.setTitle("title");
		movieRequestDTO.setYear("year");
		movieRequestDTO.setActors(actorRequestDTOList);
		movieRequestDTO.setGenres(genreRequestDTOList);

		// Fill movieResponseDTO
		movieResponseDTO = new MovieResponseDTO();
		movieResponseDTO.setId(1L);
		movieResponseDTO.setTitle("title");
		movieResponseDTO.setYear("year");
		movieResponseDTO.setActors(actorResponseDTOList);
		movieResponseDTO.setGenres(genreResponseDTOList);

	}

	@Test
	public void testGetMoviesReturnOk() throws Exception {

		List<MovieResponseDTO> movies = new ArrayList<MovieResponseDTO>();
		movies.add(movieResponseDTO);

		PagedResources<MovieResponseDTO> pagedResources = new PagedResources<>(movies,
				new PagedResources.PageMetadata(movies.size(), 1, movies.size(), 1));

		ResponseEntity<PagedResources<MovieResponseDTO>> entity = new ResponseEntity<PagedResources<MovieResponseDTO>>(
				pagedResources, HttpStatus.OK);

		when(movieService.getMovies(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(entity);

		// Result test
		final ResultActions result = mockMvc.perform(get(MOVIES_PATH).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		System.out.println("findAllMovies_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

	@Test
	public void testGetMoviesByIdReturnOk() throws Exception {

		when(movieService.getMovie(ArgumentMatchers.any()))
				.thenReturn(new ResponseEntity<MovieResponseDTO>(movieResponseDTO, HttpStatus.OK));

		// Result test
		mockMvc.perform(get(MOVIE_ID_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void testAddMoviesReturnOk() throws Exception {

		ResponseEntity<MovieResponseDTO> response = new ResponseEntity<MovieResponseDTO>(movieResponseDTO,
				HttpStatus.CREATED);

		when(movieService.addMovie(ArgumentMatchers.any(MovieRequestDTO.class))).thenReturn(response);

		Gson gson = new Gson();
		String json = gson.toJson(movieRequestDTO);

		final ResultActions result = mockMvc
				.perform(post(MOVIES_PATH).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		System.out.println("addMovie_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

	@Test
	public void testDeleteMoviesReturnOk() throws Exception {

		final ResultActions result = mockMvc.perform(delete(MOVIE_ID_PATH).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		System.out.println("deleteMovie_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

}
