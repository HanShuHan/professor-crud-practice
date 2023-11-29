/***************************************************************************
 * File:  ClubMembership.java Course materials (23S) CST 8277
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
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@SuppressWarnings("unused")

/**
 * The persistent class for the club_membership database table.
 */
// CM01 - Add the missing annotations.
// CM02 - Do we need a mapped super class?  If so, which one?
@Entity
@Table(name = "club_membership", catalog = "acmecollege")
@AttributeOverride(name = "id", column = @Column(name = "membership_id"))
@NamedQuery(name = ClubMembership.FIND_BY_ID, query = "SELECT cm FROM ClubMembership cm LEFT JOIN FETCH cm.card LEFT JOIN FETCH cm.club WHERE cm.id = :param1")
public class ClubMembership extends PojoBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3563882315743701642L;

	public static final String FIND_BY_ID = "ClubMembership.findById";

	// CM03 - Add annotations for M:1. Changes to this class should cascade to
	// StudentClub.
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "club_id", referencedColumnName = "club_id")
	private StudentClub club;

	// CM04 - Add annotations for 1:1. Changes to this class should not cascade to
	// MembershipCard.
	@OneToOne(mappedBy = "clubMembership", fetch = FetchType.LAZY)
	private MembershipCard card;

	@Embedded
	private DurationAndStatus durationAndStatus;

	public ClubMembership() {
		durationAndStatus = new DurationAndStatus();
	}

	@JsonBackReference
	public StudentClub getStudentClub() {
		return club;
	}

	public void setStudentClub(StudentClub club) {
		this.club = club;

//		if (club != null) {
//			Set<ClubMembership> clubMemberships = club.getClubMemberships();
//			
//			if (clubMemberships == null) {
//				clubMemberships = new HashSet<>();
//			}
//			
//			club.getClubMemberships().add(this);
//		}
	}

	@JsonManagedReference
	public MembershipCard getCard() {
		return card;
	}

	public void setCard(MembershipCard card) {
		this.card = card;
	}

	public DurationAndStatus getDurationAndStatus() {
		return durationAndStatus;
	}

	public void setDurationAndStatus(DurationAndStatus durationAndStatus) {
		this.durationAndStatus = durationAndStatus;
	}

	// Inherited hashCode/equals NOT sufficient for this Entity class
	/**
	 * Very important: Use getter's for member variables because JPA sometimes needs
	 * to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an
		// object's lifecycle,
		// they shouldn't be part of the hashCode calculation

		// include DurationAndStatus in identity
		return prime * result + Objects.hash(getId(), getDurationAndStatus());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof ClubMembership otherClubMembership) {
			// See comment (above) in hashCode(): Compare using only member variables that
			// are
			// truly part of an object's identity
			return Objects.equals(this.getId(), otherClubMembership.getId())
					&& Objects.equals(this.getDurationAndStatus(), otherClubMembership.getDurationAndStatus());
		}
		return false;
	}
}
