package com.example.simplenotepad;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {
	
	public final static String EXTRA_NOTE_ID = "com.example.android.notepad.NOTEID";
	
	private NoteDbHelper _db = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*
         * Sets the callback for context menu activation for the ListView. The listener is set
         * to be this Activity. The effect is that context menus are enabled for items in the
         * ListView, and the context menu is handled by a method in MainActivity.
         */
        getListView().setOnCreateContextMenuListener(this);
        
        _db = new NoteDbHelper(this);
		
        String[] dataColumns = new String[] {"title", "content"};
        int[] viewIds = new int[] { R.id.title, R.id.summary };
		
		Cursor cursor = _db.getAllNotesCursor();
		
        // Creates the backing adapter for the ListView.
        SimpleCursorAdapter adapter
            = new SimpleCursorAdapter(
                      this,                             // The Context for the ListView
                      R.layout.notelist_item,           // Points to the XML for a list item
                      cursor,                           // The cursor to get items from
                      dataColumns,
                      viewIds
              );	
        setListAdapter(adapter);
    }
    
    private void reloadNote() {
    	SimpleCursorAdapter adapter = (SimpleCursorAdapter)getListAdapter();
    	if (adapter != null) {
    		Cursor cursor = _db.getAllNotesCursor();
    		adapter.swapCursor(cursor);
    	}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	reloadNote();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_add:
        	Intent intent = new Intent(this, NoteEditorActivity.class);
        	startActivity(intent);
        	return true;
        	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * This method is called when the user clicks a note in the displayed list.
     *
     * @param l The ListView that contains the clicked item
     * @param v The View of the individual item
     * @param position The position of v in the displayed list
     * @param id The row ID of the clicked item
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, NoteEditorActivity.class);
    	intent.putExtra(EXTRA_NOTE_ID, (int)id);
    	startActivity(intent);
    }
    
    /**
     * This method is called when the user context-clicks a note in the list. MainActivity registers
     * itself as the handler for context menus in its ListView (this is done in onCreate()).
     *
     * The only available option is DELETE.
     *
     * Context-click is equivalent to long-press.
     *
     * @param menu A ContexMenu object to which items should be added.
     * @param view The View for which the context menu is being constructed.
     * @param menuInfo Data associated with view.
     * @throws ClassCastException
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }
    
    /**
     * This method is called when the user selects an item from the context menu
     * (see onCreateContextMenu()). The only menu items that are actually handled are DELETE and
     * COPY. Anything else is an alternative option, for which default handling should be done.
     *
     * @param item The selected menu item
     * @return True if the menu item was DELETE, and no default processing is need, otherwise false,
     * which triggers the default handling of the item.
     * @throws ClassCastException
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // The data from the menu item.
        AdapterView.AdapterContextMenuInfo info;

        /*
         * Gets the extra info from the menu item. When an note in the Notes list is long-pressed, a
         * context menu appears. The menu items for the menu automatically get the data
         * associated with the note that was long-pressed. The data comes from the provider that
         * backs the list.
         *
         * The note's data is passed to the context menu creation routine in a ContextMenuInfo
         * object.
         *
         * When one of the context menu items is clicked, the same data is passed, along with the
         * note ID, to onContextItemSelected() via the item parameter.
         */
        try {
            // Casts the data object in the item into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {

            // If the object can't be cast, logs an error
            Log.e("SimpleNotePad", "bad menuInfo", e);

            // Triggers default processing of the menu item.
            return false;
        }

        /*
         * Gets the menu item's ID and compares it to known actions.
         */
        switch (item.getItemId()) {
        case R.id.context_delete:
  
            int noteId = (int)info.id;
            if (noteId > 0)
            	_db.deleteNote(noteId);
            reloadNote();
            // Returns to the caller and skips further processing.
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }
}
