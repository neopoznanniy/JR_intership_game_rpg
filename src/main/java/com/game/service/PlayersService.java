package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.*;

@Service
public class PlayersService {

    @Autowired
    private PlayerCrudRepository playerCrudRepository;

    @Transactional
    public List<Player> findAllPlayers(String name,
                                       String title,
                                       Race race,
                                       Profession profession,
                                       Long after,
                                       Long before,
                                       Integer minExperience,
                                       Integer maxExperience,
                                       Integer minLevel,
                                       Integer maxLevel,
                                       Boolean banned,
                                       Pageable pageable) {

        String strRace = "";
        String strProfession = "";
        Date afterDate = new Date(0L);
        Date beforeDate = new Date();
        int minExp = 0;
        int maxExp = Integer.MAX_VALUE;
        int minLvl = 0;
        int maxLvl = Integer.MAX_VALUE;

        if(title == null) title = "";
        if(name == null) name = "";
        if(race != null){strRace = race.name();}
        if(profession != null){strProfession = profession.name();}
        if(after != null){afterDate = new Date(after);}
        if(before != null){beforeDate = new Date(before);}
        if(minExperience != null){minExp = minExperience;}
        if(maxExperience != null){maxExp = maxExperience;}
        if(minLevel != null){minLvl = minLevel;}
        if(maxLevel != null){maxLvl = maxLevel;}

        Collection<Boolean> bans = new ArrayList<>();
        if (banned == null) {
            bans.add(true);
            bans.add(false);
        } else {
            bans.add(banned);
        }

        return playerCrudRepository.findPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
                name, title, strRace, strProfession, afterDate, beforeDate, minExp, maxExp, minLvl, maxLvl, bans, pageable);
    }

    @Transactional
    public Long count(String name,
                      String title,
                      Race race,
                      Profession profession,
                      Long after,
                      Long before,
                      Integer minExperience,
                      Integer maxExperience,
                      Integer minLevel,
                      Integer maxLevel,
                      Boolean banned) {

        String strRace = "";
        String strProfession = "";
        Date afterDate = new Date(0L);
        Date beforeDate = new Date();
        int minExp = 0;
        int maxExp = Integer.MAX_VALUE;
        int minLvl = 0;
        int maxLvl = Integer.MAX_VALUE;

        if(title == null) title = "";
        if(name == null) name = "";
        if(race != null){strRace = race.name();}
        if(profession != null){strProfession = profession.name();}
        if(after != null){afterDate = new Date(after);}
        if(before != null){beforeDate = new Date(before);}
        if(minExperience != null){minExp = minExperience;}
        if(maxExperience != null){maxExp = maxExperience;}
        if(minLevel != null){minLvl = minLevel;}
        if(maxLevel != null){maxLvl = maxLevel;}

        Collection<Boolean> banneds = new ArrayList<>();
        if (banned == null) {
            banneds.add(true);
            banneds.add(false);
        } else {
            banneds.add(banned);
        }

        return (long) playerCrudRepository.findPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
                name, title, strRace, strProfession, afterDate, beforeDate, minExp, maxExp, minLvl, maxLvl, banneds, Pageable.unpaged()).size();
    }

    @Transactional
    public Player createPLayer(Player player) {

        System.out.println("PlayersController.newPlayer");
        System.out.println("newPlayer = " + player);
        System.out.println("newPLayer is null: " + player == null );
        try {
            if (player.getName() == null || player.getTitle() == null || player.getRace() == null ||
                    player.getProfession() == null || player.getBirthday() == null || player.getExperience() == null ||
                    player.getBanned() == null) throw new ValidationException("Content is empty");
            if (player.getExperience() < 0) throw new ValidationException("Experience less then 0");
            if (player.getExperience() > 10_000_000) throw new ValidationException("Experience greater then 10_000_0000");
            if (player.getBirthday().getTime() < 0) throw new ValidationException("Invalid birthday");
            if (player.getTitle().length() > 30) throw new ValidationException("Title length is too big");
        } catch (Exception e) {
            System.out.println("e = " + e);
            if (e instanceof ValidationException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Parameters is not valid", e);
        }

        System.out.println("newPlayer = " + player);

        Integer lvl = calcLevel(player.getExperience());
        Integer untilNextLevel = calcUntilNextLevel(lvl, player.getExperience());
        player.setLevel(lvl);
        player.setUntilNextLevel(untilNextLevel);

        if (player.getBanned() == null) player.setBanned(false);

        return playerCrudRepository.save(player);
    }

    @Transactional
    public Player findById(String id) {
        Player player = null;
        Long longId = null;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero or less");
            player = playerCrudRepository.findById(longId).get();
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
    }

    public void deleteById(String id) {
        Player player = null;
        Long longId = null;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero or less");
            player = playerCrudRepository.findById(longId).get();
            playerCrudRepository.deleteById(longId);
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

    public Player replacePlayer(Player newPlayer, String id) {
        Player oldPlayer = null;
        Long longId = null;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException("Id is zero");
            if (newPlayer.getExperience() < 0) throw new ValidationException("Experience less then 0");
            if (newPlayer.getExperience() > 10_000_000) throw new ValidationException("Experience greater then 10_000_0000");
            if (newPlayer.getBirthday().getTime() < 0) throw new ValidationException("Invalid birthday");
            oldPlayer = playerCrudRepository.findById(longId).get();
        } catch (Exception e) {
            if (e instanceof ValidationException || e instanceof NumberFormatException)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameters is not valid", e);
            if (e instanceof NoSuchElementException)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found", e);
        }
        final Long FIN_LONG_ID = longId;

        return playerCrudRepository.findById(longId)
                .map(player -> {
                    if (newPlayer.getName() != null) player.setName(newPlayer.getName());
                    if (newPlayer.getTitle() != null) player.setTitle(newPlayer.getTitle());
                    if (newPlayer.getRace() != null) player.setRace(newPlayer.getRace());
                    if (newPlayer.getProfession() != null) player.setProfession(newPlayer.getProfession());
                    if (newPlayer.getBirthday() != null) player.setBirthday(newPlayer.getBirthday());
                    if (newPlayer.getExperience() != null) {
                        player.setExperience(newPlayer.getExperience());
                        Integer lvl = calcLevel(newPlayer.getExperience());
                        Integer untilNextLevel = calcUntilNextLevel(lvl, newPlayer.getExperience());
                        player.setLevel(lvl);
                        player.setUntilNextLevel(untilNextLevel);
                    }
                    if (newPlayer.getBanned() != null) player.setBanned(newPlayer.getBanned());
                    return playerCrudRepository.save(player);
                })
                .orElseGet(() -> {
                    newPlayer.setId(FIN_LONG_ID);
                    return playerCrudRepository.save(newPlayer);
                });
    }

    private Integer calcLevel(Integer exp){
        return (int) ((Math.sqrt(2500 + 200. * exp) - 50) / 100);
    }

    private Integer calcUntilNextLevel(Integer lvl, Integer exp) {
        return 50 * (lvl + 1) * (lvl + 2) - exp;
    }
}