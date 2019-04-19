package dev.popov.bookify.domain.model.binding;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventCartAddBindingModel {
	@NotEmpty
	@NotNull
	private String id;

	@NotNull
	@Min(value = 1, message = "You must select atleast 1 voucher")
	private int vouchersCount;
}
