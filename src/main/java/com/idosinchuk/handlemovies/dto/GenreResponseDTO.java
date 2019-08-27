package com.idosinchuk.handlemovies.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Response DTO for Genre
 * 
 * @author Igor Dosinchuk
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Data
@ApiModel(reference = "GenreResponse", description = "Model response for manage the genres.")
public class GenreResponseDTO {

	@ApiModelProperty(value = "Genre id", example = "1")
	private Long id;

	@ApiModelProperty(value = "Genre name", example = "Action")
	private String name;

}
