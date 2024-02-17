
package databank.ejb;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import databank.model.ProfessorPojo;

@Stateless
public class ProfessorService implements Serializable {

	/** explicitly set serialVersionUID */
	private static final long serialVersionUID = 7054585366351460428L;

	//Get the log4j2 logger for this class
	private static final Logger LOG = LogManager.getLogger();

	@PersistenceContext(unitName = "PU_DataBank")
	protected EntityManager em;
	
//	public ProfessorService() {
//		// TODO Auto-generated constructor stub
//	}
	
	public List<ProfessorPojo> findAllProfessors() {
		LOG.debug("reading all professors");
		
		return em.createNamedQuery(ProfessorPojo.PROFESSOR_FIND_ALL, ProfessorPojo.class)
				.getResultList();
	}

	@Transactional
	public ProfessorPojo persistProfessor(final ProfessorPojo professor) {
		LOG.debug("creating a professor = {}", professor);
		
		em.persist(professor);
		return professor;
	}

	@Transactional
	public ProfessorPojo findProfessorById(final int professorId) {
		LOG.debug("read a specific professor = {}", professorId);

		return em.createNamedQuery(ProfessorPojo.PROFESSOR_FIND_ID, ProfessorPojo.class)
				.setParameter("id", professorId)
				.getSingleResult();
	}

	@Transactional
	public ProfessorPojo mergeProfessor(final ProfessorPojo professorWithUpdates) {
		LOG.debug("updating a specific professor = {}", professorWithUpdates);

		return em.merge(professorWithUpdates);
	}

	@Transactional
	public void removeProfessorById(final int professorId) {
		LOG.debug("deleting a specific professorID = {}", professorId);
		
		final ProfessorPojo professor = findProfessorById(professorId);
		
		LOG.debug("deleting a specific professor = {}", professor);
		
		if (professor != null) {
			em.refresh(professor);
			em.remove(professor);
		}
	}

}
