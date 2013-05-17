package net.conriot.prison;

import lombok.Getter;

public enum Message
{
	// Bounty messages
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
	BOUNTY_TOP_ITEM("num", "name", "bounty"),
	// Mine messages
	MINE_PERMISSION(),
	MINE_MANAGER_OFFLINE(),
	MINE_ARG_COUNT(),
	MINE_HELP_HEADER(),
	MINE_HELP_ITEM("cmd", "args", "desc"),
	MINE_CREATE_SUCCESS("id"),
	MINE_CREATE_FAILURE("id"),
	MINE_CREATE_SELECTION(),
	MINE_DELETE_SUCCESS("id"),
	MINE_DELETE_FAILURE("id"),
	MINE_ADD_SUCCESS("id", "type", "data", "weight"),
	MINE_ADD_FAILURE("id", "type", "data"),
	MINE_REMOVE_SUCCESS("id", "type", "data"),
	MINE_REMOVE_FAILURE("id", "type", "data"),
	MINE_LIST_HEADER("id"),
	MINE_LIST_ITEM("type", "id", "data", "weight"),
	MINE_LIST_FAILURE("id");
	
	@Getter private String[] vars;
	
	private Message(String... args)
	{
		vars = args;
	}
}
