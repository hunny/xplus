package com.xplus.commons.batch.spring.single;

import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

/**
 * 4. 数据处理。
 */
public class CsvItemProcessor extends ValidatingItemProcessor<Person> {

	@Override
	public Person process(Person item) throws ValidationException {
		super.process(item); // 这个会调用默认的校验器
		if ("汉族".equals(item.getNation())) {
			item.setNation("01");
		} else {
			item.setNation("02");
		}
		return item;
	}

}
