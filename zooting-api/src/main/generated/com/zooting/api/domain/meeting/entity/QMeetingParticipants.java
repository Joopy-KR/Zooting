package com.zooting.api.domain.meeting.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetingParticipants is a Querydsl query type for MeetingParticipants
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetingParticipants extends EntityPathBase<MeetingParticipants> {

    private static final long serialVersionUID = 497511078L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetingParticipants meetingParticipants = new QMeetingParticipants("meetingParticipants");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMeetingLog meetingId;

    public final com.zooting.api.domain.member.entity.QMember participant1;

    public final com.zooting.api.domain.member.entity.QMember participant2;

    public final com.zooting.api.domain.member.entity.QMember participant3;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMeetingParticipants(String variable) {
        this(MeetingParticipants.class, forVariable(variable), INITS);
    }

    public QMeetingParticipants(Path<? extends MeetingParticipants> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetingParticipants(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetingParticipants(PathMetadata metadata, PathInits inits) {
        this(MeetingParticipants.class, metadata, inits);
    }

    public QMeetingParticipants(Class<? extends MeetingParticipants> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meetingId = inits.isInitialized("meetingId") ? new QMeetingLog(forProperty("meetingId"), inits.get("meetingId")) : null;
        this.participant1 = inits.isInitialized("participant1") ? new com.zooting.api.domain.member.entity.QMember(forProperty("participant1"), inits.get("participant1")) : null;
        this.participant2 = inits.isInitialized("participant2") ? new com.zooting.api.domain.member.entity.QMember(forProperty("participant2"), inits.get("participant2")) : null;
        this.participant3 = inits.isInitialized("participant3") ? new com.zooting.api.domain.member.entity.QMember(forProperty("participant3"), inits.get("participant3")) : null;
    }

}

