package de.seuhd.worldcup

import java.io.File
import kotlinx.serialization.json.*
import kotlinx.serialization.*

var bets = mutableListOf<Bet>()

fun main() {
    val jsonFile = File("src/main/resources/world_cup_2026_full_data.json").readText(Charsets.UTF_8)

    val worldCupData = Json.decodeFromString<WorldCupData>(jsonFile)

    var runLoop: Boolean = true

    while (runLoop) {
        println("===== FIFA World Cup 2026 ? Betting Console ===== \n" +
                "1) Show Standings \n" +
                "2) Show Matches \n" +
                "3) Place Bets \n" +
                "4) Show Betting Score \n" +
                "5) Exit \n" +
                "================================================= \n" +
                "\"Choose an option (1 to 5) : ")
        val input = readln().toIntOrNull()
        when (input) {
            1 -> showStandings(worldCupData.groups)
            2 -> showMatches(worldCupData.groups)
            3 -> placeBets(worldCupData.groups)
            4 -> showBettingScore(worldCupData.groups)
            5 -> runLoop = false
            else -> {}
        }
    }
}

/* -------------------------------------------------------------
   1) Show Standings
   ------------------------------------------------------------- */
private fun showStandings(allGroups: List<Group>) {

    fun showTable(group: Group?){
        if (group != null) {
            println( "${group.name} \n"  +
                    " ID | W | D | L | Goals | Points")
            for (team in group.teams){
                val teamScore = calculateScores(group, team)
                println("${team.id} | ${teamScore.wins} | ${teamScore.draw} | ${teamScore.lose} |   ${teamScore.goals}   |  ${teamScore.wins * 3 + teamScore.draw}")
            }
            println("\n")
        }
    }

    println("===== FIFA World Cup 2026 Show Standnings ===== \n" +
            "1) Single Table \n" +
            "2) All Tables \n" +
            "3) Back \n" +
            "================================================= \n" +
            "Expecting Input: ")
    val input = readln().toIntOrNull()
    when (input) {
        1 -> {println("Select Group")
            for (group in allGroups) {
                println(group.name)}

            println("Enter Group: ")
            val inputGroup = readln()

            showTable(allGroups.find{it.name.lowercase().equals(inputGroup.lowercase())})
        }
        2 -> for (group in allGroups) {
                showTable(group)
        }
        else -> {}
    }

}

private fun calculateScores(group: Group, team: Team): MatchScore {

    var wins = 0
    var lose = 0
    var draws = 0
    var goals = 0

    for (matches in group.matches) {
        if (team.id == matches.homeTeam){
            if ((matches.homeScore ?: 0) == (matches.awayScore ?: 0)){
                draws += 1
                goals += matches.homeScore ?: 0
            }else if ((matches.homeScore ?: 0) > (matches.awayScore ?: 0)) {
                wins +=  3
                goals += matches.homeScore ?: 0
            } else
                lose++
                goals += matches.homeScore ?: 0
        }else if (team.id == matches.awayTeam){
            if ((matches.homeScore ?: 0) == (matches.awayScore ?: 0)){
                draws += 1
                goals += matches.awayScore ?: 0
            }else if ((matches.homeScore ?: 0) < (matches.awayScore ?: 0)) {
                wins +=  3
                goals += matches.awayScore ?: 0
            } else {
                lose++
                goals += matches.awayScore ?: 0
            }
        }
    }

    return MatchScore(wins, lose, draws, goals)
}


/* -------------------------------------------------------------
   2) Show Matches
   ------------------------------------------------------------- */
private fun showMatches(allGroups: List<Group>) {
    println("===== FIFA World Cup 2026 Show Matches ===== \n" +
            "Which groups matches do you want to see? (e.g. Group A) \n " +
            "================================================= \n" +
            "Expecting Input: ")
    val input = readln().lowercase()

    val group = allGroups.find{it.name.lowercase().equals(input.lowercase())}

    if (group != null){
        for (matches in group.matches){
            println("ID: ${matches.matchId} \n" +
                    "Date: ${matches.date}, Ground: ${matches.ground} \n" +
                    "${matches.homeTeam} vs ${matches.awayTeam} \n" +
                    "Current Score: ${matches.homeScore}:${matches.awayScore} \n")
        }
    } else {
        println("Group not found")
    }


}

/* -------------------------------------------------------------
   3) Place Bets
   ------------------------------------------------------------- */
private fun placeBets(allGroups: List<Group>) {
    println("===== FIFA World Cup 2026 Show Matches ===== \n" +
            "Which groups do you want to bet on? (e.g. Group A) \n " +
            "================================================= \n" +
            "Expecting Input: ")
    val input = readln().lowercase()

    val group = allGroups.find{it.name.lowercase().equals(input.lowercase())}

    if (group != null){
        for (matches in group.matches){
            println("ID: ${matches.matchId}: ${matches.homeTeam} vs ${matches.awayTeam} \n" +
                    "Place your bet (1: Home Win, 2: Away Win, 3: Draw): ")
            val input = readln().toInt()

            if (input != null){
                bets.add(Bet(matches.matchId, input))
            }
        }
    } else {
        println("Group not found")
    }

}

/* -------------------------------------------------------------
   4) Show Betting Score
   ------------------------------------------------------------- */
private fun showBettingScore(allGroups: List<Group>) {

    val score = calculateBettingScore(allGroups)

    println("===== FIFA World Cup 2026 Show Matches ===== \n" +
            "Current Score: ${score.correctBets} \n" +
            "Correct Predictions: ${score.correctBets} \n" +
            "Incorrect Predictions: ${score.totalBets - score.correctBets} \n" +
            "============================================ ")

}

internal fun calculateBettingScore(allGroups: List<Group>): BettingScore {
    var score = 0
    var totalMatches = 0

    for (group in allGroups) {
        for (matches in group.matches){
            for (bet in bets){
                if (matches.matchId == bet.matchId){
                    if ((matches.homeScore ?: 0) > (matches.awayScore ?: 0)){
                        if (bet.bet == 1){
                            score++
                        }
                    } else if ((matches.awayScore ?: 0) > (matches.homeScore ?: 0)){
                        if (bet.bet == 2){
                            score++
                        }
                    } else {
                        if (bet.bet == 3){
                            score++
                        }
                    }
                    totalMatches++
                }
            }
        }
    }

    return BettingScore(score, totalMatches)
}