package com.tdd.practice.membership.Controller;

import com.tdd.practice.membership.DTO.MembershipAddResponse;
import com.tdd.practice.membership.DTO.MembershipDetailResponse;
import com.tdd.practice.membership.DTO.MembershipRequest;
import com.tdd.practice.membership.Entity.Membership;
import com.tdd.practice.membership.Service.MembershipService;
import com.tdd.practice.membership.ValidationGroups;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tdd.practice.membership.Constants.MembershipConstants.USER_ID_HEADER;
import static com.tdd.practice.membership.ValidationGroups.*;


@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMeembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Validated(MembershipAddMarker.class) final MembershipRequest membershipRequest){
        final MembershipAddResponse membershipAddResponse = membershipService.addMembership(userId,membershipRequest.getMembershipType(),membershipRequest.getPoint());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipAddResponse);

    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> getMebershipList(
            @RequestHeader(USER_ID_HEADER) final String userId){
        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @DeleteMapping("/api/v1/memberships/delete")
    public ResponseEntity<List<MembershipDetailResponse>> deleteMemebership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id){
        membershipService.deleteMembership(id,userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/memberships/{id}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id,
            @RequestBody @Validated(MembershipAccumulateMarker.class) final MembershipRequest membershipRequest
    ){
        membershipService.accumulateMembershipPoint(id,userId,membershipRequest.getPoint());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/v1/memberships/{id}/update")
    public ResponseEntity<Void> updateMembershipInf(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id,
            @RequestBody @Validated(MembershipUpdateMarker.class) final MembershipRequest membershipRequest
    ){
        Membership new_Membership_Info = Membership.builder()
                        .userId(membershipRequest.getUserId()).build();

        membershipService.updateMembership(id,userId,new_Membership_Info);
        return ResponseEntity.noContent().build();
    }
}
