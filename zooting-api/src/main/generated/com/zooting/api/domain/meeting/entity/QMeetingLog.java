package com.zooting.api.domain.meeting.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetingLog is a Querydsl query type for MeetingLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetingLog extends EntityPathBase<MeetingLog> {

    private static final long serialVersionUID = -1542876802L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetingLog meetingLog = new QMeetingLog("meetingLog");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMeetingParticipants meetingParticipants;

    public final com.zooting.api.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMeetingLog(String variable) {
        this(MeetingLog.class, forVariable(variable), INITS);
    }

    public QMeetingLog(Path<? extends MeetingLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetingLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetingLog(PathMetadata metadata, PathInits inits) {
        this(MeetingLog.class, metadata, inits);
    }

    public QMeetingLog(Class<? extends MeetingLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meetingParticipants = inits.isInitialized("meetingParticipants") ? new QMeetingParticipants(forProperty("meetingParticipants"), inits.get("meetingParticipants")) : null;
        this.member = inits.isInitialized("member") ? new com.zooting.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

