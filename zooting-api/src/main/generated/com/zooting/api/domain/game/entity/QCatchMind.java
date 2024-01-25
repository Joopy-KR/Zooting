package com.zooting.api.domain.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCatchMind is a Querydsl query type for CatchMind
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCatchMind extends EntityPathBase<CatchMind> {

    private static final long serialVersionUID = -2072802661L;

    public static final QCatchMind catchMind = new QCatchMind("catchMind");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath word = createString("word");

    public QCatchMind(String variable) {
        super(CatchMind.class, forVariable(variable));
    }

    public QCatchMind(Path<? extends CatchMind> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCatchMind(PathMetadata metadata) {
        super(CatchMind.class, metadata);
    }

}

