package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {
    User findByEmail(String email);
    User findByUserName(String userName);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String verificationCode);

    @Query("SELECT u FROM User u WHERE u.approvalCode = ?1")
    public User findByApprovalCode(String approvalCode);
}