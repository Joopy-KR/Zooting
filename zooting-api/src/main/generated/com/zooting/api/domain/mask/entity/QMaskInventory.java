package com.zooting.api.domain.mask.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMaskInventory is a Querydsl query type for MaskInventory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMaskInventory extends EntityPathBase<MaskInventory> {

    private static final long serialVersionUID = 552579844L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMaskInventory maskInventory = new QMaskInventory("maskInventory");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMask mask;

    public final com.zooting.api.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMaskInventory(String variable) {
        this(MaskInventory.class, forVariable(variable), INITS);
    }

    public QMaskInventory(Path<? extends MaskInventory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMaskInventory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMaskInventory(PathMetadata metadata, PathInits inits) {
        this(MaskInventory.class, metadata, inits);
    }

    public QMaskInventory(Class<? extends MaskInventory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mask = inits.isInitialized("mask") ? new QMask(forProperty("mask"), inits.get("mask")) : null;
        this.member = inits.isInitialized("member") ? new com.zooting.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

