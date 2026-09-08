package org.mserz_o.springcourse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PlayerStateInfo {
    private PlayerDto playerDto;
    private int set;
    private int game;
    private String point;
    private Integer tieBreak;
}
