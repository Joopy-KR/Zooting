package com.zooting.api.domain.dm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDMRoom is a Querydsl query type for DMRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDMRoom extends EntityPathBase<DMRoom> {

    private static final long serialVersionUID = -840350771L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDMRoom dMRoom = new QDMRoom("dMRoom");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<DM, QDM> dms = this.<DM, QDM>createList("dms", DM.class, QDM.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zooting.api.domain.member.entity.QMember receiver;

    public final NumberPath<Long> receiverLastReadId = createNumber("receiverLastReadId", Long.class);

    public final com.zooting.api.domain.member.entity.QMember sender;

    public final NumberPath<Long> senderLastReadId = createNumber("senderLastReadId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDMRoom(String variable) {
        this(DMRoom.class, forVariable(variable), INITS);
    }

    public QDMRoom(Path<? extends DMRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDMRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDMRoom(PathMetadata metadata, PathInits inits) {
        this(DMRoom.class, metadata, inits);
    }

    public QDMRoom(Class<? extends DMRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new com.zooting.api.domain.member.entity.QMember(forProperty("receiver"), inits.get("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new com.zooting.api.domain.member.entity.QMember(forProperty("sender"), inits.get("sender")) : null;
    }

}

