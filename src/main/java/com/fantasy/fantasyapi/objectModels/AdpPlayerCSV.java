package com.fantasy.fantasyapi.objectModels;

public class AdpPlayerCSV {
    private String name;
    private String position;
    private double overallRank;
    private double positionalRank;
    private double tier;
    private String attribute;
    private double auctionValue;

    public AdpPlayerCSV(String name, String position, double overallRank, double positionalRank,
                        double tier, String attribute, double auctionValue) {
        this.name = name;
        this.position = position;
        this.overallRank = overallRank;
        this.positionalRank = positionalRank;
        this.tier = tier;
        this.attribute = attribute;
        this.auctionValue = auctionValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getOverallRank() {
        return overallRank;
    }

    public void setOverallRank(double overallRank) {
        this.overallRank = overallRank;
    }

    public double getPositionalRank() {
        return positionalRank;
    }

    public void setPositionalRank(double positionalRank) {
        this.positionalRank = positionalRank;
    }

    public double getTier() {
        return tier;
    }

    public void setTier(double tier) {
        this.tier = tier;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public double getAuctionValue() {
        return auctionValue;
    }

    public void setAuctionValue(double auctionValue) {
        this.auctionValue = auctionValue;
    }

    @Override
    public String toString() {
        return "AdpPlayerCSV{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", overallRank=" + overallRank +
                ", positionalRank=" + positionalRank +
                ", tier=" + tier +
                ", attribute='" + attribute + '\'' +
                ", auctionValue=" + auctionValue +
                '}';
    }
}
