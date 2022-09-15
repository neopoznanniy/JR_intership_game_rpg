package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.ValidationException;
import java.util.*;

@RestController
@RequestMapping("/rest/players")
@Validated
public class PlayerController {

    Logger logger = LoggerFactory.getLogger(PlayerController.class);

    private final PlayersService service;
    @Autowired
    PlayerController(PlayersService service) {
        this.service = service;
    }

    @GetMapping()
    List<Player> all(@RequestParam(required = false) String name,
                     @RequestParam(required = false) String title,
                     @RequestParam(required = false) Race race,
                     @RequestParam(required = false) Profession profession,
                     @RequestParam(required = false) Long after,
                     @RequestParam(required = false) Long before,
                     @RequestParam(required = false) Boolean banned,
                     @RequestParam(required = false) Integer minExperience,
                     @RequestParam(required = false) Integer maxExperience,
                     @RequestParam(required = false) Integer minLevel,
                     @RequestParam(required = false) Integer maxLevel,
                     @RequestParam(required = false) PlayerOrder order,
                     @RequestParam(required = false) Integer pageNumber,
                     @RequestParam(required = false) Integer pageSize) {

        int pgNumber = 0;
        int pgSize = 3;
        String playerOrder = "id";

        if(pageNumber != null){pgNumber = pageNumber;}
        if(pageSize != null){pgSize = pageSize;}
        if(order != null){playerOrder = order.getFieldName();}

        Pageable pageable = PageRequest.of(pgNumber, pgSize, Sort.by(playerOrder));

        return service.findAllPlayers(name, title, race, profession, after, before, minExperience, maxExperience, minLevel, maxLevel, banned, pageable);
    }


    @GetMapping("/count")
    Long count(@RequestParam(required = false) String name,
               @RequestParam(required = false) String title,
               @RequestParam(required = false) Race race,
               @RequestParam(required = false) Profession profession,
               @RequestParam(required = false) Long after,
               @RequestParam(required = false) Long before,
               @RequestParam(required = false) Boolean banned,
               @RequestParam(required = false) Integer minExperience,
               @RequestParam(required = false) Integer maxExperience,
               @RequestParam(required = false) Integer minLevel,
               @RequestParam(required = false) Integer maxLevel) {

        return service.count(name, title, race, profession, after, before, minExperience, maxExperience, minLevel, maxLevel, banned);
    }

    @PostMapping()
    public Player newPlayer(@RequestBody Player newPlayer) {
        return service.createPLayer(newPlayer);
    }

    @GetMapping("/{id}")
    Player one(@PathVariable String id) {
        /*System.out.println("method one:");
        System.out.println(id);*/
        Player player = null;
        Long longId = null;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero or less");
            player = service.findById(longId).get();
        } catch (Exception e) {
//            System.out.println(e);
            if (e instanceof ValidationException || e instanceof NumberFormatException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Id is not valid", e);
            if (e instanceof NoSuchElementException)
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found", e);
        }

        return player;
        /*return service.findById(id)
                .orElseThrow(
                        () -> new PlayerNotFoundException(id)
                );*/
    }

    @PostMapping("/{id}")
    Player replacePlayer(@RequestBody Player newPlayer, @PathVariable String id) {
        /*System.out.println("method replace:");
        System.out.println(id);*/
        Player player = null;
        Long longId = null;
//        System.out.println("newPlayer = " + newPlayer + ", id = " + id);
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero");
            if (newPlayer.getExperience() < 0) throw new ValidationException("Experience less then 0");
            if (newPlayer.getExperience() > 10_000_000) throw new ValidationException("Experience greater then 10_000_0000");
            if (newPlayer.getBirthday().getTime() < 0) throw new ValidationException("Invalid birthday");
            player = service.findById(longId).get();
        } catch (Exception e) {
//            System.out.println(e);
            if (e instanceof ValidationException
                    || e instanceof NumberFormatException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Parameters is not valid", e);
            if (e instanceof NoSuchElementException)
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found", e);
        }


        /*System.out.println("longId = " + longId);
        System.out.println("newPlayer = " + newPlayer);*/
        return service.replacePlayer(newPlayer, longId);
    }


    @DeleteMapping("/{id}")
    void deletePlayer(@PathVariable String id) {


        Player player = null;
        Long longId = null;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero or less");
            player = service.findById(longId).get();
            service.deleteById(longId);
        } catch (Exception e) {
//            System.out.println(e);
            if (e instanceof ValidationException || e instanceof NumberFormatException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Id is not valid", e);
            if (e instanceof NoSuchElementException)
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found", e);
        }

    }


}
