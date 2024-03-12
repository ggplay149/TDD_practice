package com.tdd.practice.membership;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //개발자가 동작을 직접 제어할수있는 가짜 객체 지원 테스트 프레임워크
public class MembershipServiceTest {

    @InjectMocks //테스트 대상으로 의존성이 주입
    private MembershipService target;

    @Mock // 이미 의존성이 있는 클래스이므로 가짜 객체을 만듬
    private MembershipRepository membershipRepository;

    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;
    private final Long membershipId = -1L;

    @Test
    @DisplayName("멤버쉽등록 실패_중복")
    public void MembershipJoinFail(){

        //doReturn : 가짜객체가 반환해줄 값을 지정할수 있다.

        //given
        doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId,membershipType);

        //when
        final MembershipException result = assertThrows(MembershipException.class,()-> target.addMembership(userId,membershipType,point));

        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    @DisplayName("멤버쉽등록 성공")
    public void MembershipJoinSuccess(){
        //given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId,membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        //when
        final MembershipResponse result = target.addMembership(userId,membershipType,point) ;

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        //verify
        verify(membershipRepository,times(1)).findByUserIdAndMembershipType(userId,membershipType);
        verify(membershipRepository,times(1)).save(any(Membership.class));
    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(membershipType.NAVER)
                .build();
    }
}
