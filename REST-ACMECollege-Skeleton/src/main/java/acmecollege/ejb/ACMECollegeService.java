/**
 * File:  ACMEColegeService.java
 * Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Updated by:  Group NN
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *
 */
package acmecollege.ejb;

import static acmecollege.utility.MyConstants.DEFAULT_KEY_SIZE;
import static acmecollege.utility.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static acmecollege.utility.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static acmecollege.utility.MyConstants.DEFAULT_SALT_SIZE;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PREFIX;
import static acmecollege.utility.MyConstants.PARAM1;
import static acmecollege.utility.MyConstants.PROPERTY_ALGORITHM;
import static acmecollege.utility.MyConstants.PROPERTY_ITERATIONS;
import static acmecollege.utility.MyConstants.PROPERTY_KEY_SIZE;
import static acmecollege.utility.MyConstants.PROPERTY_SALT_SIZE;
import static acmecollege.utility.MyConstants.PU_NAME;
import static acmecollege.utility.MyConstants.USER_ROLE;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmecollege.entity.ClubMembership;
import acmecollege.entity.Course;
import acmecollege.entity.CourseRegistration;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.Professor;
import acmecollege.entity.SecurityRole;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;
import acmecollege.entity.StudentClub;

@SuppressWarnings("unused")

/**
 * Stateless Singleton EJB Bean - ACMECollegeService
 */
@Singleton
public class ACMECollegeService implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LogManager.getLogger();

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;

	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<Student> getAllStudents() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Student> cq = cb.createQuery(Student.class);
		cq.select(cq.from(Student.class));

		return em.createQuery(cq).getResultList();
	}

	public Student getStudentById(int id) {
		return em.find(Student.class, id);
	}

	@Transactional
	public Student persistStudent(Student newStudent) {
		em.persist(newStudent);

		return newStudent;
	}

	@Transactional
	public void buildUserForNewStudent(Student newStudent) {
		SecurityUser userForNewStudent = new SecurityUser();
		Map<String, String> pbAndjProperties = new HashMap<>();

		pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
		pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
		pbAndjProperties.put(PROPERTY_SALT_SIZE, DEFAULT_SALT_SIZE);
		pbAndjProperties.put(PROPERTY_KEY_SIZE, DEFAULT_KEY_SIZE);
		pbAndjPasswordHash.initialize(pbAndjProperties);

		String pwHash = pbAndjPasswordHash.generate(DEFAULT_USER_PASSWORD.toCharArray());

		userForNewStudent.setUsername(DEFAULT_USER_PREFIX + "_" + newStudent.getFirstName() + "." + newStudent.getLastName());
		userForNewStudent.setPwHash(pwHash);
		userForNewStudent.setStudent(newStudent);

		/* ACMECS01 - Use NamedQuery on SecurityRole to find USER_ROLE */
		SecurityRole userRole = em.createNamedQuery(SecurityRole.FIND_BY_ROLE_NAME, SecurityRole.class)
				.setParameter(PARAM1, USER_ROLE).getSingleResult();

		userForNewStudent.getRoles().add(userRole);
		userRole.getUsers().add(userForNewStudent);
		em.persist(userForNewStudent);

		newStudent.getSecurityUsers().add(userForNewStudent);
		em.merge(newStudent);

		em.merge(newStudent);
	}

	public CourseRegistration findStudentCourseRegistrationBy(int studentId, int courseId) {
		Student student = getStudentById(studentId);
		if (student == null) {
			return null;
		}

		CourseRegistration courseRegistrataion = student.getCourseRegistrations().stream()
				.filter(courseReg -> courseReg.getCourse().getId() == courseId).findFirst().orElse(null);

		return courseRegistrataion;
	}

	public CourseRegistration findStudentCourseRegistrationBy(Student student, int courseId) {
		if (student != null) {
			return findStudentCourseRegistrationBy(student.getId(), courseId);
		} else {
			return null;
		}
	}

	@Transactional
	public Professor setProfessorForStudentCourse(int studentId, int courseId, Professor newProfessor) {
		Student student = getStudentById(studentId);
		if (student == null) {
			return null;
		}
		CourseRegistration courseRegistration = findStudentCourseRegistrationBy(studentId, courseId);
		if (courseRegistration == null) {
			return null;
		}

		Professor updatedProfessor = null;
		if (newProfessor != null) {
			Professor foundProfessor = getById(Professor.class, Professor.FIND_BY_ID, newProfessor.getId());
			String firstName = newProfessor.getFirstName();
			String lastName = newProfessor.getLastName();
			String department = newProfessor.getDepartment();

			if (foundProfessor != null) {
				if (firstName != null) {
					foundProfessor.setFirstName(firstName);
				}
				if (lastName != null) {
					foundProfessor.setLastName(lastName);
				}
				if (department != null) {
					foundProfessor.setDepartment(department);
				}
				foundProfessor.getCourseRegistrations().add(courseRegistration);

				courseRegistration.setProfessor(foundProfessor);
				em.merge(courseRegistration);

				updatedProfessor = foundProfessor;
			} else if (firstName != null && lastName != null && department != null) {
				newProfessor.getCourseRegistrations().add(courseRegistration);
				em.persist(newProfessor);

				courseRegistration.setProfessor(newProfessor);
				em.merge(courseRegistration);

				updatedProfessor = newProfessor;
			}
		} else {
			courseRegistration.setProfessor(null);
			em.merge(courseRegistration);
		}

		return updatedProfessor;

//		Student studentToBeUpdated = em.find(Student.class, studentId);
//		if (studentToBeUpdated == null) {
//			return null;
//		}
//		AtomicReference<Professor> updatedProf = new AtomicReference<Professor>(null);
//
//		if (studentToBeUpdated != null) { // Student exists
//			Set<CourseRegistration> courseRegistrations = studentToBeUpdated.getCourseRegistrations();
//
//			courseRegistrations.forEach(c -> {
//				if (c.getCourse().getId() == courseId) {
//					if (c.getProfessor() != null) {
//						Professor prof = em.find(Professor.class, c.getProfessor().getId());
//						prof.setProfessor(newProfessor.getFirstName(), newProfessor.getLastName(), newProfessor.getDepartment());
//						em.merge(prof);
//						updatedProf.set(prof);
//					} else {
//						newProfessor.getCourseRegistrations().add(c);
//						c.setProfessor(newProfessor);
//						em.merge(c);
//						updatedProf.set(newProfessor);
//					}
////					else { // Professor does not exist
////						c.setProfessor(newProfessor);
////						em.merge(studentToBeUpdated);
////					}
//				}
//			});
//		}
//
//		return updatedProf.get();
	}

	/**
	 * To update a student
	 * 
	 * @param id                 - id of entity to update
	 * @param studentWithUpdates - entity with updated information
	 * @return Entity with updated information
	 */
	@Transactional
	public Student updateStudentById(int id, Student studentWithUpdates) {
		Student studentToBeUpdated = getStudentById(id);

		if (studentToBeUpdated != null) {
			em.refresh(studentToBeUpdated);
			em.merge(studentWithUpdates);
			em.flush();
		}

		return studentToBeUpdated;
	}

	/**
	 * To delete a student by id
	 * 
	 * @param id - student id to delete
	 */
	@Transactional
	public void deleteStudentById(int id) {
		Student student = getStudentById(id);

		if (student != null) {
			em.refresh(student);

			List<SecurityUser> sUsers = em.createNamedQuery(SecurityUser.FIND_BY_STUDENT_ID, SecurityUser.class)
					.setParameter(PARAM1, id).getResultList();
			sUsers.forEach(su -> {
				Set<SecurityRole> roles = su.getRoles();

				if (roles != null && !roles.isEmpty()) {
					roles.forEach(r -> {
						r.getUsers().remove(su);
						em.merge(r);
					});
				}
				em.remove(su);
			});

			em.remove(student);

			/*
			 * ACMECS02 - Use NamedQuery on SecurityRole to find this related Student so
			 * that when we remove it, the relationship from SECURITY_USER table is not
			 * dangling
			 */
//			TypedQuery<SecurityUser> findUser = null;
//			SecurityUser sUser = findUser.getSingleResult();
//			em.remove(sUser);
//			em.remove(student);
		}
	}

	public List<StudentClub> getAllStudentClubs() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<StudentClub> cq = cb.createQuery(StudentClub.class);
		cq.select(cq.from(StudentClub.class));

		return em.createQuery(cq).getResultList();
	}

	// Why not use the build-in em.find? The named query
	// SPECIFIC_STUDENT_CLUB_QUERY_NAME
	// includes JOIN FETCH that we cannot add to the above API
	public StudentClub getStudentClubById(int id) {
		return getById(StudentClub.class, StudentClub.FIND_BY_ID, id);
	}

	// These methods are more generic.

	public <T> List<T> getAll(Class<T> entity, String namedQuery) {
		return em.createNamedQuery(namedQuery, entity).getResultList();
	}

	public <T> T getById(Class<T> entity, String namedQuery, int id) {
		T result = null;

		try {
			result = em.createNamedQuery(namedQuery, entity).setParameter(PARAM1, id).getSingleResult();
		} catch (NoResultException e) {
		}

		return result;
	}

	@Transactional
	public StudentClub deleteStudentClub(int id) {
		// StudentClub sc = getStudentClubById(id);
		StudentClub sc = getById(StudentClub.class, StudentClub.FIND_BY_ID, id);

		if (sc != null) {
			Set<ClubMembership> clubMemberships = sc.getClubMemberships();

			if (clubMemberships != null && !clubMemberships.isEmpty()) {
				clubMemberships.stream().forEach(cm -> {
//					cm.setStudentClub(null); // not needed

					if (cm.getCard() != null) {
						MembershipCard c = getById(MembershipCard.class, MembershipCard.FIND_BY_ID, cm.getCard().getId());

						c.setClubMembership(null);
						em.merge(c);

						cm.setCard(null);
						em.merge(cm); // the club membership needs to be updated anyway
					}
				});
			}
			// Removes the student club.
			em.remove(sc);
			return sc;
//			Set<ClubMembership> memberships = sc.getClubMemberships();
//			List<ClubMembership> list = new LinkedList<>();
//			memberships.forEach(list::add);
//			list.forEach(m -> {
//				if (m.getCard() != null) {
//					MembershipCard mc = getById(MembershipCard.class, MembershipCard.ID_CARD_QUERY_NAME, m.getCard().getId());
//					mc.setClubMembership(null);
//				}
//				m.setCard(null);
//				em.merge(m);
//			});
//			em.remove(sc);
//			return sc;
		}

		return null; // if the student club does not exist
	}

	// Please study & use the methods below in your test suites

	public boolean isDuplicated(StudentClub newStudentClub) {
		long count = em.createNamedQuery(StudentClub.COUNT_BY_NAME, Long.class).setParameter(PARAM1, newStudentClub.getName())
				.getSingleResult();

		return (count > 1);
	}

	@Transactional
	public StudentClub persistStudentClub(StudentClub newStudentClub) {
		em.persist(newStudentClub);

		return newStudentClub;
	}

	@Transactional
	public StudentClub updateStudentClub(int id, StudentClub updatingStudentClub) {
		StudentClub studentClubToBeUpdated = getStudentClubById(id);

		if (studentClubToBeUpdated != null) {
			em.refresh(studentClubToBeUpdated);
			studentClubToBeUpdated.setName(updatingStudentClub.getName());
			LOG.debug(studentClubToBeUpdated.getClubMemberships());
			em.merge(studentClubToBeUpdated);
			em.flush();
		}

		return studentClubToBeUpdated;
	}

	@Transactional
	public StudentClub updateStudentClub(StudentClub updatingStudentClub) {
		return em.merge(updatingStudentClub);
	}

	@Transactional
	public ClubMembership persistClubMembership(ClubMembership newClubMembership) {
		em.persist(newClubMembership);

		return newClubMembership;
	}

	public ClubMembership getClubMembershipById(int cmId) {
		return getById(ClubMembership.class, ClubMembership.FIND_BY_ID, cmId);
	}

	@Transactional
	public ClubMembership updateClubMembership(int id, ClubMembership clubMembershipWithUpdates) {
		ClubMembership clubMembershipToBeUpdated = getClubMembershipById(id);

		if (clubMembershipToBeUpdated != null) {
			em.refresh(clubMembershipToBeUpdated);
			em.merge(clubMembershipWithUpdates);
			em.flush();
		}

		return clubMembershipToBeUpdated;
	}

}