package com.zooting.api.domain.dm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDM is a Querydsl query type for DM
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDM extends EntityPathBase<DM> {

    private static final long serialVersionUID = 1791920082L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDM dM = new QDM("dM");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDMRoom dmRoom;

    public final ListPath<com.zooting.api.domain.file.entity.File, com.zooting.api.domain.file.entity.QFile> files = this.<com.zooting.api.domain.file.entity.File, com.zooting.api.domain.file.entity.QFile>createList("files", com.zooting.api.domain.file.entity.File.class, com.zooting.api.domain.file.entity.QFile.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final BooleanPath status = createBoolean("status");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDM(String variable) {
        this(DM.class, forVariable(variable), INITS);
    }

    public QDM(Path<? extends DM> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDM(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDM(PathMetadata metadata, PathInits inits) {
        this(DM.class, metadata, inits);
    }

    public QDM(Class<? extends DM> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dmRoom = inits.isInitialized("dmRoom") ? new QDMRoom(forProperty("dmRoom"), inits.get("dmRoom")) : null;
    }

}

