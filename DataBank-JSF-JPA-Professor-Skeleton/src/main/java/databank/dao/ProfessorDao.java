
package databank.dao;

import java.util.List;

import databank.model.ProfessorPojo;

/**
 * Description:  API for the database C-R-U-D operations
 */
public interface ProfessorDao {

	List<ProfessorPojo> readAllProfessors();

	// C
	ProfessorPojo createProfessor(ProfessorPojo professor);

	// R
	ProfessorPojo readProfessorById(int professorId);

	// U
	ProfessorPojo updateProfessor(ProfessorPojo professor);

	// D
	void deleteProfessorById(int professorId);

}
