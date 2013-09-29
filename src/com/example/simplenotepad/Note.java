package com.example.simplenotepad;

public class Note {

	int	_id;
	String	_title;
	String	_content;
	long	_created;
	long	_modified;
	
	public Note() {
		_created = System.currentTimeMillis();
		_modified = _created;
	}
	
	public Note(String title, String content) {
		_title = title;
		_content = content;
		_created = System.currentTimeMillis();
		_modified = _created;
	}
	
	public Note(int id, String title, String content) {
		_id = id;
		_title = title;
		_content = content;
		_created = System.currentTimeMillis();
		_modified = _created;
	}
	
	public Note(int id, String title, String content, long created, long modified) {
		_id = id;
		_title = title;
		_content = content;
		_created = created;
		_modified = modified;
	}
	
	public int getId() {
		return _id; 
	}
	
	public String getTitle() {
		return _title;
	}
	
	public void setTitle(String title) {
		_title = title;
	}
	
	public String getContent() {
		return _content;
	}
	
	public void setContent(String content) {
		_content = content;
	}
	
	public long getCreated() {
		return _created;
	}
	
	public void setCreated(long created) {
		_created = created;
	}
	
	public long getModified() {
		return _modified;
	}
	
	public void setModified(long modified) {
		_modified = modified;
	}
}
