/*****************************************************************
 * File:  EmailValidator.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator<String> {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

	@Override
	public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
		if (value == null) {
			FacesMessage msg = new FacesMessage("Email should not be empty", "Invalid email format.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		} else {
			Matcher matcher = EMAIL_PATTERN.matcher(value);
			if (!matcher.matches()) {
				FacesMessage msg = new FacesMessage("Email should be the pattern of ?@?", "Invalid email format.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

}
