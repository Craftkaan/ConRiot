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
	
	// Cell messages
	CELL_ADD_FAILURE("cell"),
	CELL_ADD_SUCCESS("cell"),
	CELL_ARG_COUNT(),
	CELL_DELETE("cell"),
	CELL_EXISTS("cell"),
	CELL_EXPIRED("cell"),
	CELL_EXTENDED("cell", "cost"),
	CELL_HELP_HEADER(),
	CELL_HELP_ITEM("cmd", "args", "desc"),
	CELL_LOOK_SIGN(),
	CELL_NO_BLOCK(),
	CELL_NO_REGION(),
	CELL_OWNED_BY("player"),
	CELL_PERMISSION(),
	CELL_RENTED("cell", "cost"),
	CELL_WRONG_BLOCK("region"),
	
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
	
	// SHU messages
	SHU_ANNOUNCE("name", "reason"),
	SHU_ARGS(),
	SHU_HELP_HEADER(),
	SHU_HELP_ITEM("cmd", "args", "desc"),
	SHU_MUTED(),
	SHU_NO_RECORD("name"),
	SHU_PERMISSION(),
	SHU_REASONS(),
	SHU_RECORD_HEADER("name"),
	SHU_RECORD_ITEM("entry", "value"),
	SHU_RELEASED(),
	SHU_SENT("time"),
	
	// Spot messages
	SPOT_ANNOUNCE("guard", "player", "reason"),
	SPOT_ARGS(),
	SPOT_COUNTDOWN("count"),
	SPOT_HELP_HEADER(),
	SPOT_HELP_ITEM("cmd", "args", "desc"),
	SPOT_INVALID("name"),
	SPOT_PERMISSION(),
	SPOT_WARNING("reason"),
	
	// Stream messages
	STREAM_ADD_SUCCESS("url"),
	STREAM_ARG_COUNT(),
	STREAM_COOLDOWN(),
	STREAM_HELP_HEADER(),
	STREAM_HELP_ITEM("cmd", "args", "desc"),
	STREAM_ONLINE("url"),
	STREAM_ONLINE_HEADER("name"),
	STREAM_PERMISSION();
	
	@Getter private String[] vars;
	
	private Message(String... args)
	{
		vars = args;
	}
}
