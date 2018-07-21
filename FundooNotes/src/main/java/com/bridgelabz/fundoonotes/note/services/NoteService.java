package com.bridgelabz.fundoonotes.note.services;

import java.util.Date;
import java.util.List;

import com.bridgelabz.fundoonotes.note.exceptions.CreationException;
import com.bridgelabz.fundoonotes.note.exceptions.DateNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.note.exceptions.RemainderSetException;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.UpdateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.ViewNoteDTO;

public interface NoteService {

	public ViewNoteDTO createNote(CreateNoteDTO createNote , String userID) throws CreationException;
	
	public void updateNote(UpdateNoteDTO updateNote , String userID) throws NoteNotFoundException, UserNotFoundException;
	
	public void trashNote(String noteId , String userID) throws NoteNotFoundException, UserNotFoundException;
	
	public void deleteNote(String noteID , String userID) throws UserNotFoundException, NoteNotTrashedException, NoteNotFoundException;
	
	public List<ViewNoteDTO> readNotes(String token) throws UserNotFoundException;
	
	public void addReminder(String noteId , String userId , Date remaindDate) throws NoteNotFoundException, UserNotFoundException, DateNotFoundException, RemainderSetException;
	
	public void removeReminder(String noteId , String userId) throws NoteNotFoundException, UserNotFoundException;
}
