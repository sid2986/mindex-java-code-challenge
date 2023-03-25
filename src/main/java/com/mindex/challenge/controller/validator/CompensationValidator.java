package com.mindex.challenge.controller.validator;

import com.mindex.challenge.data.Compensation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CompensationValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationValidator.class);
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public CompensationValidator() {
    }

    /**
     *  validate the input employee id string
     * @param id employee id
     * @param clazz the supplied class type
     * @param errors to store errors
     */
    public void validateId(String id, Class<?> clazz, Errors errors) {
        PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = new PropertyDescriptor("employeeId", clazz);
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Invalid class for validation", e);
        }

        if (!propertyDescriptor.getReadMethod().getReturnType().equals(String.class)) {
            throw new IllegalArgumentException("employeeId property must be of type String");
        }
        String employeeId = propertyDescriptor.getName();
        if (id.toLowerCase().contentEquals(":id") || !StringUtils.hasText(StringUtils.trimAllWhitespace(id))) {
            errors.rejectValue("", employeeId, "Employee Id cannot be null or empty");
        } else if (!id.matches("^[a-zA-Z0-9_-]*$")) {
            errors.rejectValue("", employeeId,"Employee ID should not contain invalid characters");
        }
    }

    /**
     * Basic validation for compensation
     * @param target the supplied request
     * @param errors to store errors
     */
    public void validate(Object target, Errors errors) {
        LOG.debug("Validating [{}]", target);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "employeeId", "employeeId.required", "Employee ID is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "salary", "salary.required", "Salary is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "effectiveDate", "effectiveDate.required", "Effective Date is required");

        Compensation compensation = (Compensation) target;

        //Validate employee id for speacial characters
        String id = compensation.getEmployeeId();
        if (!id.matches("^[a-zA-Z0-9_-]*$")) {
            errors.rejectValue("employeeId", "employeeId.invalid", "Employee ID contains invalid characters");
        }

        // Validate salary
        if (compensation.getSalary() != null && compensation.getSalary().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("salary", "salary.negative");
        }

        // Validate effective date
        LocalDate effectiveDate = compensation.getEffectiveDate();
        if (effectiveDate != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
                formatter.parse(effectiveDate.toString());
            } catch (DateTimeParseException e) {
                errors.rejectValue("effectiveDate", "effectiveDate.invalidFormat");
            }
        }
    }
}
