package net.daboross.bukkitdev.redstoneclockdetector.commands;

import java.util.List;
import java.util.Map;
import net.daboross.bukkitdev.redstoneclockdetector.RCDPlugin;
import net.daboross.bukkitdev.redstoneclockdetector.utils.AbstractCommand;
import net.daboross.bukkitdev.redstoneclockdetector.utils.UsageException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;

public class BreakCommand extends AbstractCommand {

    public BreakCommand(AbstractCommand[] children, RCDPlugin plugin) {
        super("break <num>  Break the block at place of number <num> in list.",
                "redstoneclockdetector.break", children);
        this.plugin = plugin;
    }
    protected RCDPlugin plugin;

    @Override
    protected boolean execute(CommandSender sender, MatchResult[] data) throws UsageException {
        int tpNum = 0;
        if (data.length != 1) {
            throw new UsageException(this.coloredUsage, "Must specify location num.");
        }
        Integer numData = data[0].getInteger();
        if (numData == null || numData <= 0) {
            throw new UsageException(this.coloredUsage, "Location num must be a positive integer.");
        }

        List<Map.Entry<Location, Integer>> actList = this.plugin.getRedstoneActivityList();
        if (tpNum >= actList.size()) {
            sender.sendMessage("Location num " + ChatColor.YELLOW + (tpNum + 1) + " " + ChatColor.WHITE + "does not exist.");
        } else {
            Location loc = actList.get(tpNum).getKey();
            Block block = loc.getBlock();
            String typeString = block.getType().toString();
            if (!block.breakNaturally()) {
                sender.sendMessage(String.format("Cannot break %s block at " + ChatColor.GREEN + "(%d, %d, %d) %s" + ChatColor.WHITE + ".",
                        typeString,
                        block.getX(), block.getY(), block.getZ(),
                        loc.getWorld().getName()));
                return true;
            }
            block.setType(Material.OAK_SIGN);
            Sign s = (Sign) block.getState();
            s.setLine(0, sender.getName());
            s.setLine(1, ChatColor.DARK_RED + "broke a");
            s.setLine(2, typeString);
            s.setLine(3, ChatColor.DARK_RED + "here.");
            s.update();
            sender.sendMessage(String.format(
                    "Has Broken %s block at " + ChatColor.GREEN + "(%d, %d, %d) %s " + ChatColor.WHITE + ".", typeString,
                    block.getX(), block.getY(), block.getZ(), loc.getWorld().getName()));
        }
        return true;
    }

}
