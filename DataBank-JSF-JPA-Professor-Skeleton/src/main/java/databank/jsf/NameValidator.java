package databank.jsf;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("nameValidator")
public class NameValidator implements Validator<String> {

	private static final Pattern NAME_PATTERN;

	static {
		NAME_PATTERN = Pattern.compile("^[a-zA-Z]+(-[a-zA-Z]+)*$");
	}

	@Override
	public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
		if (value == null) {
			FacesMessage msg = new FacesMessage("Name should not be empty.", "Name should not be empty.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		} else {
			final int nameLength = value.length();
			if (nameLength < 1 || nameLength > 50) {
				ResourceBundle uiconsts = ResourceBundle.getBundle("Bundle");

				FacesMessage msg = new FacesMessage("Name should be 1 to 50 charaters", uiconsts.getString("enter_valid_string"));
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
			Matcher matcher = NAME_PATTERN.matcher(value);
			if (!matcher.matches()) {
				FacesMessage msg = new FacesMessage("Invalid name format.",
						"Name should be english letter(s) optionally followed by a group of a dash and more letter(s).");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}

}
