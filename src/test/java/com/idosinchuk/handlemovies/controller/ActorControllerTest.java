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
import com.idosinchuk.handlemovies.service.ActorService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ActorController.class)
public class ActorControllerTest {

	private static final String ACTORS_PATH = "/api/v1/actors";
	private static final String ACTOR_ID_PATH = "/api/v1/actors/1";

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private ActorController actorController;

	@MockBean
	private ActorService actorService;

	@Mock
	private ModelMapper modelMapper;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	private ActorRequestDTO actorRequestDTO;
	private ActorResponseDTO actorResponseDTO;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(actorController)
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

	}

	@Test
	public void testGetActorsReturnOk() throws Exception {

		List<ActorResponseDTO> actors = new ArrayList<ActorResponseDTO>();
		actors.add(actorResponseDTO);

		PagedResources<ActorResponseDTO> pagedResources = new PagedResources<>(actors,
				new PagedResources.PageMetadata(actors.size(), 1, actors.size(), 1));

		ResponseEntity<PagedResources<ActorResponseDTO>> entity = new ResponseEntity<PagedResources<ActorResponseDTO>>(
				pagedResources, HttpStatus.OK);

		when(actorService.getActors(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(entity);

		// Result test
		final ResultActions result = mockMvc.perform(get(ACTORS_PATH).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		System.out.println("findAllActors_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

	@Test
	public void testGetActorReturnOk() throws Exception {

		when(actorService.getActor(ArgumentMatchers.any()))
				.thenReturn(new ResponseEntity<ActorResponseDTO>(actorResponseDTO, HttpStatus.OK));

		// Result test
		mockMvc.perform(get(ACTOR_ID_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void testAddActorReturnOk() throws Exception {

		ResponseEntity<ActorResponseDTO> response = new ResponseEntity<ActorResponseDTO>(actorResponseDTO,
				HttpStatus.CREATED);

		when(actorService.addActor(ArgumentMatchers.any(ActorRequestDTO.class))).thenReturn(response);

		Gson gson = new Gson();
		String json = gson.toJson(actorRequestDTO);

		final ResultActions result = mockMvc
				.perform(post(ACTORS_PATH).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		System.out.println("addActor_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

	@Test
	public void testDeleteActorReturnOk() throws Exception {

		final ResultActions result = mockMvc.perform(delete(ACTOR_ID_PATH).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		System.out.println("deleteActor_Test_Return_OK: " + result.andReturn().getResponse().getContentAsString());

	}

}
