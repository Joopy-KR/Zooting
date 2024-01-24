package com.zooting.api.domain.friend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendRequest is a Querydsl query type for FriendRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendRequest extends EntityPathBase<FriendRequest> {

    private static final long serialVersionUID = -377002605L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendRequest friendRequest = new QFriendRequest("friendRequest");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.zooting.api.domain.member.entity.QMember from;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zooting.api.domain.member.entity.QMember to;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFriendRequest(String variable) {
        this(FriendRequest.class, forVariable(variable), INITS);
    }

    public QFriendRequest(Path<? extends FriendRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendRequest(PathMetadata metadata, PathInits inits) {
        this(FriendRequest.class, metadata, inits);
    }

    public QFriendRequest(Class<? extends FriendRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.from = inits.isInitialized("from") ? new com.zooting.api.domain.member.entity.QMember(forProperty("from"), inits.get("from")) : null;
        this.to = inits.isInitialized("to") ? new com.zooting.api.domain.member.entity.QMember(forProperty("to"), inits.get("to")) : null;
    }

}

