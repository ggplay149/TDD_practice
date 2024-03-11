package com.tdd.practice.membership;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership,Long> {
    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);
}
