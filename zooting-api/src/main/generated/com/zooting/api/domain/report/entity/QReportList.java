package com.zooting.api.domain.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportList is a Querydsl query type for ReportList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportList extends EntityPathBase<ReportList> {

    private static final long serialVersionUID = -916267834L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportList reportList = new QReportList("reportList");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath date = createString("date");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zooting.api.domain.member.entity.QMember member;

    public final StringPath reason = createString("reason");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReportList(String variable) {
        this(ReportList.class, forVariable(variable), INITS);
    }

    public QReportList(Path<? extends ReportList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportList(PathMetadata metadata, PathInits inits) {
        this(ReportList.class, metadata, inits);
    }

    public QReportList(Class<? extends ReportList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zooting.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

