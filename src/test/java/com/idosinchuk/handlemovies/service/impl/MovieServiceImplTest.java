package com.idosinchuk.handlemovies.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.idosinchuk.handlemovies.dto.GenreRequestDTO;
import com.idosinchuk.handlemovies.dto.GenreResponseDTO;
import com.idosinchuk.handlemovies.dto.MovieRequestDTO;
import com.idosinchuk.handlemovies.dto.MovieResponseDTO;
import com.idosinchuk.handlemovies.entity.ActorEntity;
import com.idosinchuk.handlemovies.entity.GenreEntity;
import com.idosinchuk.handlemovies.entity.MovieEntity;
import com.idosinchuk.handlemovies.repository.ActorRepository;
import com.idosinchuk.handlemovies.repository.GenreRepository;
import com.idosinchuk.handlemovies.repository.MovieRepository;
import com.idosinchuk.handlemovies.service.MovieService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieServiceImplTest {

	@MockBean
	MovieRepository movieRepository;

	@MockBean
	ActorRepository actorRepository;

	@MockBean
	GenreRepository genreRepository;

	@Autowired
	MovieService movieService;

	@Autowired
	ModelMapper modelMapper;

	private ActorEntity actorEntity;
	private GenreEntity genreEntity;
	private MovieEntity movieEntityResponse;
	private MovieRequestDTO movieRequestDTO;
	private MovieResponseDTO movieResponseDTO;
	private GenreRequestDTO genreRequestDTO;
	private GenreResponseDTO genreResponseDTO;
	private ActorRequestDTO actorRequestDTO;
	private ActorResponseDTO actorResponseDTO;
	private ArrayList<ActorRequestDTO> actorRequestDTOList;
	private ArrayList<ActorResponseDTO> actorResponseDTOList;
	private ArrayList<ActorEntity> actorEntityList;
	private ArrayList<ActorEntity> actorEntityListFromDB;
	private ArrayList<GenreEntity> genreEntityList;
	private ArrayList<GenreEntity> genreEntityListFromDB;
	private ArrayList<GenreRequestDTO> genreRequestDTOList;
	private ArrayList<GenreResponseDTO> genreResponseDTOList;
	private ArrayList<MovieEntity> movieEntityResponseList;

	@Before
	public void setUp() throws Exception {

		// Fill actorEntity
		actorEntity = new ActorEntity();
		actorEntity.setId(1L);
		actorEntity.setName("name");
		actorEntity.setSurname("surname");

		// Fill actorEntityList
		actorEntityList = new ArrayList<>();
		actorEntityList.add(actorEntity);

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

		// Fill actorRequestDTOList
		actorRequestDTOList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			actorRequestDTOList.add(actorRequestDTO);
		}

		// Fill actorResponseDTOList
		actorResponseDTOList = new ArrayList<>();
		actorResponseDTOList.add(actorResponseDTO);

		// Fill genreEntity
		genreEntity = new GenreEntity();
		genreEntity.setId(1L);
		genreEntity.setName("name");

		// Fill genreEntityList
		genreEntityList = new ArrayList<>();
		genreEntityList.add(genreEntity);

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

		// Fill genreRequestDTOList
		genreRequestDTOList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			genreRequestDTOList.add(genreRequestDTO);
		}

		// Fill genreResponseDTOList
		genreResponseDTOList = new ArrayList<>();
		genreResponseDTOList.add(genreResponseDTO);

		// Fill movieEntityResponse
		movieEntityResponse = new MovieEntity();
		movieEntityResponse.setId(1L);
		movieEntityResponse.setTitle("title");
		movieEntityResponse.setYear("year");
		movieEntityResponse.setActors(actorEntityList);
		movieEntityResponse.setGenres(genreEntityList);

		// Fill movieRequestDTO
		movieRequestDTO = new MovieRequestDTO();
		movieRequestDTO.setId(1L);
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

		// Fill movieEntityResponseList
		movieEntityResponseList = new ArrayList<>();
		movieEntityResponseList.add(movieEntityResponse);

	}

	@Test
	public void testFindAllMoviesReturnOk() {

		Pageable pageable = PageRequest.of(0, 3);

		HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
		PagedResourcesAssembler<MovieRequestDTO> assembler = new PagedResourcesAssembler<>(resolver, null);

		Page<MovieEntity> entityResponse = new PageImpl<>(movieEntityResponseList, pageable,
				movieEntityResponseList.size());

		when(movieRepository.findAll(pageable)).thenReturn(entityResponse);

		ResponseEntity<PagedResources<MovieResponseDTO>> movieResponse = movieService.findAllMovies(pageable,
				assembler);
		assertNotNull(movieResponse);
		assertEquals(HttpStatus.OK, movieResponse.getStatusCode());

		// Convert Entity response to DTO
		Page<MovieResponseDTO> response = modelMapper.map(entityResponse, new TypeToken<Page<MovieResponseDTO>>() {
		}.getType());

		assertNotNull(response);
	}

	@Test
	public void testFindAllMoviesReturnNoContent() {

		Pageable pageable = PageRequest.of(0, 3);

		HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
		PagedResourcesAssembler<MovieRequestDTO> assembler = new PagedResourcesAssembler<>(resolver, null);

		when(movieRepository.findAll(pageable)).thenReturn(null);

		Page<MovieEntity> entityResponse = movieRepository.findAll(pageable);
		Objects.isNull(entityResponse);

		ResponseEntity<PagedResources<MovieResponseDTO>> movieResponse = movieService.findAllMovies(pageable,
				assembler);
		assertEquals(HttpStatus.NO_CONTENT, movieResponse.getStatusCode());

	}

	@Test
	public void testFindMovieByIdReturnOk() {

		Optional<MovieEntity> entityResponse = Optional.of(movieEntityResponse);
		when(movieRepository.findById(1L)).thenReturn(entityResponse);

		ResponseEntity<MovieResponseDTO> movieResponse = movieService.findMovieById(1L);
		assertNotNull(movieResponse);

		verify(movieRepository, times(1)).findById(ArgumentMatchers.any());

		if (entityResponse.isPresent()) {

			// Convert Entity response to DTO
			MovieResponseDTO response = modelMapper.map(entityResponse, new TypeToken<MovieResponseDTO>() {
			}.getType());

			assertNotNull(response);
		}

	}

	@Test
	public void testFindMovieByIdReturnBadRequest() {

		ResponseEntity<MovieResponseDTO> movieResponse = movieService.findMovieById(null);
		assertEquals(HttpStatus.NO_CONTENT, movieResponse.getStatusCode());

	}

	@Test
	public void testAddMovieReturnOk() {

		// Check if movie request contains duplicates actors and genres
		deleteDuplicatesActorsAndGenres(movieRequestDTO, movieRequestDTO.getActors(), movieRequestDTO.getGenres());

		MovieEntity movieEntityRequest = modelMapper.map(movieRequestDTO, MovieEntity.class);
		assertNotNull(movieEntityRequest.getActors());

		when(actorRepository.findAll()).thenReturn(actorEntityListFromDB);

		// Check if actor exists in database, if not exists delete it from request
		List<ActorEntity> actorRequestList = deleteNonExistentActors(actorEntityListFromDB,
				movieEntityRequest.getActors());
		assertNotNull(actorRequestList);

		when(genreRepository.findAll()).thenReturn(genreEntityListFromDB);

		List<GenreEntity> genreRequestList = deleteNonExistentGenres(genreEntityListFromDB,
				movieEntityRequest.getGenres());
		assertNotNull(genreRequestList);

		when(movieRepository.save(movieEntityRequest)).thenReturn(movieEntityResponse);

		MovieResponseDTO resultMovieResponseDTO = new MovieResponseDTO();
		resultMovieResponseDTO = modelMapper.map(movieEntityResponse, MovieResponseDTO.class);
		assertEquals(movieResponseDTO, resultMovieResponseDTO);

		ResponseEntity<MovieResponseDTO> movieResponse = movieService.addMovie(movieRequestDTO);
		assertNotNull(movieResponse);
		assertEquals(HttpStatus.CREATED, movieResponse.getStatusCode());

	}

	@Test
	public void testAddMovieNonExistentActorsReturnBadRequest() {

		ActorRequestDTO actorRequestDTO = new ActorRequestDTO();
		actorRequestDTO.setId(99L);
		actorRequestDTO.setName("name");
		actorRequestDTO.setSurname("surname");

		ArrayList<ActorRequestDTO> actorRequestDTOListNonExistentActors = new ArrayList<ActorRequestDTO>();
		actorRequestDTOListNonExistentActors.add(actorRequestDTO);

		MovieRequestDTO movieRequestDTONonExistentActors = new MovieRequestDTO();
		movieRequestDTONonExistentActors.setId(1L);
		movieRequestDTONonExistentActors.setTitle("title");
		movieRequestDTONonExistentActors.setYear("year");
		movieRequestDTONonExistentActors.setActors(actorRequestDTOListNonExistentActors);
		movieRequestDTONonExistentActors.setGenres(genreRequestDTOList);

		MovieEntity movieEntityRequest = modelMapper.map(movieRequestDTONonExistentActors, MovieEntity.class);
		assertNotNull(movieEntityRequest.getActors());

		when(actorRepository.findAll()).thenReturn(actorEntityListFromDB);

		// Check if the requested actor exists in database, if not exists delete it from
		// request
		List<ActorEntity> actorRequestList = deleteNonExistentActors(actorEntityListFromDB,
				movieEntityRequest.getActors());
		assertThat(actorRequestList.isEmpty(), is(true));

		ResponseEntity<MovieResponseDTO> movieResponse = movieService.addMovie(movieRequestDTONonExistentActors);
		assertEquals(HttpStatus.BAD_REQUEST, movieResponse.getStatusCode());

	}

	@Test
	public void testAddMovieNonExistentGenresReturnBadRequest() {

		GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
		genreRequestDTO.setId(99L);
		genreRequestDTO.setName("name");

		ArrayList<GenreRequestDTO> genreRequestDTOListNonExistentGenres = new ArrayList<GenreRequestDTO>();
		genreRequestDTOListNonExistentGenres.add(genreRequestDTO);

		MovieRequestDTO movieRequestDTONonExistentGenres = new MovieRequestDTO();
		movieRequestDTONonExistentGenres.setId(1L);
		movieRequestDTONonExistentGenres.setTitle("title");
		movieRequestDTONonExistentGenres.setYear("year");
		movieRequestDTONonExistentGenres.setActors(actorRequestDTOList);
		movieRequestDTONonExistentGenres.setGenres(genreRequestDTOListNonExistentGenres);

		MovieEntity movieEntityRequest = modelMapper.map(movieRequestDTONonExistentGenres, MovieEntity.class);
		assertNotNull(movieEntityRequest.getActors());

		when(actorRepository.findAll()).thenReturn(actorEntityListFromDB);

		// Check if the requested actor exists in database, if not exists delete it from
		// request
		List<ActorEntity> actorRequestList = deleteNonExistentActors(actorEntityListFromDB,
				movieEntityRequest.getActors());
		assertNotNull(actorRequestList);

		when(genreRepository.findAll()).thenReturn(genreEntityListFromDB);

		// Check if the requested genre exists in database, if not exists delete it from
		// request
		List<GenreEntity> genreRequestList = deleteNonExistentGenres(genreEntityListFromDB,
				movieEntityRequest.getGenres());
		assertThat(genreRequestList.isEmpty(), is(true));

		ResponseEntity<MovieResponseDTO> movieResponse = movieService.addMovie(movieRequestDTONonExistentGenres);
		assertEquals(HttpStatus.BAD_REQUEST, movieResponse.getStatusCode());

	}

	@Test
	public void testDeleteMovieReturnOk() {

		ResponseEntity<MovieResponseDTO> movieResponse = movieService.deleteMovie(1L);
		assertEquals(HttpStatus.OK, movieResponse.getStatusCode());
	}

	private static MovieRequestDTO deleteDuplicatesActorsAndGenres(MovieRequestDTO movieRequestDTO,
			List<ActorRequestDTO> actors, List<GenreRequestDTO> genres) {

		HashSet<Object> seenActor = new HashSet<>();
		HashSet<Object> seenGenre = new HashSet<>();

		// removeIf will remove an element if it meets the specified criteria.
		// Set.add will return false if it did not modify the Set, i.e. already contains
		// the value.
		actors.removeIf(a -> !seenActor.add(a.getId()));
		genres.removeIf(g -> !seenGenre.add(g.getId()));

		movieRequestDTO.setActors(actors);
		movieRequestDTO.setGenres(genres);

		return movieRequestDTO;
	}

	private static List<ActorEntity> deleteNonExistentActors(List<ActorEntity> actorEntityListFromDB,
			List<ActorEntity> actorRequestList) {

		List<ActorEntity> toRemove = new ArrayList<>();

		// Loop actorRequestList items
		for (ActorEntity actorFromRequest : actorRequestList) {

			// Loop actorEntityListFromDB items
			boolean found = false;

			for (ActorEntity actorFromDB : actorEntityListFromDB) {
				if (actorFromRequest.getId() == actorFromDB.getId()) {
					found = true;
				}
			}

			// If the id does not exist, we save the array with non existent actor.
			// request.
			if (!found) {
				toRemove.add(actorFromRequest);
			}

		}

		// Remove non existent actor array from the request.
		actorRequestList.removeAll(toRemove);

		return actorRequestList;

	}

	private static List<GenreEntity> deleteNonExistentGenres(List<GenreEntity> genreEntityListFromDB,
			List<GenreEntity> genreRequestList) {

		List<GenreEntity> toRemove = new ArrayList<>();

		// Loop genreRequestList items
		for (GenreEntity genreFromRequest : genreRequestList) {

			// Loop genreEntityListFromDB items
			boolean found = false;

			for (GenreEntity genreFromDB : genreEntityListFromDB) {
				if (genreFromRequest.getId() == genreFromDB.getId()) {
					found = true;
				}
			}

			// If the id does not exist, we save the array with non existent actor.
			// request.
			if (!found) {
				toRemove.add(genreFromRequest);
			}

		}

		// Remove non existent genre array from the request.
		genreRequestList.removeAll(toRemove);

		return genreRequestList;

	}

}