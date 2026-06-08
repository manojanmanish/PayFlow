package com.assignment.payflow.Repository;

import com.assignment.payflow.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUpiId(String upiId);
    @Query("SELECT u FROM Users u WHERE (:userId IS NULL OR u.userId = :userId) AND (:upiId IS NULL OR u.upiId = :upiId)")
    Optional<Users> findByFilter(Long userId, String upiId);

    @Query("SELECT u FROM Users u WHERE u.balance > :amount")
    List<Users> findUsersWithBalanceGreaterThan(double amount);
}
