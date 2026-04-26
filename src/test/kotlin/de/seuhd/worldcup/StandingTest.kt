package de.seuhd.worldcup

import kotlinx.serialization.json.Json
import java.io.File
import kotlin.test.*

class StandingsServiceTest {

    @Test
    fun `calculate standings for a simple group`() {

        val jsonFile = File("src/main/resources/world_cup_2026_full_data.json").readText(Charsets.UTF_8)
        val worldCupData = Json.decodeFromString<WorldCupData>(jsonFile)

        val group = worldCupData.groups
        group[0].matches[0].homeScore = 4
        group[0].matches[0].awayScore = 0

        bets.add(Bet(group[0].matches[0].matchId, 1))

        val testBets = calculateBettingScore(group)

        val correctScore = testBets.correctBets
        val correctTotal = testBets.totalBets

        assertEquals(1, correctScore)
        assertEquals(1, correctTotal)
    }
}