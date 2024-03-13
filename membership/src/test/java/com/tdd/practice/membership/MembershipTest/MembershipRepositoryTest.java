package com.tdd.practice.membership.MembershipTest;
import com.tdd.practice.membership.Entity.Membership;
import com.tdd.practice.membership.Enums.MembershipType;
import com.tdd.practice.membership.Repository.MembershipRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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

    @Test
    @DisplayName("멤버십조회_사이즈가 0")
    public void search_size_zero(){
        //given
        //when
        List<Membership> result = membershipRepository.findAllByUserId("userId");
        //then
        assertThat(result.size()).isEqualTo(0);
    }
    @Test
    @DisplayName("멤버십조회_사이즈가 2")
    public void search_size_two(){
        //given
        final Membership naverMembership = Membership.builder()
                .userId("userId").point(10000).membershipType(MembershipType.NAVER).build();
        final Membership kakaoMembership = Membership.builder()
                .userId("userId").point(10000).membershipType(MembershipType.KAKAO).build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);

        //when
        List<Membership> result = membershipRepository.findAllByUserId("userId");
        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("멤버십추가후삭제")
    public void join_And_Delete(){
        //given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership savedMembership = membershipRepository.save(naverMembership);

        //when
        membershipRepository.deleteById(savedMembership.getId());

        //then
    }


}
