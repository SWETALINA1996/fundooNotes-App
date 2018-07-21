package com.bridgelabz.fundoonotes.note.controllers;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.note.exceptions.CreationException;
import com.bridgelabz.fundoonotes.note.exceptions.DateNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.note.exceptions.RemainderSetException;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.UpdateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.ViewNoteDTO;
import com.bridgelabz.fundoonotes.note.services.NoteService;
import com.bridgelabz.fundoonotes.user.models.Response;

@RestController
@RequestMapping("/notes")
public class NoteController {
	
	@Autowired
	private NoteService noteService;
	
	/**
	 * @param createNote
	 * @param token
	 * @return
	 * @throws CreationException
	 */
	@RequestMapping(value = "/create-note", method = RequestMethod.POST)
	public ResponseEntity<ViewNoteDTO> create(@RequestBody CreateNoteDTO createNote , HttpServletRequest req) throws CreationException{
		String token = req.getHeader("token");
		ViewNoteDTO viewNote = noteService.createNote(createNote, token);
		
		return new ResponseEntity<>(viewNote, HttpStatus.OK);
	}
	
	/**
	 * @param updateNote
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "/update-note", method = RequestMethod.PUT)
	public ResponseEntity<Response> update(@RequestBody UpdateNoteDTO updateNote , HttpServletRequest req) throws NoteNotFoundException, UserNotFoundException{
		
		String token = req.getHeader("token");
		noteService.updateNote(updateNote, token);
		Response dto = new Response();
		dto.setMessage("Successfully updated Note..");
		dto.setStatus(10);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/**
	 * @param noteId
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "/trash-note" , method = RequestMethod.PUT)
	public ResponseEntity<Response> trashNote(@RequestParam String noteId ,  HttpServletRequest req) throws NoteNotFoundException, UserNotFoundException {
		
		String token = req.getHeader("token");
		noteService.trashNote(noteId, token);
		
		Response dto = new Response();
		dto.setMessage("Successfully trashed Note..");
		dto.setStatus(10);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/**
	 * @param noteId
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws NoteNotTrashedException 
	 */
	@RequestMapping(value = "/deleteNote" , method = RequestMethod.DELETE)
	public ResponseEntity<Response> deleteNote(@RequestParam String noteId , HttpServletRequest req) throws NoteNotFoundException, UserNotFoundException, NoteNotTrashedException {
		
		String token = req.getHeader("token");
		noteService.deleteNote(noteId, token);
		
		Response dto = new Response();
		dto.setMessage("Successfully deleted Note..");
		dto.setStatus(10);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/**
	 * @param token
	 * @return
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "/get-all-notes", method = RequestMethod.GET)
	public ResponseEntity<List<ViewNoteDTO>> viewAll( HttpServletRequest req) throws UserNotFoundException{
		
		String token = req.getHeader("token");
		List<ViewNoteDTO> viewNote = noteService.readNotes(token);
		
		return new ResponseEntity<>(viewNote, HttpStatus.OK);
	}
	
	/**
	 * @param noteId
	 * @param remindDate
	 * @param req
	 * @return
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws DateNotFoundException
	 * @throws RemainderSetException
	 */
	@RequestMapping(value = "/reminder", method = RequestMethod.POST)
	public ResponseEntity<Response> setReminder(@RequestParam String noteId, @RequestParam Date remindDate, HttpServletRequest req) throws UserNotFoundException, NoteNotFoundException, DateNotFoundException, RemainderSetException{
		
		String token = req.getHeader("token");
		noteService.addReminder(noteId, token, remindDate);
		Response dto = new Response();
		dto.setMessage("Successfully added to reminder");
		dto.setStatus(11);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	/**
	 * @param noteId
	 * @param remindDate
	 * @param req
	 * @return
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws DateNotFoundException
	 * @throws RemainderSetException
	 */
	@RequestMapping(value = "/delete-reminder", method = RequestMethod.PUT)
	public ResponseEntity<Response> deleteReminder(@RequestParam String noteId, HttpServletRequest req) throws UserNotFoundException, NoteNotFoundException, DateNotFoundException, RemainderSetException{
		
		String token = req.getHeader("token");
		noteService.removeReminder(noteId, token);
		Response dto = new Response();
		dto.setMessage("Successfully removed from reminder");
		dto.setStatus(12);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
}
