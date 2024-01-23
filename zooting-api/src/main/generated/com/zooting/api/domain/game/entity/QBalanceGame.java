package com.zooting.api.domain.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBalanceGame is a Querydsl query type for BalanceGame
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBalanceGame extends EntityPathBase<BalanceGame> {

    private static final long serialVersionUID = -1852393892L;

    public static final QBalanceGame balanceGame = new QBalanceGame("balanceGame");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath sentence1 = createString("sentence1");

    public final StringPath sentence2 = createString("sentence2");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBalanceGame(String variable) {
        super(BalanceGame.class, forVariable(variable));
    }

    public QBalanceGame(Path<? extends BalanceGame> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBalanceGame(PathMetadata metadata) {
        super(BalanceGame.class, metadata);
    }

}

