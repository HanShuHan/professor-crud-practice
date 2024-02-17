
package databank.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

import databank.ejb.ProfessorService;
import databank.model.ProfessorPojo;

/**
 * Description:  Implements the C-R-U-D API for the database
 */
@SessionScoped
public class ProfessorDaoImpl implements ProfessorDao, Serializable {

	/** explicitly set serialVersionUID */
	private static final long serialVersionUID = -5291890394373508757L;

	@EJB
	protected ProfessorService professorService;

	@Override
	public List<ProfessorPojo> readAllProfessors() {
		return professorService.findAllProfessors();
	}

	@Override
	public ProfessorPojo createProfessor(final ProfessorPojo professor) {
		return professorService.persistProfessor(professor);
	}

	@Override
	public ProfessorPojo readProfessorById(final int professorId) {
		return professorService.findProfessorById(professorId);
	}

	@Override
	public ProfessorPojo updateProfessor(final ProfessorPojo professorWithUpdates) {
		return professorService.mergeProfessor(professorWithUpdates);
	}

	@Override
	public void deleteProfessorById(final int professorId) {
		professorService.removeProfessorById(professorId);
	}

}
