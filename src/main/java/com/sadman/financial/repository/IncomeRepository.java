package com.sadman.financial.repository;

import com.sadman.financial.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {}

