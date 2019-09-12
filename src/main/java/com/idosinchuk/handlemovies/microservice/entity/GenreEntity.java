package com.idosinchuk.handlemovies.microservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for Genre
 * 
 * @author Igor Dosinchuk
 *
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "genre")
public class GenreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "genre_id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

}
