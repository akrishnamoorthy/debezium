package com.example.rstream;

public class Comment {


	    private String author;
	    private String message;
	    private String timestamp;
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		public Comment(String author, String message, String timestamp) {
			super();
			this.author = author;
			this.message = message;
			this.timestamp = timestamp;
		}
	    
	    

	    //getter, setter and constructor

}
