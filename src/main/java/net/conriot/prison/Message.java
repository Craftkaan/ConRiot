package net.conriot.prison;

import lombok.Getter;

public enum Message
{
	BOUNTY_ADDED("player", "target", "added", "total"),
	BOUNTY_COLLECTED("hunter", "victim", "bounty"),
	BOUNTY_HELP_HEADER(),
	BOUNTY_HELP_ITEM("cmd", "args", "desc"),
	BOUNTY_HIS("name", "bounty"),
	BOUNTY_HIS_NONE("name"),
	BOUNTY_LIST_HEADER("page", "total"),
	BOUNTY_LIST_ITEM("num", "name", "bounty"),
	BOUNTY_MINE("bounty"),
	BOUNTY_MINE_NONE(),
	BOUNTY_TOP_HEADER(),
	BOUNTY_TOP_ITEM("num", "name", "bounty");
	
	@Getter private String[] vars;
	
	private Message(String... args)
	{
		vars = args;
	}
}
