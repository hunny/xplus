package com.xplus.commons.spring.batch;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

/**
 * 5. 数据检验。
 * 
 */
public class CsvBeanValidator<T> implements Validator<T>, InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(CsvBeanValidator.class);
	
	private javax.validation.Validator validator;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.usingContext().getValidator();
	}

	@Override
	public void validate(T value) throws ValidationException {
		Set<ConstraintViolation<T>> constraintViolations = //
				validator.validate(value); //使用Validator的validate方法校验数据。
		logger.info("验证数据：{}", value);
		if (constraintViolations.isEmpty()) {// 检验通过
			logger.info("没有校验不通过的数据，直接忽略。");
			return;
		}
		StringBuilder message = new StringBuilder();
		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			message.append(constraintViolation.toString());
			message.append("\n");
		}
		throw new ValidationException(message.toString());
	}

}
