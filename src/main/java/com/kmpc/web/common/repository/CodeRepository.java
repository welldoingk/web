package com.kmpc.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmpc.web.common.entity.Code;
import com.kmpc.web.common.entity.CodeId;
import java.util.List;


public interface CodeRepository extends JpaRepository<Code, CodeId> {

    List<Code> findByClassCode(String classCode);
    
}