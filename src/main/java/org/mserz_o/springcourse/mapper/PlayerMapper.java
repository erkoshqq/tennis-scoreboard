package org.mserz_o.springcourse.mapper;

import org.mserz_o.springcourse.dto.PlayerDto;
import org.mserz_o.springcourse.model.entity.Player;

public class PlayerMapper {

    public static PlayerDto mapFrom(Player player){
        return PlayerDto
                .builder()
                .id(player.getId())
                .name(player.getName())
                .build();
    }
}
