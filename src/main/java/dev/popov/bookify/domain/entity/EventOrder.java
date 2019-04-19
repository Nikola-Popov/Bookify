package dev.popov.bookify.domain.entity;

import static javax.persistence.CascadeType.MERGE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_orders")
@Getter
@Setter
@NoArgsConstructor
public class EventOrder extends BaseEntity {
	@Column(name = "quantity")
	@Min(value = 1, message = "Minimum is 1 item quantity")
	private int quantity;

	@ManyToOne(targetEntity = Event.class, cascade = MERGE)
	@JoinColumn(name = "event_id", referencedColumnName = "id")
	private Event event;
}
