package com.myrole.model;

import com.google.gson.annotations.SerializedName;

public class ShareDownloadCountModel{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public boolean isStatus(){
		return status;
	}

	public static class Data{

		@SerializedName("fieldCount")
		private int fieldCount;

		@SerializedName("serverStatus")
		private int serverStatus;

		@SerializedName("protocol41")
		private boolean protocol41;

		@SerializedName("changedRows")
		private int changedRows;

		@SerializedName("affectedRows")
		private int affectedRows;

		@SerializedName("warningCount")
		private int warningCount;

		@SerializedName("message")
		private String message;

		@SerializedName("insertId")
		private int insertId;

		public int getFieldCount(){
			return fieldCount;
		}

		public int getServerStatus(){
			return serverStatus;
		}

		public boolean isProtocol41(){
			return protocol41;
		}

		public int getChangedRows(){
			return changedRows;
		}

		public int getAffectedRows(){
			return affectedRows;
		}

		public int getWarningCount(){
			return warningCount;
		}

		public String getMessage(){
			return message;
		}

		public int getInsertId(){
			return insertId;
		}
	}
}