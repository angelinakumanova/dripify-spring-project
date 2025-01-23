package com.dripify.web.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class MatchFieldsValidator implements ConstraintValidator<MatchFields, Object> {

    private String firstField;
    private String secondField;

    @Override
    public void initialize(MatchFields constraintAnnotation) {
        this.firstField = constraintAnnotation.firstField();
        this.secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        Object firstValue = beanWrapper.getPropertyValue(firstField);
        Object secondValue = beanWrapper.getPropertyValue(secondField);

        if (firstValue == null || secondValue == null) {
            return false;
        }

        return firstValue.equals(secondValue); // Check if values match
    }
}
