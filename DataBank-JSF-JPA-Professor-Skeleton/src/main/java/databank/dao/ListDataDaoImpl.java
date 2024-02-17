
package databank.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

import databank.ejb.ListDataService;

/**
 * Description:  API for reading list data from the database
 */
@SessionScoped
public class ListDataDaoImpl implements ListDataDao, Serializable {
	
	/** Explicitly set serialVersionUID */
	private static final long serialVersionUID = 8856045561596204709L;

	@EJB
	protected ListDataService listDataService;

	@Override
	public List<String> readAllDegrees() {
		return listDataService.findAllDegrees();
	}

	@Override
	public List<String> readAllMajors() {
		return listDataService.findAllMajors();
	}
	
}
