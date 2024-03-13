package com.tdd.practice.membership;

import com.google.gson.Gson;
import com.tdd.practice.membership.Controller.MembershipController;
import com.tdd.practice.membership.DTO.MembershipAddResponse;
import com.tdd.practice.membership.DTO.MembershipDetailResponse;
import com.tdd.practice.membership.Exception.GlobalExceptionHandler;
import com.tdd.practice.membership.DTO.MembershipRequest;
import com.tdd.practice.membership.DTO.MembershipResponse;
import com.tdd.practice.membership.Enums.MembershipType;
import com.tdd.practice.membership.Exception.MembershipErrorResult;
import com.tdd.practice.membership.Exception.MembershipException;
import com.tdd.practice.membership.Service.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.tdd.practice.membership.Constants.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {



    @InjectMocks
    private MembershipController target;

    private MockMvc mockMvc;
    private Gson gson;

    @Mock
    private MembershipService membershipService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        gson = new Gson();
    }


    @Test
    public void mockMvcIsNotNull() throws Exception{
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("멤버등록실패_사용자식별값이헤더에없음")
    public void joinFail_No_Header()throws Exception{

        //given
        final String url = "/api/v1/memberships";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }
    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }
    @ParameterizedTest
    @DisplayName("멤버십등록실패_잘못된파라미터")
    @MethodSource("invalidMembershipAddParameter")
    public void joinFail_Wrong_Param(final Integer point, final MembershipType membershipType) throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(point, membershipType)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMembershipAddParameter(){
        return Stream.of(
                Arguments.of(null,MembershipType.NAVER),
                Arguments.of(-1,MembershipType.NAVER),
                Arguments.of(10000,null)
        );
    }

    @Test
    @DisplayName("멤버쉽등록실패_포인트가null")
    public void joinFail_Point_Null() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("멤버쉽등록실패_포인트가음수")
    public void joinFail_Point_Negative() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("멤버쉽등록실패_타입이null")
    public void joinFail_type_Null() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십등록실패_MemberService에서 에러 Throw()")
    public void joinFail_service_trow() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("12345",MembershipType.NAVER,10000);
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER,"12345")
                        .content(gson.toJson(membershipRequest(10000,MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("멤버십등록성공")
    public void joinSuccess() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        final MembershipAddResponse membershipAddResponse = MembershipAddResponse.builder()
                .id(-1L).membershipType(MembershipType.NAVER)
                .build();

        doReturn(membershipAddResponse).when(membershipService).addMembership("12345",MembershipType.NAVER,10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER,"12345")
                        .content(gson.toJson(membershipRequest(10000,MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());
        final MembershipResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8),MembershipResponse.class);

        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(response.getId()).isNotNull();
    }

    @Test
    @DisplayName("멤버십목록조회실패_사용자식별값이헤더에없음")
    public void search_Fail_No_Header() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십목록조회성공")
    public void search_Success_Wtih_Header() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        doReturn(Arrays.asList(
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build()
        )).when(membershipService).getMembershipList("12345");

        //when
            final ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(url)
                            .header(USER_ID_HEADER,"12345")
            );
        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("멤버십삭제실패_사용자식별값이헤더에없음")
    public void delete_Fail_no_valid() throws Exception{
        //given
        final String url = "/api/v1/memberships/delete";
        //when
        final ResultActions resultActions = mockMvc.perform(
          MockMvcRequestBuilders.delete(url)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }
}
