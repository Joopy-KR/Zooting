package com.zooting.api.domain.mask.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMask is a Querydsl query type for Mask
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMask extends EntityPathBase<Mask> {

    private static final long serialVersionUID = 1859446072L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMask mask = new QMask("mask");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    public final StringPath animal = createString("animal");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final com.zooting.api.domain.file.entity.QFile file;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMask(String variable) {
        this(Mask.class, forVariable(variable), INITS);
    }

    public QMask(Path<? extends Mask> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMask(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMask(PathMetadata metadata, PathInits inits) {
        this(Mask.class, metadata, inits);
    }

    public QMask(Class<? extends Mask> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.file = inits.isInitialized("file") ? new com.zooting.api.domain.file.entity.QFile(forProperty("file"), inits.get("file")) : null;
    }

}

