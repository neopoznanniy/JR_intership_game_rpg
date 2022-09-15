package com.game.repository;

import java.util.List;

public interface PlayerRepository<T> {
    List<T> getAllPlayersQuery(String name);
}