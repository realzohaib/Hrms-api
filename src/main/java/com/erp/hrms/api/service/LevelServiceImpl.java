package com.erp.hrms.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.LevelRepository;
import com.erp.hrms.entity.Designation;
import com.erp.hrms.entity.Level;

@Service
public class LevelServiceImpl implements ILevelService {

	@Autowired
	private LevelRepository levelRepository;
	
	@Override
	public void saveInitialLevels() {
			List<Level> list = new ArrayList<>();
//			initialLevels.add(new Level("level 1"));
//			initialLevels.add(new Level("level 2"));
//			initialLevels.add(new Level("level 3"));
//			initialLevels.add(new Level("level 4"));
//			initialLevels.add(new Level("level 5"));
//			initialLevels.add(new Level("level 6"));
//			initialLevels.add(new Level("level 7"));
//			initialLevels.add(new Level("level 8"));
			//levelRepository.saveAll(initialLevels);

		// Create Level objects first
		Level level1 = new Level("Level 1");
		Level level2 = new Level("Level 2");

		// Then create Designation objects, associating them with the existing Level objects
		List<Designation> designations1 = Arrays.asList(
		    new Designation("Designation A", level1),
		    new Designation("Designation B", level1)
		);
		level1.setDesignations(designations1); // Set the designations for level1

		Designation designationC = new Designation("Designation C", level2);
		level2.setDesignations(Collections.singletonList(designationC)); // Set the designation for level2

		list.add(level1);
		list.add(level2);
		
		levelRepository.saveAll(list);
	}

	@Transactional
	@Override
	public void saveLevel(Level level) {
		try {
			levelRepository.save(level);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}

	@Override
	public Level findByLevelId(Long lid) {

		return levelRepository.findBylId(lid);
	}

	@Override
	public List<Level> findAllLevel() {

		return levelRepository.findAll();
	}

}
