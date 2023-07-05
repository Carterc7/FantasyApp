package com.fantasy.fantasyapi.points;

import com.fantasy.fantasyapi.objectModels.StatsPlayer;

public class PointsCalculator 
{
    /** 
     * Method to calculate fantasyPoints for a StatsPlayer
     * Handles offensive and defensive scoring (PPR)
     * @param player
     * @return double
     */
    public double calculateFantasyPoints(StatsPlayer player) 
    {
        double fantasyPoints = 0;

        // Calculate fantasy points for passing
        int passYards = 0;
        int passTouchdowns = 0;
        int interceptions = 0;
        if(player.getPassYds() != "") 
        {
            passYards = Integer.parseInt(player.getPassYds());
        }
        if(player.getPassTD() != "") 
        {
            passTouchdowns = Integer.parseInt(player.getPassTD());
        }
        if(player.getInterceptions() != "") 
        {
            interceptions = Integer.parseInt(player.getInterceptions());
        }
        double passingPoints = (passYards / 25.0 + passTouchdowns * 4.0) - (interceptions * 2);

        // Calculate fantasy points for rushing
        int rushYards = 0;
        int rushTouchdowns = 0;
        if(player.getRushYds() != "") 
        {
            rushYards = Integer.parseInt(player.getRushYds());
        }
        if(player.getRushTd() != "") 
        {
            rushTouchdowns = Integer.parseInt(player.getRushTd());
        }
        double rushingPoints = rushYards / 10.0 + rushTouchdowns * 6.0;

        // Calculate fantasy points for receiving
        int receptions = 0;
        int receivingYards = 0;
        int receivingTouchdowns = 0;
        if(player.getReceptions() != "") 
        {
            receptions = Integer.parseInt(player.getReceptions());
        }
        if(player.getRecYds() != "") 
        {
            receivingYards = Integer.parseInt(player.getRecYds());
        }
        if(player.getRecTD() != "") 
        {
            receivingTouchdowns = Integer.parseInt(player.getRecTD());
        }
        double receivingPoints = receptions + receivingYards / 10.0 + receivingTouchdowns * 6.0;

        int assistedTackles = 0;
        int qbHits = 0;
        if(player.getTotalTackles() != "" && player.getSoloTackles() != "")
        {
            assistedTackles = Integer.parseInt(player.getTotalTackles()) - Integer.parseInt(player.getSoloTackles());
        }
        if(player.getQbHits() != "")
        {
            qbHits = Integer.parseInt(player.getQbHits());
        }
        double defOnePointers = (assistedTackles / 2) + qbHits;

        int soloTackles = 0;
        int tfls = 0;
        if(player.getSoloTackles() != "")
        {
            soloTackles = Integer.parseInt(player.getSoloTackles());
        }
        if(player.getTfls() != "")
        {
            tfls = Integer.parseInt(player.getTfls());
        }
        double defTwoPointers = (soloTackles) + (tfls * 2);

        int passDeflections = 0;
        if(player.getPassDeflections() != "")
        {
            passDeflections = Integer.parseInt(player.getPassDeflections());
        }
        double defThreePointers = passDeflections * 3;

        int defTD = 0;
        int sacks = 0;
        int defInterceptions = 0;
        if(player.getDefTD() != "")
        {
            defTD = Integer.parseInt(player.getDefTD());
        }
        if(player.getSacks() != "")
        {
            sacks = Integer.parseInt(player.getSacks());
        }
        if(player.getDefInterceptions() != "")
        {
            defInterceptions = Integer.parseInt(player.getDefInterceptions());
        }
        double defSixPointers = (defTD * 6) + (sacks * 6) + (defInterceptions * 6);

        // Sum up the fantasy points from passing, rushing, and receiving
        fantasyPoints = passingPoints + rushingPoints + receivingPoints + defOnePointers + defTwoPointers + defThreePointers + defSixPointers;
        double roundedPoints = roundToTensPlace(fantasyPoints);
        return roundedPoints;
    }   

    public double roundToTensPlace(double value) 
    {
        double roundedValue = Math.round(value * 10) / 10.0;
        return roundedValue;
    }

}
