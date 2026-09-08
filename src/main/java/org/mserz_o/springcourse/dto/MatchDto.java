package org.mserz_o.springcourse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MatchDto {
    private Integer id;
    private PlayerDto firstPlayerDto;
    private PlayerDto secondPlayerDto;
    private PlayerDto winner;
}
