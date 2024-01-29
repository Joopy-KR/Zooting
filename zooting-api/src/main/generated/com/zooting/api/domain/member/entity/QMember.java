package com.zooting.api.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1510210644L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.zooting.api.domain.QBaseEntity _super = new com.zooting.api.domain.QBaseEntity(this);

    public final QAdditionalInfo additionalInfo;

    public final StringPath address = createString("address");

    public final com.zooting.api.domain.animalface.entity.QAnimalFace animalFace;

    public final DateTimePath<java.util.Date> birth = createDateTime("birth", java.util.Date.class);

    public final ListPath<com.zooting.api.domain.block.entity.Block, com.zooting.api.domain.block.entity.QBlock> blockFromList = this.<com.zooting.api.domain.block.entity.Block, com.zooting.api.domain.block.entity.QBlock>createList("blockFromList", com.zooting.api.domain.block.entity.Block.class, com.zooting.api.domain.block.entity.QBlock.class, PathInits.DIRECT2);

    public final ListPath<com.zooting.api.domain.block.entity.Block, com.zooting.api.domain.block.entity.QBlock> blockToList = this.<com.zooting.api.domain.block.entity.Block, com.zooting.api.domain.block.entity.QBlock>createList("blockToList", com.zooting.api.domain.block.entity.Block.class, com.zooting.api.domain.block.entity.QBlock.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.zooting.api.domain.dm.entity.DMRoom, com.zooting.api.domain.dm.entity.QDMRoom> dmRooms = this.<com.zooting.api.domain.dm.entity.DMRoom, com.zooting.api.domain.dm.entity.QDMRoom>createList("dmRooms", com.zooting.api.domain.dm.entity.DMRoom.class, com.zooting.api.domain.dm.entity.QDMRoom.class, PathInits.DIRECT2);

    public final ListPath<com.zooting.api.domain.dm.entity.DMRoom, com.zooting.api.domain.dm.entity.QDMRoom> dmRoomsReverse = this.<com.zooting.api.domain.dm.entity.DMRoom, com.zooting.api.domain.dm.entity.QDMRoom>createList("dmRoomsReverse", com.zooting.api.domain.dm.entity.DMRoom.class, com.zooting.api.domain.dm.entity.QDMRoom.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final ListPath<com.zooting.api.domain.friend.entity.Friend, com.zooting.api.domain.friend.entity.QFriend> friendList = this.<com.zooting.api.domain.friend.entity.Friend, com.zooting.api.domain.friend.entity.QFriend>createList("friendList", com.zooting.api.domain.friend.entity.Friend.class, com.zooting.api.domain.friend.entity.QFriend.class, PathInits.DIRECT2);

    public final ListPath<com.zooting.api.domain.friend.entity.FriendRequest, com.zooting.api.domain.friend.entity.QFriendRequest> friendRequestFromMeList = this.<com.zooting.api.domain.friend.entity.FriendRequest, com.zooting.api.domain.friend.entity.QFriendRequest>createList("friendRequestFromMeList", com.zooting.api.domain.friend.entity.FriendRequest.class, com.zooting.api.domain.friend.entity.QFriendRequest.class, PathInits.DIRECT2);

    public final ListPath<com.zooting.api.domain.friend.entity.FriendRequest, com.zooting.api.domain.friend.entity.QFriendRequest> friendRequestToMeList = this.<com.zooting.api.domain.friend.entity.FriendRequest, com.zooting.api.domain.friend.entity.QFriendRequest>createList("friendRequestToMeList", com.zooting.api.domain.friend.entity.FriendRequest.class, com.zooting.api.domain.friend.entity.QFriendRequest.class, PathInits.DIRECT2);

    public final StringPath gender = createString("gender");

    public final ListPath<com.zooting.api.domain.meeting.entity.MeetingLog, com.zooting.api.domain.meeting.entity.QMeetingLog> meetingLogList = this.<com.zooting.api.domain.meeting.entity.MeetingLog, com.zooting.api.domain.meeting.entity.QMeetingLog>createList("meetingLogList", com.zooting.api.domain.meeting.entity.MeetingLog.class, com.zooting.api.domain.meeting.entity.QMeetingLog.class, PathInits.DIRECT2);

    public final ListPath<com.zooting.api.domain.background.entity.BackgroundInventory, com.zooting.api.domain.background.entity.QBackgroundInventory> myBackgrounds = this.<com.zooting.api.domain.background.entity.BackgroundInventory, com.zooting.api.domain.background.entity.QBackgroundInventory>createList("myBackgrounds", com.zooting.api.domain.background.entity.BackgroundInventory.class, com.zooting.api.domain.background.entity.QBackgroundInventory.class, PathInits.DIRECT2);

    public final ListPath<com.zooting.api.domain.mask.entity.MaskInventory, com.zooting.api.domain.mask.entity.QMaskInventory> myMasks = this.<com.zooting.api.domain.mask.entity.MaskInventory, com.zooting.api.domain.mask.entity.QMaskInventory>createList("myMasks", com.zooting.api.domain.mask.entity.MaskInventory.class, com.zooting.api.domain.mask.entity.QMaskInventory.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Long> point = createNumber("point", Long.class);

    public final ListPath<Privilege, EnumPath<Privilege>> role = this.<Privilege, EnumPath<Privilege>>createList("role", Privilege.class, EnumPath.class, PathInits.DIRECT2);

    public final BooleanPath status = createBoolean("status");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.additionalInfo = inits.isInitialized("additionalInfo") ? new QAdditionalInfo(forProperty("additionalInfo"), inits.get("additionalInfo")) : null;
        this.animalFace = inits.isInitialized("animalFace") ? new com.zooting.api.domain.animalface.entity.QAnimalFace(forProperty("animalFace"), inits.get("animalFace")) : null;
    }

}

