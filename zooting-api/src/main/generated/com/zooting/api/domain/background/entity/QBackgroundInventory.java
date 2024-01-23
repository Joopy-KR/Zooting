package com.zooting.api.domain.background.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBackgroundInventory is a Querydsl query type for BackgroundInventory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBackgroundInventory extends EntityPathBase<BackgroundInventory> {

    private static final long serialVersionUID = 955762368L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBackgroundInventory backgroundInventory = new QBackgroundInventory("backgroundInventory");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    public final QBackground background;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zooting.api.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBackgroundInventory(String variable) {
        this(BackgroundInventory.class, forVariable(variable), INITS);
    }

    public QBackgroundInventory(Path<? extends BackgroundInventory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBackgroundInventory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBackgroundInventory(PathMetadata metadata, PathInits inits) {
        this(BackgroundInventory.class, metadata, inits);
    }

    public QBackgroundInventory(Class<? extends BackgroundInventory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.background = inits.isInitialized("background") ? new QBackground(forProperty("background"), inits.get("background")) : null;
        this.member = inits.isInitialized("member") ? new com.zooting.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

