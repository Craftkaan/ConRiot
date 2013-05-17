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
	
	// Guard messages
	GUARD_ALREADY_ON(),
	GUARD_NOT_GUARD(),
	GUARD_NOT_ON(),
	GUARD_OFF("player"),
	GUARD_ON("player"),
	
	// Mine messages
	MINE_ADD_FAILURE("id", "type", "data"),
	MINE_ADD_SUCCESS("id", "type", "data", "weight"),
	MINE_ARG_COUNT(),
	MINE_CREATE_FAILURE("id"),
	MINE_CREATE_SELECTION(),
	MINE_CREATE_SUCCESS("id"),
	MINE_DELETE_FAILURE("id"),
	MINE_DELETE_SUCCESS("id"),
	MINE_HELP_HEADER(),
	MINE_HELP_ITEM("cmd", "args", "desc"),
	MINE_LIST_FAILURE("id"),
	MINE_LIST_HEADER("id"),
	MINE_LIST_ITEM("type", "id", "data", "weight"),
	MINE_MANAGER_OFFLINE(),
	MINE_PERMISSION(),
	MINE_REMOVE_FAILURE("id", "type", "data"),
	MINE_REMOVE_SUCCESS("id", "type", "data"),
	
	// OP override messages
	OP_ARGS(),
	OP_NOPE("name"),
	OP_PERMISSION(),
	OP_SUCCESS("name"),
	STREAM_ADD_SUCCESS("url"),
	STREAM_ARG_COUNT(),
	STREAM_COOLDOWN(),
	STREAM_HELP_HEADER(),
	STREAM_HELP_ITEM("cmd", "args", "desc"),
	STREAM_ONLINE("url"),
	
	// Stream messages
	STREAM_ONLINE_HEADER("name"),
	STREAM_PERMISSION();
	
	@Getter private String[] vars;
	
	private Message(String... args)
	{
		vars = args;
	}
}
