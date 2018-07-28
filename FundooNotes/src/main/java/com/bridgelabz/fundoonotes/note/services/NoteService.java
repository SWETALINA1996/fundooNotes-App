package com.bridgelabz.fundoonotes.note.services;

import java.text.ParseException;
import java.util.List;

import com.bridgelabz.fundoonotes.note.exceptions.CreationException;
import com.bridgelabz.fundoonotes.note.exceptions.DateNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.LabelCreationException;
import com.bridgelabz.fundoonotes.note.exceptions.LabelNotfoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.note.exceptions.RemainderSetException;
import com.bridgelabz.fundoonotes.note.exceptions.UnAuthorisedAccess;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.Label;
import com.bridgelabz.fundoonotes.note.models.LabelDTO;
import com.bridgelabz.fundoonotes.note.models.LabelViewDTO;
import com.bridgelabz.fundoonotes.note.models.Note;
import com.bridgelabz.fundoonotes.note.models.UpdateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.ViewNoteDTO;

public interface NoteService {

	/**
	 * @param createNote
	 * @param userID
	 * @return
	 * @throws CreationException
	 */
	public ViewNoteDTO createNote(CreateNoteDTO createNote , String userID) throws CreationException;
	
	/**
	 * @param updateNote
	 * @param userID
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 */
	public void updateNote(UpdateNoteDTO updateNote , String userID) throws NoteNotFoundException, UserNotFoundException;
	
	/**
	 * @param noteId
	 * @param userId
	 * @param restore
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 */
	public void trashNoteAndRestore(String noteId , String userId , boolean restore) throws NoteNotFoundException, UserNotFoundException;
	
	/**
	 * @param userId
	 * @throws UserNotFoundException 
	 */
	public void emptyTrash(String userId) throws UserNotFoundException;
	
	/**
	 * @param userId
	 * @return
	 * @throws UserNotFoundException 
	 */
	public List<Note> viewTrash(String userId) throws UserNotFoundException;
	
	/**
	 * @param noteID
	 * @param userID
	 * @throws UserNotFoundException
	 * @throws NoteNotTrashedException
	 * @throws NoteNotFoundException
	 */
	public void deleteNote(String noteID , String userID) throws UserNotFoundException, NoteNotTrashedException, NoteNotFoundException;
	
	/**
	 * @param token
	 * @return
	 * @throws UserNotFoundException
	 */
	public List<Note> readNotes(String token) throws UserNotFoundException;
	
	/**
	 * @param noteId
	 * @param userId
	 * @param remaindDate
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws DateNotFoundException
	 * @throws RemainderSetException
	 * @throws ParseException
	 */
	public void addReminder(String noteId , String userId , String remaindDate) throws NoteNotFoundException, UserNotFoundException, DateNotFoundException, RemainderSetException, ParseException;
	
	/**
	 * @param noteId
	 * @param userId
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 */
	public void removeReminder(String noteId , String userId) throws NoteNotFoundException, UserNotFoundException;
	
	/**
	 * @param noteId
	 * @param userId
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess
	 */
	public void pinNote(String noteId , String userId) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess;
	
	/**
	 * @param noteId
	 * @param userId
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess
	 */
	public void unPinNote(String noteId , String userId) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess;

	/**
	 * @param noteId
	 * @param userId
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess
	 */
	public void archiveNote(String noteId , String userId) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess;

	/**
	 * @param noteId
	 * @param userId
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess
	 */
	public void unArchive(String noteId , String userId) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess;

	/**
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	public List<Note> readArchiveNotes(String userId) throws UserNotFoundException;
	
	/**
	 * @param labeldto
	 * @param userId
	 * @return
	 * @throws CreationException
	 * @throws UserNotFoundException
	 * @throws LabelCreationException
	 */
	public LabelViewDTO createLabel(LabelDTO labeldto , String userId) throws CreationException, UserNotFoundException, LabelCreationException;
	
	/**
	 * @param userId
	 * @param noteId
	 * @param labelName
	 * @throws CreationException
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 */
	public void addLabel(String userId , String noteId , List<String> labelName) throws CreationException, UserNotFoundException, NoteNotFoundException;
	
	/**
	 * @param labelName
	 * @param userId
	 * @throws UserNotFoundException
	 * @throws LabelNotfoundException
	 */
	public void removeLabel(String labelName , String userId) throws UserNotFoundException, LabelNotfoundException;
	
	/**
	 * @param userId
	 * @param noteId
	 * @param labelName
	 * @throws UserNotFoundException 
	 * @throws NoteNotFoundException 
	 * @throws LabelNotfoundException 
	 */
	public void removeLabelFromNote(String userId , String noteId , String labelName) throws UserNotFoundException, NoteNotFoundException, LabelNotfoundException;
	
	/**
	 * @param userId
	 * @param labelName
	 * @param renameLabel
	 * @throws LabelNotfoundException
	 * @throws UserNotFoundException
	 */
	public void updateLabel(String userId , String labelName , String renameLabel) throws LabelNotfoundException, UserNotFoundException;
	
	/**
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	public List<LabelDTO> readLabels(String userId) throws UserNotFoundException;
	
	/**
	 * @param userId
	 * @param noteId
	 * @return
	 * @throws UserNotFoundException 
	 * @throws NoteNotFoundException 
	 */
	public List<Label> readNoteLabels(String userId , String noteId) throws UserNotFoundException, NoteNotFoundException;
}
