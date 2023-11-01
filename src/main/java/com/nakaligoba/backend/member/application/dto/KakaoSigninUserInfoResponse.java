package com.nakaligoba.backend.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class KakaoSigninUserInfoResponse {
    private long id;
    private LocalDateTime connected_at;
    private KakaoSigninUserInfoDto kakao_account;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoSigninUserInfoDto {
        private KakaoProfileDto profile;
        private String email;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class KakaoProfileDto {
            private String nickname;
        }
    }
}