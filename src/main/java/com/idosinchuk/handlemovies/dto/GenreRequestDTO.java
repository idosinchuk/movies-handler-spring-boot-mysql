package com.idosinchuk.handlemovies.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for Genre
 * 
 * @author Igor Dosinchuk
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "GenreRequest", description = "Model request for manage the genres.")
public class GenreRequestDTO {

	@ApiModelProperty(value = "Genre id", example = "1")
	private Long id;

	@ApiModelProperty(value = "Genre name", example = "Action")
	@NotNull(message = "{genre.name.notNull}")
	@Size(min = 1, max = 60, message = "{genre.name.size}")
	private String name;

}
