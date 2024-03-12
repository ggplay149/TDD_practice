package com.tdd.practice.membership;

import com.tdd.practice.membership.DTO.MembershipAddResponse;
import com.tdd.practice.membership.DTO.MembershipDetailResponse;
import com.tdd.practice.membership.DTO.MembershipRequest;
import com.tdd.practice.membership.DTO.MembershipResponse;
import com.tdd.practice.membership.Entity.Membership;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tdd.practice.membership.Constants.MembershipConstants.USER_ID_HEADER;


@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMeembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest){
        final MembershipAddResponse membershipAddResponse = membershipService.addMembership(userId,membershipRequest.getMembershipType(),membershipRequest.getPoint());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipAddResponse);

    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> getMebershipList(
            @RequestHeader(USER_ID_HEADER) final String userId){
        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }
}
