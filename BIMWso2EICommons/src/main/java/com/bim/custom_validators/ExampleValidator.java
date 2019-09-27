package com.bim.custom_validators;

import org.wso2.carbon.dataservices.core.engine.ParamValue;
import org.wso2.carbon.dataservices.core.validation.ValidationContext;
import org.wso2.carbon.dataservices.core.validation.ValidationException;
import org.wso2.carbon.dataservices.core.validation.Validator;

public class ExampleValidator implements Validator {
	
	private int minLength = 2;
	
	public void validate(ValidationContext context, String name, ParamValue value)
		throws ValidationException {
		
		if (!isEmpty(value)) {
			
			String numTransac = value.getScalarValue();
			System.out.println("numTransac: " + numTransac);
			
			if(numTransac.equalsIgnoreCase("Test"))
				throw new ValidationException("Invalid name value", name, value);
			
			if(numTransac.length() < minLength)
				throw new ValidationException("Invalid length param value", name, value);
		}
	}
	
	private boolean isEmpty(ParamValue val) {
        return val == null || val.getScalarValue() == null || val.getScalarValue().length() == 0;
    }
}
