package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface PlayerCrudRepository extends PagingAndSortingRepository<Player, Long>, PlayerRepository<Player> {
    List<Player> findPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
            String name,
            String title,
            String strRace,
            String strProfession,
            Date dateAfter,
            Date dateBefore,
            Integer minExpirience,
            Integer maxExpirience,
            Integer minLevel,
            Integer maxLevel,
            Collection <Boolean> banneds,
            Pageable page);

    /*Long countByPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
            String name,
            String title,
            String strRace,
            String strProfession,
            Date dateAfter,
            Date dateBefore,
            Integer minExpirience,
            Integer maxExpirience,
            Integer minLevel,
            Integer maxLevel,
            Collection<Boolean> banneds);*/


}