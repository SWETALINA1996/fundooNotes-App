package com.bridgelabz.fundoonotes.note.models;

import java.util.Date;

public class CreateNoteDTO {

	private String title;
	
	private String description;
	
	private String color;

	private Date createdAt;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
	@Override
	public String toString() {
		return "CreateNoteDTO [title=" + title + ", description=" + description + "]";
	}
	
}
