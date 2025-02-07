package com.sadman.financial.repository;

import com.sadman.financial.entity.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query(value = "SELECT i FROM Income i WHERE (:search IS NULL OR :search = '' OR LOWER(i.source) LIKE LOWER(CONCAT('%', :search, '%'))) AND i.user.id = :userId ORDER BY i.date",
            countQuery = "SELECT COUNT(i) FROM Income i WHERE (:search IS NULL OR :search = '' OR LOWER(i.source) LIKE LOWER(CONCAT('%', :search, '%'))) AND i.user.id = :userId")
    Page<Income> search(String search, Long userId, Pageable pageable);


    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId")
    double findTotalIncomeByUser(Long userId);

}

