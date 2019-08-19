package com.idosinchuk.handlemovies.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Request DTO for Actor
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Data
@ApiModel(reference = "ActorRequest", description = "Model request for manage the actors.")
public class ActorRequestDTO {

	@ApiModelProperty(value = "Actor id", example = "1")
	private Long id;

	@ApiModelProperty(value = "Actor name", example = "Jhon")
	@NotNull(message = "{actor.name.notNull}")
	@Size(min = 1, max = 60, message = "{actor.name.size}")
	private String name;

	@ApiModelProperty(value = "Actor surname", example = "Doe")
	@NotNull(message = "{actor.surname.notNull}")
	@Size(min = 1, max = 60, message = "{actor.surname.size}")
	private String surname;

}
