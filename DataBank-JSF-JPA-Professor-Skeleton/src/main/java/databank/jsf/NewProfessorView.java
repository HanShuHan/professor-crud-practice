/*****************************************************************
 * File:  NewProfessorView.java Course materials (23W) CST8277
 *
 * @author Teddy Yap
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.io.Serializable;

import javax.faces.annotation.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import databank.model.ProfessorPojo;

/**
 * This class represents the scope of adding a new professor to the DB.
 */
@Named(value = "newProfessor")
@ViewScoped
public class NewProfessorView implements Serializable {

	/** explicit set serialVersionUID */
	private static final long serialVersionUID = -6096342184732960364L;

	protected String lastName;
	protected String firstName;
	protected String email;
	protected String phoneNumber;
	protected String degree;
	protected String major;

	@Inject
	@ManagedProperty("#{professorController}")
	protected ProfessorController professorController;

	public NewProfessorView() {
	}

	/**
	 * @return last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber phone number to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return degree
	 */
	public String getDegree() {
		return degree;
	}

	/**
	 * @param degree degree to set
	 */
	public void setDegree(String degree) {
		this.degree = degree;
	}

	/**
	 * @return major
	 */
	public String getMajor() {
		return major;
	}

	/**
	 * @param major major to set
	 */
	public void setMajor(String major) {
		this.major = major;
	}

	public void addProfessor() {
		if (allNotNullOrEmpty(firstName, lastName)) {
			ProfessorPojo theNewProfessor = new ProfessorPojo();

			theNewProfessor.setFirstName(getFirstName());
			theNewProfessor.setLastName(getLastName());
			theNewProfessor.setEmail(getEmail());
			theNewProfessor.setPhoneNumber(getPhoneNumber());
			theNewProfessor.setDegree(getDegree());
			theNewProfessor.setMajor(getMajor());

			professorController.addNewProfessor(theNewProfessor);

			// Set everything else to null
			setFirstName(null);
			setLastName(null);
			setEmail(null);
			setPhoneNumber(null);
			setDegree(null);
			setMajor(null);

			// Clean up
			professorController.toggleAdding();
		}
	}

	private static boolean allNotNullOrEmpty(final Object... values) {
		if (values == null) {
			return false;
		}
		for (final Object val : values) {
			if (val == null) {
				return false;
			}
			if (val instanceof String str && str.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
