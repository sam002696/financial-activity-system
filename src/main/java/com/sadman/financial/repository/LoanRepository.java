package com.sadman.financial.repository;

import com.sadman.financial.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "SELECT l FROM Loan l WHERE " +
            "(:search IS NULL OR :search = '' OR LOWER(l.status) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR CAST(l.amount AS string) LIKE LOWER(CONCAT('%', :search, '%')) " +  // Convert amount to String for searching
            "OR LOWER(CAST(l.dueDate AS string)) LIKE LOWER(CONCAT('%', :search, '%'))) " +  // Convert date to string for searching
            "ORDER BY l.dueDate",
            countQuery = "SELECT COUNT(l) FROM Loan l WHERE " +
                    "(:search IS NULL OR :search = '' OR LOWER(l.status) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR CAST(l.amount AS string) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(CAST(l.dueDate AS string)) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    Page<Loan> search(String search, Pageable pageable);


    @Query("SELECT SUM(l.amount) FROM Loan l WHERE l.user.id = :userId")
    double findTotalLoanByUser(Long userId);

    @Query("SELECT SUM(l.remainingAmount) FROM Loan l WHERE l.user.id = :userId")
    double findRemainingLoanByUser(Long userId);

}
