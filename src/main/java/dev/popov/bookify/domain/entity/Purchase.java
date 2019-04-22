package dev.popov.bookify.domain.entity;

import static javax.persistence.CascadeType.MERGE;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
public class Purchase extends BaseEntity {
	@ManyToOne(targetEntity = User.class, cascade = MERGE)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@OneToOne(cascade = MERGE)
	@JoinColumn(name = "purchase_id", referencedColumnName = "id")
	private EventOrder event;
}
