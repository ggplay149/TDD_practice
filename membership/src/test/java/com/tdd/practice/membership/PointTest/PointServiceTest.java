package com.tdd.practice.membership.PointTest;

import com.tdd.practice.membership.Service.RatePointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {
    @InjectMocks
    RatePointService pointService;

    @Test
    @DisplayName("_10000원의적립은100원")
    public void test10000_100(){
        //given
        final int price = 10000;
        //when
        final int result = pointService.caculatePoint(price);
        //then
        assertThat(result).isEqualTo(100);
    }
}
