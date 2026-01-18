package com.common_game.rules;

import com.enums.TerrainType;

/**
 * @param terrainType type de tuile
 * @param count       combien de tuiles nécessaires
 * @param adjacent    si ces tuiles doivent être adjacentes à la tuile où on place l’animal
 */
public record TerrainRequirement(TerrainType terrainType, int count, boolean adjacent) {

}
