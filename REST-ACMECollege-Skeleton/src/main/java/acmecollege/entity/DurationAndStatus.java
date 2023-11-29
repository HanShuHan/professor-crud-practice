/***************************************************************************
 * File:  DurationAndStatus.java Course materials (23S) CST 8277
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
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;

// DS01 - This class is not an entity however it can be embedded in other entities.  Add missing annotation.
@Embeddable
public class DurationAndStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//  DS02 - Add annotations
	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;
	
	//  DS03 - Add annotations
	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	//  DS04 - Add annotations
	@Column(name = "active", columnDefinition = "BIT(1)", nullable = false)
	private byte active;

	public DurationAndStatus() {
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public void setDurationAndStatus(LocalDateTime startDate, LocalDateTime endDate, String active) {
		setStartDate(startDate);
		setEndDate(endDate);
		byte p = 0b1;
		byte n = 0b0;
		setActive(("+".equals(active) ? p : n));
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
		this.active = active;
	}
	
	@JsonIgnore
	public boolean isNotNull() {
		return !(getStartDate() == null || getEndDate()== null);
	}

	/**
	 * Very important:  Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		return prime * result + Objects.hash(getStartDate(), getEndDate(), getActive());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof DurationAndStatus otherDurationAndStatus) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return Objects.equals(this.getStartDate(), otherDurationAndStatus.getStartDate()) &&
					Objects.equals(this.getEndDate(), otherDurationAndStatus.getEndDate()) &&
					Objects.equals(this.getActive(), otherDurationAndStatus.getActive());
		}
		return false;
	}

	
}
