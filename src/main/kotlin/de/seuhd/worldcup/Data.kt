package de.seuhd.worldcup

import kotlinx.serialization.Serializable

@Serializable
data class WorldCupData(
    val tournament: String,
   val groups: List<Group>,
   val knockouts: List<Knockout>
)

@Serializable
data class Group(val name: String, val teams: List<Team>, val matches: List<Match>)

@Serializable
data class Team(val id: String, val name: String)

@Serializable
data class Match(val matchId: Int, val round: String, val date: String,
                 val homeTeam: String, val awayTeam: String, var homeScore: Int?, var awayScore: Int?,
                 val ground: String)

@Serializable
data class Knockout(val matchId: Int, val round: String, val date: String, val homePlaceholder: String,
                    val awayPlaceholder: String,var homeScore: Int?, var awayScore: Int?,
                    val ground: String)

data class MatchScore(val wins: Int, val lose: Int, val draw: Int, val goals: Int)

data class Bet(val matchId: Int, val bet: Int)

data class BettingScore(val correctBets: Int, val totalBets: Int)