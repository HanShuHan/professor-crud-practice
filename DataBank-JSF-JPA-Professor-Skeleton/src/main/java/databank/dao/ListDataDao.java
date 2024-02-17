
package databank.dao;

import java.util.List;


/**
 * Description:  API for reading list data from the database
 */
public interface ListDataDao {

	public List<String> readAllDegrees();

	public List<String> readAllMajors();

}