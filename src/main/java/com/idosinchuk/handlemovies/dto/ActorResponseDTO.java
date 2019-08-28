package com.idosinchuk.handlemovies.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for Actor
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ActorResponse", description = "Model response for manage the actors.")
public class ActorResponseDTO {

	@ApiModelProperty(value = "Actor id", example = "1")
	private Long id;

	@ApiModelProperty(value = "Actor name", example = "Jhon")
	private String name;

	@ApiModelProperty(value = "Actor surname", example = "Doe")
	private String surname;

}
