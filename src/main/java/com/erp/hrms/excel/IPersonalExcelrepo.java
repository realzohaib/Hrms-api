package com.erp.hrms.excel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.PersonalInfo;

@Repository
public interface IPersonalExcelrepo extends JpaRepository<PersonalInfo, Long> {

}
