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
import com.idosinchuk.handlemovies.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.dto.GenreResponseDTO;
import com.idosinchuk.handlemovies.service.GenreService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = GenreController.class)
public class GenreControllerTest {

	private static final String GENRES_PATH = "/api/v1/genres";
	private static final String GENRE_ID_PATH = "/api/v1/genres/1";

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private GenreController genreController;

	@MockBean
	private GenreService genreService;

	@Mock
	private ModelMapper modelMapper;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	private GenreRequestDTO genreRequestDTO;
	private GenreResponseDTO genreResponseDTO;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(genreController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();

		// Fill genreRequestDTO
		genreRequestDTO = new GenreRequestDTO();
		genreRequestDTO.setName("name");

		// Fill genreResponseDTO
		genreResponseDTO = new GenreResponseDTO();
		genreResponseDTO.setId(1L);
		genreResponseDTO.setName("name");

	}

	@Test
	public void testGetGenresReturnOk() throws Exception {

		List<GenreResponseDTO> genres = new ArrayList<GenreResponseDTO>();
		genres.add(genreResponseDTO);

		PagedResources<GenreResponseDTO> pagedResources = new PagedResources<>(genres,
				new PagedResources.PageMetadata(genres.size(), 1, genres.size(), 1));

		ResponseEntity<PagedResources<GenreResponseDTO>> entity = new ResponseEntity<PagedResources<GenreResponseDTO>>(
				pagedResources, HttpStatus.OK);

		when(genreService.getGenres(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(entity);

		// Result test
		final ResultActions result = mockMvc.perform(get(GENRES_PATH).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		System.out.println("findAllGenres_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

	@Test
	public void testGetGenresByIdReturnOk() throws Exception {

		when(genreService.getGenre(ArgumentMatchers.any()))
				.thenReturn(new ResponseEntity<GenreResponseDTO>(genreResponseDTO, HttpStatus.OK));

		// Result test
		mockMvc.perform(get(GENRE_ID_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void testAddGenresReturnOk() throws Exception {

		ResponseEntity<GenreResponseDTO> response = new ResponseEntity<GenreResponseDTO>(genreResponseDTO,
				HttpStatus.CREATED);

		when(genreService.addGenre(ArgumentMatchers.any(GenreRequestDTO.class))).thenReturn(response);

		Gson gson = new Gson();
		String json = gson.toJson(genreRequestDTO);

		final ResultActions result = mockMvc
				.perform(post(GENRES_PATH).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		System.out.println("addGenre_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

	@Test
	public void testDeleteGenresReturnOk() throws Exception {

		final ResultActions result = mockMvc.perform(delete(GENRE_ID_PATH).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		System.out.println("deleteGenre_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

}
