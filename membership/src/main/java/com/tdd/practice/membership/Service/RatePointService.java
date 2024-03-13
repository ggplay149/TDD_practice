package com.tdd.practice.membership.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatePointService implements PointService{

    private static final int POINT_RATE = 1;

    @Override
    public int caculatePoint(int price) {
        return price * POINT_RATE/100;
    }
}
