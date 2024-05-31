package com.charly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.charly.entity.OperacionEntity;

@Repository
public interface OperacionRepository extends JpaRepository<OperacionEntity, Long> {
   
}
