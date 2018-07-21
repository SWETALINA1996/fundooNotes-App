package com.bridgelabz.fundoonotes.note.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.note.exceptions.CreationException;
import com.bridgelabz.fundoonotes.note.exceptions.DateNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonotes.note.exceptions.RemainderSetException;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.Note;
import com.bridgelabz.fundoonotes.note.models.UpdateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.ViewNoteDTO;
import com.bridgelabz.fundoonotes.note.repositories.NoteRepository;
import com.bridgelabz.fundoonotes.note.utility.Utility;
import com.bridgelabz.fundoonotes.user.models.User;
import com.bridgelabz.fundoonotes.user.repositories.UserRepository;
import com.bridgelabz.fundoonotes.user.security.JWTtokenProvider;
import com.google.common.base.Preconditions;


@Service
public class NoteServiceImpl implements NoteService{

	@Autowired
	private NoteRepository noteRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JWTtokenProvider tokenProvider;
	
	/***************************************create*************************************************************/
	@Override
	public ViewNoteDTO createNote(CreateNoteDTO createNote, String token) throws CreationException {
		
		String userId = tokenProvider.parseJWT(token);
		
		Utility.isNoteValidate(createNote);
		
		Note note = new Note();
		note.setTitle(createNote.getTitle());
		note.setDescription(createNote.getDescription());	
		note.setColor(createNote.getColor());
		Date createdDate = new Date();
		note.setCreatedAt(createdDate);
		note.setUpdatedAt(createdDate);
		note.setUserId(userId);
		if(note.getColor() == null) {
			note.setColor("#FFFFFF");
		}
		
		noteRepository.save(note);
		
		ModelMapper mapper = new ModelMapper(); //create bean and autowire
		ViewNoteDTO viewNote = mapper.map(note, ViewNoteDTO.class);
		
		return viewNote;
	}

	/****************************************update****************************************************************************/
	@Override
	public void updateNote(UpdateNoteDTO updateNote, String token) throws NoteNotFoundException, UserNotFoundException {
		
		String userID = tokenProvider.parseJWT(token);
		
		Optional<User> optional = userRepository.findById(userID);
		if(!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}
		
		Optional<Note> optionalNote = noteRepository.findById(updateNote.getId());
		if(!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note not present");
		}
		
		if(!userID.equals(optionalNote.get().getUserId())) {
			throw new UserNotFoundException("User doesnot have the note");
		}
		
		Note note = optionalNote.get();
		
		if(note.isTrash()) {
			throw new NoteNotFoundException("Note not present..Please check your trash");
		}
		
		Date updatedDate = new Date();
		updateNote.setUpdatedDate(updatedDate);
		
		note.setTitle(updateNote.getTitle());
		note.setDescription(updateNote.getDescription());
		note.setUpdatedAt(updateNote.getUpdatedDate());
		
		noteRepository.save(note);	 
	}

	/******************************************trash************************************************/
	@Override
	public void trashNote(String noteId, String token) throws NoteNotFoundException, UserNotFoundException {

		Optional<Note> optionalNote = isValidRequest(noteId, token);
		Note note = optionalNote.get();
		if(note.isTrash()) {
			throw new NoteNotFoundException("Note not present..Please check your trash");
		}
		note.setTrash(true);
		
	noteRepository.save(note);	
	}

	/********************************************delete**************************************************/
	@Override
	public void deleteNote(String noteId, String token) throws UserNotFoundException, NoteNotTrashedException, NoteNotFoundException {
	
		Optional<Note> optionalNote = isValidRequest(noteId, token);
		Note note = optionalNote.get();
		if(note.isTrash() == false) {
			throw new NoteNotTrashedException("Note is not in trash");
		}
		
		noteRepository.deleteById(noteId);
	}

	/***********************************************read************************************************/
	@Override
	public List<ViewNoteDTO> readNotes(String token) throws UserNotFoundException {
		
		String userID = tokenProvider.parseJWT(token);
		Optional<User> optional = userRepository.findById(userID);
		if(!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}
		
		List<ViewNoteDTO> notes = noteRepository.findAllByUserId(optional.get().getUserId());
		List<ViewNoteDTO> noteList = new ArrayList<ViewNoteDTO>();
		for(ViewNoteDTO note : notes) {
			if(!note.isTrash())
				noteList.add(note);
		}
		
		return noteList;
		
	}
	
	/***************************************add remainder*****************************************************/
	
	@Override
	public void addReminder(String noteId, String token, Date remindDate) throws NoteNotFoundException, UserNotFoundException, DateNotFoundException, RemainderSetException {
		
		Optional<Note> optionalNote = isValidRequest(noteId, token);
	
		if(remindDate == null) {
			throw new RemainderSetException("Please enter date");
		}
		if(remindDate.before(new Date())) {
		throw new DateNotFoundException("Past dates cannot be recorded");
		}
		optionalNote.get().setReminder(remindDate);
		noteRepository.save(optionalNote.get());
}
	
	/*****************************************remove remainder********************************************************/
	@Override
	public void removeReminder(String noteId, String token) throws NoteNotFoundException, UserNotFoundException {
		
		Optional<Note> optionalNote = isValidRequest(noteId, token);
		optionalNote.get().setReminder(null);
		noteRepository.save(optionalNote.get());
	}
	/**
	 * @param noteId
	 * @param token
	 * @return 
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 */
	public Optional<Note> isValidRequest(String noteId , String token) throws NoteNotFoundException, UserNotFoundException {
		

		if(noteId == null) {
			throw new NoteNotFoundException("NoteId required");
		}
		
		String userId = tokenProvider.parseJWT(token);
		
		Optional<User> optional = userRepository.findById(userId);
		if(!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}
		
		Optional<Note> optionalNote = noteRepository.findById(noteId);
		if(!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note not present");
		}
		
		return optionalNote;
	}
	
}
