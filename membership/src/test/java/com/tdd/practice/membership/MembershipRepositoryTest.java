package com.tdd.practice.membership;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.reflect.Member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest //JPA Repository들에 대한 빈들을 등록하여 단위 테스트의 작성을 용이하게 함

public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository ;

//    @Test
//    @DisplayName("MembershipRepository 유무확인")
//    public void MembershipRepositoryIsNotNull(){
//        assertThat(membershipRepository).isNotNull();
//    }

    @Test
    @DisplayName("멤버십 등록")
    public void MembershipJoin(){
        //given
        final Membership testMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();
        //when
        final Membership temp = membershipRepository.save(testMembership);

        //then
        assertThat(temp.getId()).isNotNull();
        assertThat(temp.getUserId()).isEqualTo("userId");
        assertThat(temp.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(temp.getPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("멤버십 존재 확인")
    public void MembershipSearch(){
        //given
        final Membership testMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();
        //when
        final Membership temp = membershipRepository.save(testMembership);
        final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId",MembershipType.NAVER);

        //then
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(findResult.getPoint()).isEqualTo(10000);

    }
}
