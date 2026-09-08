package org.mserz_o.springcourse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ActiveMatchDto {
    private PlayerStateInfo firstPlayerStateInfo;
    private PlayerStateInfo secondPlayerStateInfo;
}
