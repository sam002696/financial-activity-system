package com.sadman.financial.repository;

import com.sadman.financial.entity.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query(value = "SELECT i FROM Income i WHERE (:search IS NULL OR :search = '' OR LOWER(i.source) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY i.date",
            countQuery = "SELECT COUNT(i) FROM Income i WHERE (:search IS NULL OR :search = '' OR LOWER(i.source) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    Page<Income> search(String search, Pageable pageable);

}

