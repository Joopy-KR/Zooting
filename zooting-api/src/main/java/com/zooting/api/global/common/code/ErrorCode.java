package com.zooting.api.global.common.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 409 : Conflict
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, "G003", "Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(400, "G005", "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(400, "G006", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400, "G007", "com.fasterxml.jackson.core Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403, "G008", "Forbidden Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "G009", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(404, "G010", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(404, "G011", "Handle Validation Exception"),

    // Header 가 유효하지 않은 경우
    NOT_VALID_HEADER_ERROR(404, "G012", "Header에 데이터가 존재하지 않는 경우 "),
    // 로컬 파일 업로드 실패,
    FAILED_TO_UPLOAD_LOCAL_FILE(404, "F001", "로컬 파일 업로드 실패"),
    // 로컬 파일 업로드 실패,
    FAILED_TO_UPLOAD_S3_FILE(404, "F002", "S3 파일 업로드 실패"),
    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),

    /**
     * ******************************* Business Error CodeList ***************************************
     */

    // 사용자 권한 인증 실패 (CODE: 100)
    UNAUTHORIZED_USER_EXCEPTION(403, "B100", "권한이 없는 사용자입니다."),
    FAILED_OAUTH2_AUTHENTICATION_EXCEPTION(403, "B101", "소셜 로그인에 실패했습니다."),
    INVALID_ACCESS_TOKEN_EXCEPTION(403, "B102", "유효하지 않은 ACCESS TOKEN 입니다."),
    EXPIRED_ACCESS_TOKEN_EXCEPTION(403, "B103", "만료된 ACCESS TOKEN 입니다."),
    INCONSISTENT_ACCESS_TOKEN_EXCEPTION(403, "B104", "일치하지 않는 ACCESS TOKEN 입니다."),
    INVALID_REFRESH_TOKEN_EXCEPTION(403, "B105", "유효하지 않은 REFRESH TOKEN 입니다."),
    EXPIRED_REFRESH_TOKEN_EXCEPTION(403, "B106", "만료된 REFRESH TOKEN 입니다."),
    INCONSISTENT_REFRESH_TOKEN_EXCEPTION(403, "B107", "일치하지 않는 REFRESH TOKEN 입니다."),
    ILLEGAL_TOKEN_EXCEPTION(403, "B107", "헤더에 토큰 정보가 존재하지 않습니다."),

    // 유저 에러 (CODE: 200)
    // 유저가 존재하지 않음
    NOT_FOUND_USER(404, "B200", "존재하지 않는 유저입니다."),
    NOT_ENOUGH_POINT(404, "B021", "포인트가 부족합니다."),
    // 메뉴 에러 (CODE: 300)
    // 메뉴가 존재하지 않음 (CODE: 300)
    NOT_FOUND_MENU(404, "B300", "존재하지 않는 메뉴 입니다."),
    FAILED_TO_UPDATE_MENU(404, "B301", "메뉴 업데이트에 실패 하였습니다."),
    // 음식점 에러 (CODE: 400)
    NOT_FOUND_RESTAURANT(404, "B400", "존재하지 않는 음식점 입니다."),
    // S3 File 에러 (CODE: 900)
    NOT_FOUND_S3FILE(404, "B900", "존재하지 않는 S3 파일입니다."),
    // 친구 요청 에러 (CODE: 201)
    ALREADY_EXIST_FRIEND_REQUEST(404,"B201" ,"이미 요청을 보냈습니다" ),
    // 친구 에러 (CODE: 202)
    ALREADY_EXIST_FRIEND(404,"B202" ,"이미 친구입니다" ),

    ;

    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private final int status;

    // 에러 코드의 '코드간 구분 값'을 반환한다.
    private final String divisionCode;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private final String message;

    // 생성자 구성
    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}
