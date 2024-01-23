package com.zooting.api.domain.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLiarGame is a Querydsl query type for LiarGame
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLiarGame extends EntityPathBase<LiarGame> {

    private static final long serialVersionUID = -1227106094L;

    public static final QLiarGame liarGame = new QLiarGame("liarGame");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath topic = createString("topic");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath word = createString("word");

    public QLiarGame(String variable) {
        super(LiarGame.class, forVariable(variable));
    }

    public QLiarGame(Path<? extends LiarGame> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLiarGame(PathMetadata metadata) {
        super(LiarGame.class, metadata);
    }

}

