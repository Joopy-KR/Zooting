package com.zooting.api.domain.block.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlock is a Querydsl query type for Block
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlock extends EntityPathBase<Block> {

    private static final long serialVersionUID = -271347546L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlock block = new QBlock("block");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.zooting.api.domain.member.entity.QMember from;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zooting.api.domain.member.entity.QMember to;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBlock(String variable) {
        this(Block.class, forVariable(variable), INITS);
    }

    public QBlock(Path<? extends Block> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlock(PathMetadata metadata, PathInits inits) {
        this(Block.class, metadata, inits);
    }

    public QBlock(Class<? extends Block> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.from = inits.isInitialized("from") ? new com.zooting.api.domain.member.entity.QMember(forProperty("from"), inits.get("from")) : null;
        this.to = inits.isInitialized("to") ? new com.zooting.api.domain.member.entity.QMember(forProperty("to"), inits.get("to")) : null;
    }

}

