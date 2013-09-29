package com.example.simplenotepad;

import com.example.simplenotepad.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class NoteEditorActivity extends Activity {
	private EditText _noteTitleEditText;
	private EditText _noteContentEditText;
	private int _noteId;
	private NoteDbHelper _db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_editor);		
		
		_noteTitleEditText = (EditText) findViewById(R.id.noteTitleEditText);
		_noteContentEditText = (EditText) findViewById(R.id.noteContentEditText);
		
		_db = new NoteDbHelper(this);
		
		Intent intent = getIntent();
		_noteId = intent.getIntExtra(MainActivity.EXTRA_NOTE_ID, -1);
		
		// We need to display the content of an existed note.
		if (_noteId > 0) {
			Note note = _db.getNote(_noteId);
			_noteTitleEditText.setText(note.getTitle());
			_noteContentEditText.setText(note.getContent());
			setTitle(R.string.title_edit);
		}
		else {
			setTitle(R.string.title_new);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_editor_menu, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_save:
        	addOrUpdateNote();
        	finish();
        	return true;
        	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private final int addOrUpdateNote() {
    	String title = _noteTitleEditText.getText().toString();
    	String content = _noteContentEditText.getText().toString();
    	
    	int newNodeId = -1;
    	
    	if (_noteId > 0) {
    		Note note = _db.getNote(_noteId);
    		note.setTitle(title);
    		note.setContent(content);
    		note.setModified(Long.valueOf(System.currentTimeMillis()));
    		_db.updateNote(note);
    	}
    	else {
    		Note newNote = new Note(title, content);
    		newNodeId = _db.addNote(newNote);
    	}
    	
    	return newNodeId;
    }

}
