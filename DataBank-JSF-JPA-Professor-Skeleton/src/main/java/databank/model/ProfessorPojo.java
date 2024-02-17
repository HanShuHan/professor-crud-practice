
package databank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.faces.view.ViewScoped;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@ViewScoped
@Entity(name = "prof")
@Table(name = "professor", catalog = "databank", schema = "")
@Access(AccessType.FIELD)
@EntityListeners(value = { ProfessorPojoListener.class })
@NamedQuery(name = ProfessorPojo.PROFESSOR_FIND_ALL, query = "FROM prof")
@NamedQuery(name = ProfessorPojo.PROFESSOR_FIND_ID, query = "SELECT p FROM prof p WHERE p.id = :id")
public class ProfessorPojo implements Serializable {

	private static final long serialVersionUID = 6415061855277821061L;

	public static final String PROFESSOR_FIND_ALL = "Professor.findAll";
	public static final String PROFESSOR_FIND_ID = "Professor.findById";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Basic(optional = false)
	@Column(name = "last_name")
	private String lastName;

	@Basic(optional = false)
	@Column(name = "first_name")
	private String firstName;

	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phoneNumber;

	@Column(name = "degree")
	private String degree;

	@Column(name = "major")
	private String major;

	@Column(name = "created")
	private LocalDateTime created;

	@Column(name = "updated")
	private LocalDateTime updated;

	@Version
	private int version = 1;

	@Transient
	private boolean editable;

	public ProfessorPojo() {
		super();
	}

	public int getId() {
		return id;
	}

	/**
	 * @param id new value for id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the value for lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName new value for lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the value for firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName new value for firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean getEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();

		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof ProfessorPojo otherProfessorPojo) {
			return Objects.equals(this.getId(), otherProfessorPojo.getId());
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Professor [id = ").append(getId());
		if (getLastName() != null) {
			builder.append(", ").append("lastName = ").append(getLastName());
		}
		if (getFirstName() != null) {
			builder.append(", ").append("firstName = ").append(getFirstName());
		}
		if (getPhoneNumber() != null) {
			builder.append(", ").append("phoneNumber = ").append(getPhoneNumber());
		}
		if (getEmail() != null) {
			builder.append(", ").append("email = ").append(getEmail());
		}
		if (getDegree() != null) {
			builder.append(", ").append("degree = ").append(getDegree());
		}
		if (getMajor() != null) {
			builder.append(", ").append("major = ").append(getMajor());
		}
		if (getCreated() != null) {
			builder.append(", ").append("created = ").append(getCreated());
		}
		if (getUpdated() != null) {
			builder.append(", ").append("updated = ").append(getUpdated());
		}
		builder.append(", ").append("version = ").append(getVersion());
		builder.append("]");
		return builder.toString();
	}

}
