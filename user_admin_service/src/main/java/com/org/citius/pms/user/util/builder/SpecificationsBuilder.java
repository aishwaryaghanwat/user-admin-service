package com.org.citius.pms.user.util.builder;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author Aishwarya Ghanwat
 * @version 1.0.0
 * @implNote A generic builder class to construct a {@link Specifications}.
 * @since 2021
 */
public class SpecificationsBuilder<T> {

	private Specification<T> specifications;
	private boolean isEmpty = true;

	public SpecificationsBuilder<T> addSpecification(Specification<T> specification) {
		if (null == specification)
			return this;
		if (isEmpty) {
			specifications = Specification.where(specification);
			isEmpty = false;
		} else
			specifications = specifications.and(specification);
		return this;
	}

	public Specification<T> build() {
		return specifications;
	}
}