package com.erp.hrms.joblevelandDesignationREPO;

import javax.websocket.server.ServerEndpoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.SubDuties;

@Service
public interface SubDutiesRepo extends JpaRepository<SubDuties, Integer>{

}
