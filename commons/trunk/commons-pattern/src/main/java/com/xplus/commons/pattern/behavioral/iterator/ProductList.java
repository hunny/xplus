package com.xplus.commons.pattern.behavioral.iterator;

import java.util.List;

public class ProductList extends AbstractList<String> {

	public ProductList(List<String> objects) {
		super(objects);
	}

	@Override
	public Iterator<String> createIterator() {
		return new ProductIterator(this);
	}

}
