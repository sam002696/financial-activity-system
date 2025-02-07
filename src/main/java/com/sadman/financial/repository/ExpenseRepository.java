package com.sadman.financial.repository;

import com.sadman.financial.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(value = "SELECT e FROM Expense e WHERE (:search IS NULL OR :search = '' OR LOWER(e.category) " +
            "LIKE LOWER(CONCAT('%', :search, '%'))) AND e.user.id = :userId ORDER BY e.date",
            countQuery = "SELECT COUNT(e) FROM Expense e WHERE (:search IS NULL OR :search = '' OR LOWER(e.category) " +
                    "LIKE LOWER(CONCAT('%', :search, '%'))) AND e.user.id = :userId")
    Page<Expense> search(String search, Long userId, Pageable pageable);



    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    double findTotalExpenseByUser(Long userId);

}
