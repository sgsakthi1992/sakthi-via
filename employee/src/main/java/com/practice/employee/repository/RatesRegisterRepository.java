package com.practice.employee.repository;

import com.practice.employee.model.RatesRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesRegisterRepository extends
    JpaRepository<RatesRegister, Integer> {

}
