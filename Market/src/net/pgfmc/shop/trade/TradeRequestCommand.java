package net.pgfmc.shop.trade;

public class TradeRequestCommand /*implements CommandExecutor*/ {
	/*
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player) || ((Player) sender).getGameMode() != GameMode.SURVIVAL)
		{
			sender.sendMessage("§cYou cannot execute this command."); // lol
			return true;
		}
		
		if (args.length < 1) return true;
		PlayerData tar = PlayerData.getPlayerData(args[0]);
		if (tar == null) {
			sender.sendMessage("§cPlease Enter a valid player!");
			return true;
		}
		
		PlayerData pd = PlayerData.getPlayerData((Player) sender);
		
		Request r = TradeRequester.DEFAULT.findRequest(pd.getUniqueId(), tar.getUniqueId());
		if (r == null) {
			TradeRequester.DEFAULT.createRequest(pd.getPlayer(), pd.getPlayer());
		}
		
		
		
		
		// XXX ((Player) sender).openInventory(new MainScreen(pd).getInventory()); // opens the inventory
		
		return true;
	}*/
}
