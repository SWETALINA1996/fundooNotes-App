package com.bridgelabz.fundoonotes.note.services;

import java.util.List;

import com.bridgelabz.fundoonotes.note.exceptions.CreationException;
import com.bridgelabz.fundoonotes.note.exceptions.LabelCreationException;
import com.bridgelabz.fundoonotes.note.exceptions.LabelNotfoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.UnAuthorisedAccess;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.note.models.Label;
import com.bridgelabz.fundoonotes.note.models.LabelDTO;
import com.bridgelabz.fundoonotes.note.models.LabelViewDTO;

public interface LabelService {

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
	 * @throws UnAuthorisedAccess 
	 * @throws LabelNotfoundException 
	 */
	public void addLabel(String userId , String noteId , String labelName) throws CreationException, UserNotFoundException, NoteNotFoundException, UnAuthorisedAccess, LabelNotfoundException;
	
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
