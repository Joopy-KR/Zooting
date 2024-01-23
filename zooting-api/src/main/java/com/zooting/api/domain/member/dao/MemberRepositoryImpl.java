package com.zooting.api.domain.member.dao;


import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zooting.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import static com.zooting.api.domain.block.entity.QBlock.block;
import static com.zooting.api.domain.member.entity.QMember.member;


@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Member> extractMatchingMember(String userId, Date userBirth) {
//        OrderSpecifier[] orderSpecifiers = createSpecifier();
        List<Member> notBlockAndNotFriend = queryFactory
                .selectFrom(member)
                .where(
                        // 차단 목록의 친구는 매칭되지 않는다
                        member.notIn(member.blockFromList.any().from),
                        member.notIn(member.blockToList.any().to),
                        // 이미 친구라면 매칭이 되지 않는다
                        member.notIn(member.friendList.any().follower)

                ).fetch();
        return notBlockAndNotFriend;
    }
//    private OrderSpecifier[] createOrderSpecifierByInterests(List<String> memberInterests) {
//        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
//        orderSpecifiers.add(new OrderSpecifier(Order.DESC, member.additionalInfo.interest.in(memberInterests).count())); // todo:수정해야함
//    }




    // 위도 경도 기준 근처
    // 생년 월일 범위 2~10




    // 관심사가 일치하는 유저가 먼저 오도록 sort
//    private List<Member> interests(List<String> memberInterests) {
//
//        queryFactory.selectFrom(member)
//                .where(
//                        member.additionalInfo.interest
//                )
//    }
    // 이상형이 일치하는 유저가 먼저 오도록 sort




}
