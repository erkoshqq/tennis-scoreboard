package org.mserz_o.springcourse.mapper;

import org.mserz_o.springcourse.dto.MatchDto;
import org.mserz_o.springcourse.model.entity.Match;

public class MatchMapper {

    public static MatchDto mapFrom(Match match){
        return MatchDto
                .builder()
                .id(match.getId())
                .firstPlayerDto(PlayerMapper.mapFrom(match.getFirstPlayer()))
                .secondPlayerDto(PlayerMapper.mapFrom(match.getSecondPlayer()))
                .winner(PlayerMapper.mapFrom(match.getWinner()))
                .build();
    }
}
