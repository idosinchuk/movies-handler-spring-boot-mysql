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
import org.junit.Ignore;
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

import com.idosinchuk.handlemovies.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.dto.GenreResponseDTO;
import com.idosinchuk.handlemovies.entity.GenreEntity;
import com.idosinchuk.handlemovies.repository.GenreRepository;
import com.idosinchuk.handlemovies.service.GenreService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenreServiceImplTest {

	@MockBean
	GenreRepository genreRepository;

	@Autowired
	GenreService genreService;

	@Autowired
	ModelMapper modelMapper;

	private GenreEntity genreEntity;
	private GenreEntity genreEntityResponse;
	private GenreRequestDTO genreRequestDTO;
	private GenreResponseDTO genreResponseDTO;
	private ArrayList<GenreEntity> genreEntityListFromDB;
	private ArrayList<GenreEntity> genreEntityResponseList;

	@Before
	public void setUp() throws Exception {

		// Fill genreEntity
		genreEntity = new GenreEntity();
		genreEntity.setId(1L);
		genreEntity.setName("name");

		// Fill genreEntityListFromDB
		genreEntityListFromDB = new ArrayList<>();
		genreEntityListFromDB.add(genreEntity);

		// Fill genreRequestDTO
		genreRequestDTO = new GenreRequestDTO();
		genreRequestDTO.setId(1L);
		genreRequestDTO.setName("name");

		// Fill genreResponseDTO
		genreResponseDTO = new GenreResponseDTO();
		genreResponseDTO.setId(1L);
		genreResponseDTO.setName("name");

		// Fill genreEntityResponse
		genreEntityResponse = new GenreEntity();
		genreEntityResponse.setId(1L);
		genreEntityResponse.setName("name");

		// Fill genreEntityResponseList
		genreEntityResponseList = new ArrayList<>();
		genreEntityResponseList.add(genreEntityResponse);

	}

	@Test
	public void testFindAllGenresReturnOk() {

		Pageable pageable = PageRequest.of(0, 3);

		HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
		PagedResourcesAssembler<GenreRequestDTO> assembler = new PagedResourcesAssembler<>(resolver, null);

		Page<GenreEntity> entityResponse = new PageImpl<>(genreEntityResponseList, pageable,
				genreEntityResponseList.size());

		when(genreRepository.findAll(pageable)).thenReturn(entityResponse);

		ResponseEntity<PagedResources<GenreResponseDTO>> genreResponse = genreService.findAllGenres(pageable,
				assembler);
		assertNotNull(genreResponse);
		assertEquals(HttpStatus.OK, genreResponse.getStatusCode());

		// Convert Entity response to DTO
		Page<GenreResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<GenreResponseDTO>>() {
		}.getType());

		assertNotNull(response);
	}

	@Test
	@Ignore
	public void testFindAllGenresReturnNoContent() {

		Pageable pageable = PageRequest.of(0, 3);

		HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
		PagedResourcesAssembler<GenreRequestDTO> assembler = new PagedResourcesAssembler<>(resolver, null);

		when(genreRepository.findAll(pageable)).thenReturn(null);

		Page<GenreEntity> entityResponse = genreRepository.findAll(pageable);
		Objects.isNull(entityResponse);

		ResponseEntity<PagedResources<GenreResponseDTO>> genreResponse = genreService.findAllGenres(pageable,
				assembler);
		assertEquals(HttpStatus.NO_CONTENT, genreResponse.getStatusCode());

	}

	@Test
	public void testFindGenreByIdReturnOk() {

		Optional<GenreEntity> entityResponse = Optional.of(genreEntityResponse);
		when(genreRepository.findById(1L)).thenReturn(entityResponse);

		ResponseEntity<GenreResponseDTO> genreResponse = genreService.findGenreById(1L);
		assertNotNull(genreResponse);

		verify(genreRepository, times(1)).findById(ArgumentMatchers.any());

		if (entityResponse.isPresent()) {

			// Convert Entity response to DTO
			GenreResponseDTO response = modelMapper.map(entityResponse, new TypeToken<GenreResponseDTO>() {
			}.getType());

			assertNotNull(response);
		}

	}

	@Test
	public void testFindGenreByIdReturnBadRequest() {

		ResponseEntity<GenreResponseDTO> genreResponse = genreService.findGenreById(null);
		assertEquals(HttpStatus.NO_CONTENT, genreResponse.getStatusCode());

	}

	@Test
	public void testAddGenreReturnOk() {

		GenreEntity genreEntityRequest = modelMapper.map(genreRequestDTO, GenreEntity.class);
		assertNotNull(genreEntityRequest);

		when(genreRepository.findAll()).thenReturn(genreEntityListFromDB);

		when(genreRepository.save(genreEntityRequest)).thenReturn(genreEntityResponse);

		GenreResponseDTO resultGenreResponseDTO = new GenreResponseDTO();
		resultGenreResponseDTO = modelMapper.map(genreEntityResponse, GenreResponseDTO.class);
		assertEquals(genreResponseDTO, resultGenreResponseDTO);

		ResponseEntity<GenreResponseDTO> genreResponse = genreService.addGenre(genreRequestDTO);
		assertNotNull(genreResponse);
		assertEquals(HttpStatus.CREATED, genreResponse.getStatusCode());

	}

	@Test
	public void testDeleteGenreReturnOk() {

		ResponseEntity<GenreResponseDTO> genreResponse = genreService.deleteGenre(1L);
		assertEquals(HttpStatus.OK, genreResponse.getStatusCode());
	}

}