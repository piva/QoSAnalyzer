package main.model;

public enum Label {
	START,
	END,
	ACTIVITY,
	EXCLUSIVE_SPLIT,
	EXCLUSIVE_JOIN,
	INCLUSIVE_SPLIT,
	INCLUSIVE_JOIN,
	FORK_SPLIT,
	FORK_JOIN;
	
	public boolean isJoinGateway(){
		return this == FORK_JOIN || this == INCLUSIVE_JOIN || this == EXCLUSIVE_JOIN;
	}
	
	public boolean isSplitGateway(){
		return this == FORK_SPLIT || this == INCLUSIVE_SPLIT || this == EXCLUSIVE_SPLIT;
	}
	
	public boolean isGateway(){
		return this != START && this != END && this != ACTIVITY;
	}
}
