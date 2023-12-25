package com.erp.hrms.api.service;

import java.util.List;

import com.erp.hrms.entity.Level;

public interface ILevelService {

	public void saveLevel(Level level);

	public Level findByLevelId(Long lid);

	public List<Level> findAllLevel();

	public void saveInitialLevels();

}
