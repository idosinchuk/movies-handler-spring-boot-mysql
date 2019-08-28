package com.idosinchuk.handlemovies.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.idosinchuk.handlemovies.dto.ActorRequestDTO;
import com.idosinchuk.handlemovies.dto.ActorResponseDTO;
import com.idosinchuk.handlemovies.entity.ActorEntity;
import com.idosinchuk.handlemovies.repository.ActorRepository;
import com.idosinchuk.handlemovies.service.ActorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActorServiceImplTest {

	@MockBean
	ActorRepository actorRepository;

	@Autowired
	ActorService actorService;

	@Autowired
	ModelMapper modelMapper;

	private ActorEntity actorEntity;
	private ActorEntity actorEntityResponse;
	private ActorRequestDTO actorRequestDTO;
	private ActorResponseDTO actorResponseDTO;
	private ArrayList<ActorEntity> actorEntityListFromDB;
	private ArrayList<ActorEntity> actorEntityResponseList;

	@Before
	public void setUp() throws Exception {

		// Fill actorEntity
		actorEntity = new ActorEntity();
		actorEntity.setId(1L);
		actorEntity.setName("name");
		actorEntity.setSurname("surname");

		// Fill actorEntityListFromDB
		actorEntityListFromDB = new ArrayList<>();
		actorEntityListFromDB.add(actorEntity);

		// Fill actorRequestDTO
		actorRequestDTO = new ActorRequestDTO();
		actorRequestDTO.setId(1L);
		actorRequestDTO.setName("name");
		actorRequestDTO.setSurname("surname");

		// Fill actorResponseDTO
		actorResponseDTO = new ActorResponseDTO();
		actorResponseDTO.setId(1L);
		actorResponseDTO.setName("name");
		actorResponseDTO.setSurname("surname");

		// Fill actorEntityResponse
		actorEntityResponse = new ActorEntity();
		actorEntityResponse.setId(1L);
		actorEntityResponse.setName("name");
		actorEntityResponse.setSurname("surname");

		// Fill actorEntityResponseList
		actorEntityResponseList = new ArrayList<>();
		actorEntityResponseList.add(actorEntityResponse);

	}

	@Test
	public void testFindAllActorsReturnOk() {

		Pageable pageable = PageRequest.of(0, 3);

		HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
		PagedResourcesAssembler<ActorRequestDTO> assembler = new PagedResourcesAssembler<>(resolver, null);

		Page<ActorEntity> entityResponse = new PageImpl<>(actorEntityResponseList, pageable,
				actorEntityResponseList.size());

		when(actorRepository.findAll(pageable)).thenReturn(entityResponse);

		ResponseEntity<PagedResources<ActorResponseDTO>> actorResponse = actorService.findAllActors(pageable,
				assembler);
		assertNotNull(actorResponse);
		assertEquals(HttpStatus.OK, actorResponse.getStatusCode());

		// Convert Entity response to DTO
		Page<ActorResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<ActorResponseDTO>>() {
		}.getType());

		assertNotNull(response);
	}

	@Test
	public void testFindAllActorsReturnNoContent() {

		Pageable pageable = PageRequest.of(0, 3);

		HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
		PagedResourcesAssembler<ActorRequestDTO> assembler = new PagedResourcesAssembler<>(resolver, null);

		when(actorRepository.findAll(pageable)).thenReturn(null);

		Page<ActorEntity> entityResponse = actorRepository.findAll(pageable);
		Objects.isNull(entityResponse);

		ResponseEntity<PagedResources<ActorResponseDTO>> actorResponse = actorService.findAllActors(pageable,
				assembler);
		assertEquals(HttpStatus.NO_CONTENT, actorResponse.getStatusCode());

	}

	@Test
	public void testFindActorByIdReturnOk() {

		Optional<ActorEntity> entityResponse = Optional.of(actorEntityResponse);
		when(actorRepository.findById(1L)).thenReturn(entityResponse);

		ResponseEntity<ActorResponseDTO> actorResponse = actorService.findActorById(1L);
		assertNotNull(actorResponse);

		verify(actorRepository, times(1)).findById(ArgumentMatchers.any());

		if (entityResponse.isPresent()) {

			// Convert Entity response to DTO
			ActorResponseDTO response = modelMapper.map(entityResponse, new TypeToken<ActorResponseDTO>() {
			}.getType());

			assertNotNull(response);
		}

	}

	@Test
	public void testFindActorByIdReturnBadRequest() {

		ResponseEntity<ActorResponseDTO> actorResponse = actorService.findActorById(null);
		assertEquals(HttpStatus.NO_CONTENT, actorResponse.getStatusCode());

	}

	@Test
	public void testAddActorReturnOk() {

		ActorEntity actorEntityRequest = modelMapper.map(actorRequestDTO, ActorEntity.class);
		assertNotNull(actorEntityRequest);

		when(actorRepository.findAll()).thenReturn(actorEntityListFromDB);

		when(actorRepository.save(actorEntityRequest)).thenReturn(actorEntityResponse);

		ActorResponseDTO resultActorResponseDTO = new ActorResponseDTO();
		resultActorResponseDTO = modelMapper.map(actorEntityResponse, ActorResponseDTO.class);
		assertEquals(actorResponseDTO, resultActorResponseDTO);

		ResponseEntity<ActorResponseDTO> actorResponse = actorService.addActor(actorRequestDTO);
		assertNotNull(actorResponse);
		assertEquals(HttpStatus.CREATED, actorResponse.getStatusCode());

	}

	@Test
	public void testDeleteActorReturnOk() {

		ResponseEntity<ActorResponseDTO> actorResponse = actorService.deleteActor(1L);
		assertEquals(HttpStatus.OK, actorResponse.getStatusCode());
	}

}