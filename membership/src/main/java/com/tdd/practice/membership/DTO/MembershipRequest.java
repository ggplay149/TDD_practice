package com.tdd.practice.membership.DTO;

import com.tdd.practice.membership.Enums.MembershipType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static com.tdd.practice.membership.ValidationGroups.*;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {

    @NotNull(groups = {MembershipAddMarker.class, MembershipAccumulateMarker.class})
    @Min(value = 0, groups = {MembershipAddMarker.class, MembershipAccumulateMarker.class})
    private final Integer point;

    @NotNull(groups = {MembershipAddMarker.class})
    private final MembershipType membershipType;

}
