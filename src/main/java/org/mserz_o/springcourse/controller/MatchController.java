package org.mserz_o.springcourse.controller;

import jakarta.validation.Valid;
import org.mserz_o.springcourse.dto.MatchForm;
import org.mserz_o.springcourse.model.enums.MatchResult;
import org.mserz_o.springcourse.model.enums.Side;
import org.mserz_o.springcourse.service.ActiveMatchService;
import org.mserz_o.springcourse.service.MatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/matches")
public class MatchController {

    private final ActiveMatchService activeMatchService;
    private final MatchService matchService;

    public MatchController(ActiveMatchService activeMatchService, MatchService matchService) {
        this.activeMatchService = activeMatchService;
        this.matchService = matchService;
    }

    @GetMapping("/new-match")
    public String newMatch(@ModelAttribute("matchForm")MatchForm matchForm){
        return "new-match";
    }

    @PostMapping()
    public String create(@ModelAttribute("matchForm") @Valid MatchForm matchForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "new-match";
        }
        if (matchForm.getFirstName().equals(matchForm.getSecondName())) {
            bindingResult.reject(
                    "players.same",
                    "Игрок не может играть сам с собой"
            );
            return "new-match";
        }

        UUID newMatchUUID = activeMatchService.createMatch(matchForm.getFirstName(),matchForm.getSecondName());

        return "redirect:/matches/" + newMatchUUID.toString();
    }

    @GetMapping("/{uuid}")
    public String matchPage(@PathVariable UUID uuid, Model model){
        model.addAttribute("uuid", uuid);
        model.addAttribute("matchInfo", activeMatchService.showMatchInfo(uuid));
        return "match-score";
    }

    @PostMapping("/{uuid}/point")
    public String addPoint(@RequestParam Side winnerSide, @PathVariable UUID uuid){
        MatchResult matchResult = activeMatchService.playerWonPoint(uuid, winnerSide);
        return switch (matchResult) {
            case ONGOING -> "redirect:/matches/" + uuid;
            case WIN -> "redirect:/matches";
        };
    }

    @GetMapping
    public String allMatches(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "name", required = false) String name,
            Model model
    ) {
        model.addAttribute("matches", matchService.showMatches(page,name));
        model.addAttribute("page", page);
        model.addAttribute("name", name);
        model.addAttribute("pageQuantity", matchService.showPageQuantity(name));

        return "matches";
    }
}
