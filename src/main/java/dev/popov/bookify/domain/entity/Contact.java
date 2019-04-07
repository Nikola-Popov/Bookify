package dev.popov.bookify.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Column(name = "firstName")
	private String firstName;

	@Column(name = "lastName")
	private String lastName;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "address")
	private String address;
}
