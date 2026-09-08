package org.mserz_o.springcourse.service;

import org.mserz_o.springcourse.dao.MatchDao;
import org.mserz_o.springcourse.dao.PlayerDao;
import org.mserz_o.springcourse.dto.*;
import org.mserz_o.springcourse.exception.NotFoundException;
import org.mserz_o.springcourse.mapper.MatchMapper;
import org.mserz_o.springcourse.model.entity.Match;

import org.mserz_o.springcourse.model.entity.Player;
import org.mserz_o.springcourse.model.enums.Side;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MatchService {
    private static final int LIMIT = 10;

    private final MatchDao matchDao;

    public MatchService(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    @Transactional
    public void saveMatch(Player firstPlayer, Player secondPlayer, Player winner){
        matchDao.save(firstPlayer, secondPlayer, winner);
    }

    @Transactional(readOnly = true)
    public Integer showPageQuantity(String name){
        return (int) Math.ceil((double) matchDao.showMatchQuantity(name) /LIMIT);
    }

    private List<Match> findMatches(String name, int offset){
        if(name==null)
            return matchDao.show(offset,LIMIT);
        else
            return matchDao.show(name,offset,LIMIT);
    }


    private int validatePage(int page, Integer pageQuantity){
        if(pageQuantity == 0){
            return 1;
        }
        if (page < 1 || page > pageQuantity)
            throw new NotFoundException("Страница не найдена");

        return page;
    }

    private List<MatchDto> toMatchDtoList(List<Match> matches){
        List<MatchDto> matchDtos = new ArrayList<>();

        for (Match match:matches){
            matchDtos.add(MatchMapper.mapFrom(match));
        }

        return matchDtos;
    }

    @Transactional(readOnly = true)
    public List<MatchDto> showMatches(int page, String name){

        int validatedPage = validatePage(page,showPageQuantity(name));

        validatedPage--;

        int offset = validatedPage*LIMIT;

        List<Match> matches = findMatches(name, offset);

        return toMatchDtoList(matches);
    }
}
