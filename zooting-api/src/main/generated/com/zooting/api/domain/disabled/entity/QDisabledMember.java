package com.zooting.api.domain.disabled.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDisabledMember is a Querydsl query type for DisabledMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDisabledMember extends EntityPathBase<DisabledMember> {

    private static final long serialVersionUID = 956572050L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDisabledMember disabledMember = new QDisabledMember("disabledMember");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath endDate = createString("endDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zooting.api.domain.member.entity.QMember member;

    public final StringPath startDate = createString("startDate");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDisabledMember(String variable) {
        this(DisabledMember.class, forVariable(variable), INITS);
    }

    public QDisabledMember(Path<? extends DisabledMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDisabledMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDisabledMember(PathMetadata metadata, PathInits inits) {
        this(DisabledMember.class, metadata, inits);
    }

    public QDisabledMember(Class<? extends DisabledMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zooting.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

