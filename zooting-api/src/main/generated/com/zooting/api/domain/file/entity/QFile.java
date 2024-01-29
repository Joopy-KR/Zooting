package com.zooting.api.domain.file.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFile is a Querydsl query type for File
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFile extends EntityPathBase<File> {

    private static final long serialVersionUID = 1317890648L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFile file = new QFile("file");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.zooting.api.domain.dm.entity.QDM dm;

    public final StringPath fileDir = createString("fileDir");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgUrl = createString("imgUrl");

    public final StringPath originFileName = createString("originFileName");

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFile(String variable) {
        this(File.class, forVariable(variable), INITS);
    }

    public QFile(Path<? extends File> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFile(PathMetadata metadata, PathInits inits) {
        this(File.class, metadata, inits);
    }

    public QFile(Class<? extends File> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dm = inits.isInitialized("dm") ? new com.zooting.api.domain.dm.entity.QDM(forProperty("dm"), inits.get("dm")) : null;
    }

}

