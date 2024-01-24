package com.zooting.api.domain.background.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBackground is a Querydsl query type for Background
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBackground extends EntityPathBase<Background> {

    private static final long serialVersionUID = 1937796604L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBackground background = new QBackground("background");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.zooting.api.domain.file.entity.QFile file;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBackground(String variable) {
        this(Background.class, forVariable(variable), INITS);
    }

    public QBackground(Path<? extends Background> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBackground(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBackground(PathMetadata metadata, PathInits inits) {
        this(Background.class, metadata, inits);
    }

    public QBackground(Class<? extends Background> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.file = inits.isInitialized("file") ? new com.zooting.api.domain.file.entity.QFile(forProperty("file"), inits.get("file")) : null;
    }

}

