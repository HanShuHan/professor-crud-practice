/***************************************************************************
 * File:  MembershipCard.java Course materials (23S) CST 8277
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

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@SuppressWarnings("unused")

/**
 * The persistent class for the membership_card database table.
 */
// MC01 - Add the missing annotations.
// MC02 - Do we need a mapped super class?  If so, which one?
@Entity
@Table(name = "membership_card", catalog = "acmecollege")
@AttributeOverride(name = "id", column = @Column(name = "card_id"))
@NamedQuery(name = MembershipCard.FIND_BY_ID, query = "SELECT mc FROM MembershipCard mc WHERE mc.id = :param1")
public class MembershipCard extends PojoBase implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_ID = "MembershipCard.findById";

	//  MC03 - Add annotations for 1:1 mapping.  Changes here should cascade.
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "membership_id", referencedColumnName = "membership_id", nullable = false)
	private ClubMembership clubMembership;

	//  MC04 - Add annotations for M:1 mapping.  Changes here should not cascade.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
	private Student owner;

	//  MC05 - Add annotations.
	@Column(name = "signed", columnDefinition = "BIT(1)", nullable = false)
	private byte signed;

	public MembershipCard() {
		super();
	}
	
	public MembershipCard(ClubMembership clubMembership, Student owner, byte signed) {
		this();
		this.clubMembership = clubMembership;
		this.owner = owner;
		this.signed = signed;
	}

	@JsonBackReference
	public ClubMembership getClubMembership() {
		return clubMembership;
	}

	public void setClubMembership(ClubMembership clubMembership) {
		this.clubMembership = clubMembership;
		
		if (clubMembership != null) {
			clubMembership.setCard(this);
		}
	}

	public Student getOwner() {
		return owner;
	}

	public void setOwner(Student owner) {
		this.owner = owner;
		
		if (owner != null) {
			owner.getMembershipCards().add(this);
		}
	}

	public byte getSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = (byte) (signed ? 0b0001 : 0b0000);
	}
	
	//Inherited hashCode/equals is sufficient for this entity class

}