package net.mistersevent.pick.chance;


import net.mistersevent.pick.Main;

public enum XP {
    IRON(Main.getInstance().getConfig().getInt("XP.IRON")),
    GOLD(Main.getInstance().getConfig().getInt("XP.GOLD")),
    COAL(Main.getInstance().getConfig().getInt("XP.COAL")),
    LAPIS(Main.getInstance().getConfig().getInt("XP.LAPIS")),
    DIAMOND(Main.getInstance().getConfig().getInt("XP.DIAMOND")),
    REDSTONE(Main.getInstance().getConfig().getInt("XP.REDSTONE")),
    EMERALD(Main.getInstance().getConfig().getInt("XP.EMERALD"));

    private final double xp;

    private XP(double xp) {
        this.xp = xp;
    }

    public double getXp() {
        return xp;
    }
}
