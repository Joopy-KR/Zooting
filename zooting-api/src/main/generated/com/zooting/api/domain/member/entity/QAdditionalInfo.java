package com.zooting.api.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdditionalInfo is a Querydsl query type for AdditionalInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdditionalInfo extends EntityPathBase<AdditionalInfo> {

    private static final long serialVersionUID = 2112257327L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdditionalInfo additionalInfo = new QAdditionalInfo("additionalInfo");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    public final StringPath animal = createString("animal");

    public final NumberPath<Long> backgroundId = createNumber("backgroundId", Long.class);

    public final StringPath backgroundUrl = createString("backgroundUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath idealAnimal = createString("idealAnimal");

    public final StringPath interest = createString("interest");

    public final StringPath introduce = createString("introduce");

    public final NumberPath<Long> maskId = createNumber("maskId", Long.class);

    public final StringPath maskUrl = createString("maskUrl");

    public final QMember member;

    public final StringPath personality = createString("personality");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAdditionalInfo(String variable) {
        this(AdditionalInfo.class, forVariable(variable), INITS);
    }

    public QAdditionalInfo(Path<? extends AdditionalInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdditionalInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdditionalInfo(PathMetadata metadata, PathInits inits) {
        this(AdditionalInfo.class, metadata, inits);
    }

    public QAdditionalInfo(Class<? extends AdditionalInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

