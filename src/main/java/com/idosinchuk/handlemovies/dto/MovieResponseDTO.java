package com.idosinchuk.handlemovies.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Response DTO for Movie
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Data
@ApiModel(reference = "MovieResponse", description = "Model response for manage the films.")
public class MovieResponseDTO {

	@ApiModelProperty(value = "Movie id", example = "1")
	private Long id;

	@ApiModelProperty(value = "Title name", example = "SmartUp the Film")
	private String title;

	@ApiModelProperty(value = "Year", example = "2019")
	private String year;

	@ApiModelProperty(value = "Actors", example = "List of actors")
	private List<ActorResponseDTO> actors;

	@ApiModelProperty(value = "Genres", example = "List of genres")
	private List<GenreResponseDTO> genres;

}
