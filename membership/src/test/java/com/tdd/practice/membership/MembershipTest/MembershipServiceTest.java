package com.tdd.practice.membership.MembershipTest;

import com.tdd.practice.membership.DTO.MembershipAddResponse;
import com.tdd.practice.membership.DTO.MembershipDetailResponse;
import com.tdd.practice.membership.Entity.Membership;
import com.tdd.practice.membership.Enums.MembershipType;
import com.tdd.practice.membership.Enums.MembershipErrorResult;
import com.tdd.practice.membership.Exception.MembershipException;
import com.tdd.practice.membership.Repository.MembershipRepository;
import com.tdd.practice.membership.Service.MembershipService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(membershipType.NAVER)
                .build();
    }

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
        final MembershipAddResponse result = target.addMembership(userId,membershipType,point) ;

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        //verify
        verify(membershipRepository,times(1)).findByUserIdAndMembershipType(userId,membershipType);
        verify(membershipRepository,times(1)).save(any(Membership.class));
    }



    @Test
    @DisplayName("멤버십목록조회")
    public void searh_List(){

        //given
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipRepository).findAllByUserId(userId);

        //when
        final List<MembershipDetailResponse> result = target.getMembershipList(userId);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("멤버십상세조회실패_존재하지않음")
    public void searchDeatil_No_member(){
        //given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
        //when
        final MembershipException result = assertThrows(MembershipException.class,()->
                target.getMembership(membershipId,userId));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }
    @Test
    @DisplayName("멤버십상세조회실패_본인이아님")
    public void searchDeatil_Not_vailed(){
        //given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
        //when
        final MembershipException result = assertThrows(MembershipException.class,()->
                target.getMembership(membershipId,"notOwner"));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }
    @Test
    @DisplayName("멤버십상세조회 성공")
    public void searchDeatil_Success(){
        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipDetailResponse result = target.getMembership(membershipId, userId);

        // then
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(point);
    }

//    @Test
//    @DisplayName("멤버십상세조회실패_존재하지않음")
//    public void searchDeatil_No_member(){
//        //given
//        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
//        //when
//        final MembershipException result = assertThrows(MembershipException.class,()->
//                target.getMembership(membershipId,userId));
//        //then
//        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
//    }

    @Test
    @DisplayName("멤버십삭제실패_존재하지않음")
    public void deleteFail_No_member(){
        //given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
        //when
        final MembershipException result = assertThrows(MembershipException.class,() -> target.deleteMembership(membershipId,userId));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("멤버십삭제실패_본인이아님")
    public void deleteFail_Not_valid(){
        //given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
        //when
        final MembershipException result = assertThrows(MembershipException.class,()-> target.deleteMembership(membershipId,"wrongID"));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    @DisplayName("멤버십삭제성공")
    public void delete_Success(){
        //given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
        //when
        //then
        target.deleteMembership(membershipId,userId);
    }

    @Test
    @DisplayName("멤버십적립실패_존재하지않음")
    public void fail_Point_No_Memeber(){
        //given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
        //when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> target.accumulateMembershipPoint(membershipId,userId,1000));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    /*
     *
     * 유저 정보 업데이트 기능 추가
     *
     */
    @Test
    @DisplayName("업데이트실패_해당유저없음")
    public void fail_Update_No_Memeber(){
        //given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
        final Membership newMembershipInfo = Membership.builder().build();
        //when
        final MembershipException result = assertThrows(MembershipException.class, ()->target.updateMembership(membershipId,userId,newMembershipInfo));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("업데이트실패_권한없음")
    public void fail_Update_Not_valid(){
        //given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
        final Membership newMembershipInfo = Membership.builder().build();
        //when
        final MembershipException result = assertThrows(MembershipException.class, ()->target.updateMembership(membershipId,"wrongId",newMembershipInfo));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);

    }
    @Test
    @DisplayName("업데이트실패_업데이트하려고하는 유저아이디중복")
    public void fail_Update_duplicated(){

        //given
        final Membership newMembershipInfo = Membership.builder()
                .userId("newUserId")
                .build();

        //계정 본인확인용 리턴
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
        //중복검사용 리턴
        doReturn(Membership.builder().build()).when(membershipRepository).findByUserId(newMembershipInfo.getUserId());

        //when
        final MembershipException result = assertThrows(MembershipException.class, ()->target.updateMembership(membershipId,userId,newMembershipInfo));

        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);

    }

    @Test
    @DisplayName("업데이트성공")
    public void fail_Update_success() throws Exception {

        //given
        final Membership newMembershipInfo = Membership.builder()
                .userId("newUserId") // 기존 userId
                .point(77777) //기존
                .membershipType(MembershipType.LINE)
                .build();

        //계정 본인확인용 리턴
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
        //중복검사용 리턴
        doReturn(null).when(membershipRepository).findByUserId(newMembershipInfo.getUserId());

        //when
        target.updateMembership(membershipId,userId,newMembershipInfo);

        //then
        Optional<Membership> updatedMembershipTemp = membershipRepository.findById(membershipId);
        Membership updatedMembership = updatedMembershipTemp.orElseThrow(()-> new Exception());
        assertEquals("newUserId", updatedMembership.getUserId());
        assertEquals(77777, updatedMembership.getPoint());
        assertEquals(MembershipType.LINE, updatedMembership.getMembershipType());

    }
}
