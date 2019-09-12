package com.idosinchuk.handlemovies.microservice.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for Movie
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "MovieRequest", description = "Model request for manage the films.")
public class MovieRequestDTO {

	@ApiModelProperty(value = "Movie id", example = "1")
	private Long id;

	@ApiModelProperty(value = "Title name", example = "SmartUp the Film")
	@NotNull(message = "{movie.name.notNull}")
	@Size(min = 1, max = 150, message = "{movie.name.size}")
	private String title;

	@ApiModelProperty(value = "Year", example = "2019")
	@NotNull(message = "{year.name.notNull}")
	@Size(min = 4, max = 4, message = "{year.name.size}")
	private String year;

	@ApiModelProperty(value = "Actors", example = "List of actors")
	private List<ActorRequestDTO> actors;

	@ApiModelProperty(value = "Genres", example = "List of genres")
	private List<GenreRequestDTO> genres;

}
