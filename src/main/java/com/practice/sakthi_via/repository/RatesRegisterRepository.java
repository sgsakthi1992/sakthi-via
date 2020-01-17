package com.practice.sakthi_via.repository;

import com.practice.sakthi_via.model.RatesRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesRegisterRepository extends
        JpaRepository<RatesRegister, Integer> {

}
