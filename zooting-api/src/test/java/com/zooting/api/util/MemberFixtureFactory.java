package com.zooting.api.util;

import com.zooting.api.domain.member.dto.Gender;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.jeasy.random.randomizers.time.DateRandomizer;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;


public class MemberFixtureFactory {


    public static EasyRandomParameters getMemberParams() throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse("1960-01-01");
        Date end = sdf.parse("2023-01-01");
        var parameter = new EasyRandomParameters()
                .stringLengthRange(5, 10)
                .randomize(named("gender").and(ofType(Gender.class)), new StringRandomizer())
                .randomize(named("birth").and(ofType(Date.class)), new DateRandomizer());
        return parameter;

    }
}
