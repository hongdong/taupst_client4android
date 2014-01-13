package com.example.taupstairs.bean;

public class Rank {

	private Long id;
	private String rankId;
	private String personId;
	private String personPhotoUrl;
	private String personNickname;
	private String personSex;
	private String rankPraise;
	private String rankRank;
	
	public static final String TB_NAME = "rank";	
	public static final int RANK_COUNT_PERPAGE = 20;
	public static final String ID = "_id";
	
	public static final String RANK_ID = "rankId";
	public static final String PERSON_ID = "personId";
	public static final String PERSON_PHOTOURL = "personPhotoUrl";
	public static final String PERSON_NICKNAME = "personNickname";
	public static final String PERSON_SEX = "personSex";
	public static final String RANK_PRAISE = "rankPraise";
	public static final String RANK_RANK = "rankRank";
	
	public Rank() {
		
	}
	
	public Rank(String rankId, String personId, String personPhotoUrl,
			String personNickname, String personSex, String rankPraise, String rankRank) {
		this.rankId = rankId;
		this.personId = personId;
		this.personPhotoUrl = personPhotoUrl;
		this.personNickname = personNickname;
		this.personSex = personSex;
		this.rankPraise = rankPraise;
		this.rankRank = rankRank;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRankId() {
		return rankId;
	}
	public void setRankId(String rankId) {
		this.rankId = rankId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getPersonPhotoUrl() {
		return personPhotoUrl;
	}
	public void setPersonPhotoUrl(String personPhotoUrl) {
		this.personPhotoUrl = personPhotoUrl;
	}
	public String getPersonNickname() {
		return personNickname;
	}
	public void setPersonNickname(String personNickname) {
		this.personNickname = personNickname;
	}
	public String getPersonSex() {
		return personSex;
	}
	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}
	public String getRankPraise() {
		return rankPraise;
	}
	public void setRankPraise(String rankPraise) {
		this.rankPraise = rankPraise;
	}
	public String getRankRank() {
		return rankRank;
	}
	public void setRankRank(String rankRank) {
		this.rankRank = rankRank;
	}
}
