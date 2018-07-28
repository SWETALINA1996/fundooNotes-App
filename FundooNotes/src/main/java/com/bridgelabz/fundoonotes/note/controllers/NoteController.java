package com.bridgelabz.fundoonotes.note.controllers;


import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.bridgelabz.fundoonotes.note.models.LabelDTO;
import com.bridgelabz.fundoonotes.note.models.LabelViewDTO;
import com.bridgelabz.fundoonotes.note.models.Note;
import com.bridgelabz.fundoonotes.note.models.UpdateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.ViewNoteDTO;
import com.bridgelabz.fundoonotes.note.services.NoteService;
import com.bridgelabz.fundoonotes.user.models.Response;

@RestController
@RequestMapping("/notes")
public class NoteController {
	
	@Autowired
	private NoteService noteService;
	
	/************************************************************************************************************************************************************
	 * @param createNote
	 * @param token
	 * @return
	 * @throws CreationException
	 ************************************************************************************************************************************************************/
	
	@RequestMapping(value = "/create-note", method = RequestMethod.POST)
	public ResponseEntity<ViewNoteDTO> create(@RequestBody CreateNoteDTO createNoteDto , HttpServletRequest req , @RequestAttribute("token") String token) throws CreationException{
		String userId = (String) req.getAttribute("token");
		ViewNoteDTO viewNote = noteService.createNote(createNoteDto , userId);
		
		return new ResponseEntity<>(viewNote, HttpStatus.OK);
	}
	
	/*************************************************************************************************************************************************************
	 * @param updateNote
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 *************************************************************************************************************************************************************/
	
	@RequestMapping(value = "/update-note", method = RequestMethod.PUT)
	public ResponseEntity<Response> update(@RequestBody UpdateNoteDTO updateNote , HttpServletRequest req , @RequestAttribute("token") String token) throws NoteNotFoundException, UserNotFoundException{
		
		String userId = (String) req.getAttribute("token");
		noteService.updateNote(updateNote, userId);
		Response dto = new Response();
		dto.setMessage("Successfully updated Note..");
		dto.setStatus(10);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*************************************************************************************************************************************
	 * @param noteId
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 ************************************************************************************************************************************/
	@RequestMapping(value = "/trash-and-restore-note" , method = RequestMethod.PUT)
	public ResponseEntity<Response> trashNote(@RequestParam String noteId , @RequestParam boolean restore , HttpServletRequest req , @RequestAttribute("token") String token) throws NoteNotFoundException, UserNotFoundException {
		
		String userId = (String) req.getAttribute("token");
		noteService.trashNoteAndRestore(noteId, userId , restore);
		
		Response dto = new Response();
		dto.setMessage("Successful");
		dto.setStatus(10);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/**
	 * @param req
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "/empty-trash" , method = RequestMethod.PUT)
	public ResponseEntity<Response> trashNote(HttpServletRequest req , @RequestAttribute("token") String token) throws NoteNotFoundException, UserNotFoundException {
		
		String userId = (String) req.getAttribute("token");
		noteService.emptyTrash(userId);
		
		Response dto = new Response();
		dto.setMessage("Successfully trashed all notes");
		dto.setStatus(20);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	/**
	 * @param req
	 * @param token
	 * @return
	 * @throws CreationException
	 * @throws UserNotFoundException 
	 */
	
	@RequestMapping(value = "/view-trash", method = RequestMethod.POST)
	public ResponseEntity<List<Note>> viewTrash(HttpServletRequest req , @RequestAttribute("token") String token) throws CreationException, UserNotFoundException{
		
		String userId = (String) req.getAttribute("token");
		List<Note> note = noteService.viewTrash(userId);
		
		return new ResponseEntity<>(note, HttpStatus.OK);
	}
	/***************************************************************************************************************************
	 * @param noteId
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws NoteNotTrashedException 
	 ********************************************************************************************************************************/
	
	@RequestMapping(value = "/deleteNote" , method = RequestMethod.DELETE)
	public ResponseEntity<Response> deleteNote(@RequestParam String noteId , HttpServletRequest req , @RequestAttribute("token") String token) throws NoteNotFoundException, UserNotFoundException, NoteNotTrashedException {
		
		String userId = (String) req.getAttribute("token");
		noteService.deleteNote(noteId, userId);
		
		Response dto = new Response();
		dto.setMessage("Successfully deleted Note..");
		dto.setStatus(10);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/****************************************************************************************************************
	 * @param token
	 * @return
	 * @throws UserNotFoundException
	 **************************************************************************************************************/
	@RequestMapping(value = "/get-all-notes", method = RequestMethod.GET)
	public ResponseEntity<List<Note>> viewAll( HttpServletRequest req , @RequestAttribute("token") String token) throws UserNotFoundException{
		
		String userId = (String) req.getAttribute("token");
		List<Note> viewNote = noteService.readNotes(userId);
		System.out.println(viewNote);
		return new ResponseEntity<>(viewNote, HttpStatus.OK);
	}
	
	/*************************************************************************************************************************************************************
	 * @param noteId
	 * @param remindDate
	 * @param req
	 * @return
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws DateNotFoundException
	 * @throws RemainderSetException
	 * @throws ParseException 
	 ***********************************************************************************************************************************************************/
	
	@RequestMapping(value = "/reminder", method = RequestMethod.PUT)
	public ResponseEntity<Response> setReminder(@RequestParam String noteId, @RequestParam String remindDate, HttpServletRequest req 
			, @RequestAttribute("token") String token) throws UserNotFoundException, NoteNotFoundException, DateNotFoundException, RemainderSetException, ParseException{
		
		String userId = (String) req.getAttribute("token");
		noteService.addReminder(noteId, userId, remindDate);
		Response dto = new Response();
		dto.setMessage("Successfully added to reminder");
		dto.setStatus(11);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/******************************************************************************************************************************************************
	 * @param noteId
	 * @param remindDate
	 * @param req
	 * @return
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws DateNotFoundException
	 * @throws RemainderSetException
	 ******************************************************************************************************************************************************/
	
	@RequestMapping(value = "/delete-reminder", method = RequestMethod.PUT)
	public ResponseEntity<Response> deleteReminder(@RequestParam String noteId, HttpServletRequest req ,
			@RequestAttribute("token") String token) throws UserNotFoundException, NoteNotFoundException, DateNotFoundException, RemainderSetException{
		
		String userId = (String) req.getAttribute("token");
		noteService.removeReminder(noteId, userId);
		Response dto = new Response();
		dto.setMessage("Successfully removed from reminder");
		dto.setStatus(12);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*************************************************************************************************************************************************
	 * @param noteId
	 * @param req
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess
	 **************************************************************************************************************************************************/
	@RequestMapping(value = "/pin-note" , method = RequestMethod.PUT)
	public ResponseEntity<Response> pinNote(@RequestParam String noteId ,  HttpServletRequest req , @RequestAttribute("token") String token) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {
		
		String userId = (String) req.getAttribute("token");
		noteService.pinNote(noteId, userId);
		
		Response dto = new Response();
		dto.setMessage("Successfully pinned Note..");
		dto.setStatus(13);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*********************************************************************************************************************************************************
	 * @param noteId
	 * @param req
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess
	 **************************************************************************************************************************************************************/
	@RequestMapping(value = "/unpin-note" , method = RequestMethod.PUT)
	public ResponseEntity<Response> unpinNote(@RequestParam String noteId ,  HttpServletRequest req , @RequestAttribute("token") String token) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {
		
		String userId = (String) req.getAttribute("token");
		noteService.unPinNote(noteId, userId);
		
		Response dto = new Response();
		dto.setMessage("Successfully unpinned Note..");
		dto.setStatus(14);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	/******************************************************************************************************************************************************************
	 * @param noteId
	 * @param req
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess
	 *********************************************************************************************************************************************************************/
	@RequestMapping(value = "/archive-note" , method = RequestMethod.PUT)
	public ResponseEntity<Response> archiveNote(@RequestParam String noteId ,  HttpServletRequest req , @RequestAttribute("token") String token) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {
		
		String userId = (String) req.getAttribute("token");
		noteService.archiveNote(noteId, userId);
		
		Response dto = new Response();
		dto.setMessage("Successfully archived Note..");
		dto.setStatus(15);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	/***********************************************************************************************************************************************************************
	 * @param noteId
	 * @param req
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess
	 **********************************************************************************************************************************************************************/
	@RequestMapping(value = "/unarchive-note" , method = RequestMethod.PUT)
	public ResponseEntity<Response> unarchiveNote(@RequestParam String noteId ,  HttpServletRequest req , @RequestAttribute("token") String token) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {
		
		String userId = (String) req.getAttribute("token");
		noteService.unArchive(noteId, userId);
		
		Response dto = new Response();
		dto.setMessage("Successfully unarchive Note..");
		dto.setStatus(16);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	/****************************************************************************************************************************
	 * @param req
	 * @param token
	 * @return
	 * @throws UserNotFoundException
	 **************************************************************************************************************************/
	@RequestMapping(value = "/get-archive-notes", method = RequestMethod.GET)
	public ResponseEntity<List<Note>> viewArchiveNotes( HttpServletRequest req , @RequestAttribute("token") String token) throws UserNotFoundException{
		
		String userId = (String) req.getAttribute("token");
		List<Note> viewNote = noteService.readArchiveNotes(userId);
		
		return new ResponseEntity<>(viewNote, HttpStatus.OK);
	}
	
	/********************************************************************************************************************************
	 * @param labelDto
	 * @param req
	 * @param token
	 * @return
	 * @throws CreationException
	 * @throws LabelCreationException 
	 * @throws UserNotFoundException 
	 ********************************************************************************************************************************/
	@RequestMapping(value = "/create-label" , method = RequestMethod.POST)
	public ResponseEntity<LabelViewDTO> createLabel(@RequestBody LabelDTO labelDto , HttpServletRequest req , @RequestAttribute("token") String token) throws CreationException, UserNotFoundException, LabelCreationException{
		
		String userId = (String) req.getAttribute("token");
		LabelViewDTO viewLabel = noteService.createLabel(labelDto , userId);
		
		return new ResponseEntity<>(viewLabel , HttpStatus.OK);
	}
	
	/*********************************************************************************************************************************
	 * @param labelName
	 * @param req
	 * @param token
	 * @return
	 * @throws CreationException
	 * @throws UserNotFoundException
	 * @throws LabelNotfoundException
	 ************************************************************************************************************************************/
	@RequestMapping(value = "/remove-label" , method = RequestMethod.DELETE)
	public ResponseEntity<Response> deleteLabel(@RequestParam String labelName , HttpServletRequest req , @RequestAttribute("token") String token) throws CreationException, UserNotFoundException, LabelNotfoundException{
		
		String userId = (String) req.getAttribute("token");
		noteService.removeLabel(labelName, userId);
		
		Response dto = new Response();
		dto.setMessage("Successfully removed label..");
		dto.setStatus(17);

		return new ResponseEntity<>(dto , HttpStatus.OK);
	}
	
	/*****************************************************************************************************************************************
	 * @param labelName
	 * @param noteId
	 * @param req
	 * @param token
	 * @return
	 * @throws CreationException
	 * @throws UserNotFoundException
	 * @throws LabelNotfoundException
	 * @throws NoteNotFoundException 
	 *****************************************************************************************************************************************/
	@RequestMapping(value = "/add-label" , method = RequestMethod.POST)
	public ResponseEntity<Response> addLabel(@RequestParam String noteId , @RequestBody List<String> labelList , HttpServletRequest req , @RequestAttribute("token") String token) throws CreationException, UserNotFoundException, LabelNotfoundException, NoteNotFoundException{
		
		String userId = (String) req.getAttribute("token");
		noteService.addLabel(userId , noteId , labelList);
		
		Response dto = new Response();
		dto.setMessage("Successfully added label..");
		dto.setStatus(17);

		return new ResponseEntity<>(dto , HttpStatus.OK);
	}
	
	/*******************************************************************************************************************************************
	 * @param req
	 * @param token
	 * @return
	 * @throws UserNotFoundException
	 ******************************************************************************************************************************************/
	@RequestMapping(value = "/get-all-labels", method = RequestMethod.GET)
	public ResponseEntity<List<LabelDTO>> viewAllLabel( HttpServletRequest req , @RequestAttribute("token") String token) throws UserNotFoundException{
		
		String userId = (String) req.getAttribute("token");
		List<LabelDTO> viewLabel = noteService.readLabels(userId);
		
		return new ResponseEntity<>(viewLabel , HttpStatus.OK);
	}
	
	/**
	 * @param req
	 * @param token
	 * @param labelName
	 * @return
	 * @throws UserNotFoundException
	 * @throws LabelNotfoundException
	 */
	@RequestMapping(value = "/update-label", method = RequestMethod.PUT)
	public ResponseEntity<Response> updateLabel( HttpServletRequest req , @RequestAttribute("token") String token ,@RequestParam String labelName ,@RequestParam String renameLabel ) throws UserNotFoundException, LabelNotfoundException{
		
		String userId = (String) req.getAttribute("token");
		noteService.updateLabel(userId, labelName , renameLabel);
		
		Response dto = new Response();
		dto.setMessage("Successfully updated label..");
		dto.setStatus(18);

		return new ResponseEntity<>(dto , HttpStatus.OK);
		
	}
	
	/**
	 * @param req
	 * @param token
	 * @param noteId
	 * @param labelName
	 * @return
	 * @throws UserNotFoundException
	 * @throws LabelNotfoundException
	 * @throws NoteNotFoundException 
	 */
	@RequestMapping(value = "/remove-label-from-note", method = RequestMethod.PUT)
	public ResponseEntity<Response> removeLabelFromNote( HttpServletRequest req , @RequestAttribute("token") String token ,@RequestParam String noteId , @RequestParam String labelName ) throws UserNotFoundException, LabelNotfoundException, NoteNotFoundException{
		
		String userId = (String) req.getAttribute("token");
		noteService.removeLabelFromNote(userId, noteId, labelName);
		
		Response dto = new Response();
		dto.setMessage("Successfully removed label from note.");
		dto.setStatus(18);

		return new ResponseEntity<>(dto , HttpStatus.OK);
		
	}
	
}
