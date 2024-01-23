package com.zooting.api.domain.animalface.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnimalFace is a Querydsl query type for AnimalFace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnimalFace extends EntityPathBase<AnimalFace> {

    private static final long serialVersionUID = 1589735346L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnimalFace animalFace = new QAnimalFace("animalFace");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    public final NumberPath<Long> animal1 = createNumber("animal1", Long.class);

    public final NumberPath<Long> animal2 = createNumber("animal2", Long.class);

    public final NumberPath<Long> animal3 = createNumber("animal3", Long.class);

    public final NumberPath<Long> animal4 = createNumber("animal4", Long.class);

    public final NumberPath<Long> animal5 = createNumber("animal5", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zooting.api.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAnimalFace(String variable) {
        this(AnimalFace.class, forVariable(variable), INITS);
    }

    public QAnimalFace(Path<? extends AnimalFace> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnimalFace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnimalFace(PathMetadata metadata, PathInits inits) {
        this(AnimalFace.class, metadata, inits);
    }

    public QAnimalFace(Class<? extends AnimalFace> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zooting.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

