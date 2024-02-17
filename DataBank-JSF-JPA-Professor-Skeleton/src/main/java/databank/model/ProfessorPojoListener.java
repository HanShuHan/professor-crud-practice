
package databank.model;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class ProfessorPojoListener {

	@PrePersist
	public void setCreatedOnDate(ProfessorPojo professor) {
		LocalDateTime now = LocalDateTime.now();
		professor.setCreated(now);
		professor.setUpdated(now);
	}

	@PreUpdate
	public void setUpdatedDate(ProfessorPojo professor) {
		professor.setUpdated(LocalDateTime.now());
	}

}
