package com.tdd.practice.membership.Repository;

import com.tdd.practice.membership.Entity.Membership;
import com.tdd.practice.membership.Enums.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership,Long> {

    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);

    List<Membership> findAllByUserId(String userId);

}
