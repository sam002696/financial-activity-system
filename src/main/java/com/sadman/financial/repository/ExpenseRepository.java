package com.sadman.financial.repository;

import com.sadman.financial.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(value = "SELECT e FROM Expense e WHERE (:search IS NULL OR :search = '' OR LOWER(e.category) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY e.date",
            countQuery = "SELECT COUNT(e) FROM Expense e WHERE (:search IS NULL OR :search = '' OR LOWER(e.category) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    Page<Expense> search(String search, Pageable pageable);

}
