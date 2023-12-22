/**
 * 
 */
package com.erp.hrms.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.api.security.entity.JobLevel;

/**
 * @author TA Admin
 *
 * 
 */
@Repository
public interface IjobLevelRepo extends JpaRepository<JobLevel, Integer>{
	
	 JobLevel findByJoblevel(String name);

}
