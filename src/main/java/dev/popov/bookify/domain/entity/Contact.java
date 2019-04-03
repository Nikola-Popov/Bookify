package dev.popov.bookify.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customers")
public class Contact extends BaseEntity {
	@Column(name = "name")
	private String name;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@OneToOne
	@JoinColumn(name = "location_id")
	private Location location;
}
