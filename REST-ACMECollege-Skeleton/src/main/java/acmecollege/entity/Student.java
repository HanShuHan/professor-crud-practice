/***************************************************************************
 * File:  Student.java Course materials (23S) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 * Updated by:  Group NN
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The persistent class for the student database table.
 */
@SuppressWarnings("unused")
// ST01 - Add the missing annotations.
@Entity
// ST02 - Do we need a mapped super class? If so, which one?
@Table(name = "student", catalog = "acmecollege")
public class Student extends PojoBase implements Serializable {
	
	private static final long serialVersionUID = 1L;

    public Student() {
    	super();
    }

    //  ST03 - Add annotation
    @Column(name = "first_name", length = 50, nullable = false)
	private String firstName;

	//  ST04 - Add annotation
    @Column(name = "last_name", length = 50, nullable = false)
	private String lastName;

	//  ST05 - Add annotations for 1:M relation.  Changes should not cascade.
    @OneToMany(mappedBy = "owner", cascade = CascadeType.MERGE)
	private Set<MembershipCard> membershipCards = new HashSet<>();

	//  ST06 - Add annotations for 1:M relation.  Changes should not cascade.
	@OneToMany(mappedBy = "student", cascade = CascadeType.MERGE)
	private Set<CourseRegistration> courseRegistrations = new HashSet<>();

	@OneToMany(mappedBy = "student")
	private Set<SecurityUser> securityUsers = new HashSet<>();
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    // Simplify JSON body, skip MembershipCards
    @JsonIgnore
    public Set<MembershipCard> getMembershipCards() {
		return membershipCards;
	}

	public void setMembershipCards(Set<MembershipCard> membershipCards) {
		this.membershipCards = membershipCards;
	}

    // Simplify JSON body, skip CourseRegistrations
//    @JsonIgnore
	@JsonManagedReference
    public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

//	@JsonIgnore
	@JsonManagedReference
	public Set<SecurityUser> getSecurityUsers() {
		return securityUsers;
	}

	public void setSecurityUsers(Set<SecurityUser> securityUsers) {
		this.securityUsers = securityUsers;
	}
	
	//Inherited hashCode/equals is sufficient for this entity class

}
