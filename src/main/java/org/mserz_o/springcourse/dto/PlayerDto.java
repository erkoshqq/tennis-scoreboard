package org.mserz_o.springcourse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PlayerDto {
    private Integer id;
    private String name;
}
