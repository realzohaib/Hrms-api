package com.erp.hrms.employeedesignationandtask.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.hrms.employeedesignationandtask.entity.CurrentDesignationAndTask;

@Repository
public interface ICurrentDsAndTskRepo extends JpaRepository<CurrentDesignationAndTask, Integer> {

	List<CurrentDesignationAndTask> findByEmpId(Long empId);

	@Query("SELECT cdt FROM CurrentDesignationAndTask cdt WHERE cdt.currentDsAndTskId = :currentDsAndTskId")
	public CurrentDesignationAndTask findByCurrentDsAndTskId(Integer currentDsAndTskId);

	List<CurrentDesignationAndTask> findByLevelId(Integer levelId);

//    @Query("SELECT cdt FROM CurrentDesignationAndTask cdt JOIN cdt.task t WHERE t.taskId = :taskId")
//    List<CurrentDesignationAndTask> findByTaskId(Integer taskId);

	@Query("SELECT cdt FROM CurrentDesignationAndTask cdt JOIN cdt.task t WHERE t.taskId = :taskId")
	List<CurrentDesignationAndTask> findByTaskIdInJoinTable(@Param("taskId") Integer taskId);

	

}

