package com.tdd.practice.membership;

import com.tdd.practice.membership.DTO.MembershipAddResponse;
import com.tdd.practice.membership.DTO.MembershipDetailResponse;
import com.tdd.practice.membership.DTO.MembershipResponse;
import com.tdd.practice.membership.Entity.Membership;
import com.tdd.practice.membership.Enums.MembershipType;
import com.tdd.practice.membership.Exception.MembershipErrorResult;
import com.tdd.practice.membership.Exception.MembershipException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point){
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId,membershipType);
        if(result != null){
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }
        final Membership membership = Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();

        final Membership savedMembership =  membershipRepository.save(membership);

        return MembershipAddResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }

    public List<MembershipDetailResponse> getMembershipList(String userId) {
        final List<Membership> membershipList = membershipRepository.findAllByUserId(userId);
        return membershipList.stream()
                .map(v -> MembershipDetailResponse.builder()
                .id(v.getId())
                .membershipType(v.getMembershipType())
                .point(v.getPoint())
                .createdAt(v.getCreatedAt())
                .build())
        .collect(Collectors.toList());

    }
}
