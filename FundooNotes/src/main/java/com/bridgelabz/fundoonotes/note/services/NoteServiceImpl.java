package com.bridgelabz.fundoonotes.note.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.bridgelabz.fundoonotes.note.exceptions.UnAuthorisedAccess;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.Label;
import com.bridgelabz.fundoonotes.note.models.LabelDTO;
import com.bridgelabz.fundoonotes.note.models.Note;
import com.bridgelabz.fundoonotes.note.models.UpdateNoteDTO;
import com.bridgelabz.fundoonotes.note.models.ViewNoteDTO;
import com.bridgelabz.fundoonotes.note.repositories.LabelRepo;
import com.bridgelabz.fundoonotes.note.repositories.LabelRepository;
import com.bridgelabz.fundoonotes.note.repositories.NoteRepo;
import com.bridgelabz.fundoonotes.note.repositories.NoteRepository;
import com.bridgelabz.fundoonotes.note.utility.Utility;
import com.bridgelabz.fundoonotes.user.models.User;
import com.bridgelabz.fundoonotes.user.repositories.UserESRepository;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private NoteRepo noteRepo;

	/*@Autowired
	private UserRepository userRepository;*/
	
	@Autowired
	private UserESRepository userRepo;

	@Autowired
	private LabelRepository labelRepository;
	
	@Autowired
	private LabelRepo labelRepo;

	@Autowired
	private ModelMapper modelMapper;

	/***********************************************
	 * create
	 ***********************************************************************************/
	@Override
	public ViewNoteDTO createNote(CreateNoteDTO createNote, String userId) throws CreationException {

		Utility.isNoteValidate(createNote);

		Note note = new Note();
		note.setTitle(createNote.getTitle());
		note.setDescription(createNote.getDescription());
		note.setColor(createNote.getColor());
		Date createdDate = new Date();
		note.setCreatedAt(createdDate);
		note.setUpdatedAt(createdDate);
		note.setUserId(userId);

		if (note.getColor() == null) {
			note.setColor(createNote.getColor());
		}

		if (createNote.getReminder() != null) {
			note.setReminder(createNote.getReminder());
		}

		note.setPin(createNote.isPin());
		note.setArchive(createNote.isArchive());

		List<String> labelList = createNote.getLabelList();
		// List<String> listOfLabels = new ArrayList<>();
		List<Label> listOfLabels = new ArrayList<>();

		//List<LabelDTO> userLabels = labelRepository.findAllByUserId(userId);
		List<LabelDTO> userLabels = labelRepo.findAllByUserId(userId);
		
		List<String> userLabelList = new ArrayList<>();
		for (int j = 0; j < labelList.size(); j++) {
			userLabelList.add(userLabels.get(j).getLabelName());
		}
		for (int i = 0; i < labelList.size(); i++) {
			if (labelList.get(i) != null || !labelList.get(i).isEmpty()) {

				if (!userLabelList.contains(labelList.get(i))) {
					Label label = new Label();
					label.setLabelName(labelList.get(i));
					label.setUserId(userId);

					labelRepository.save(label);
					labelRepo.save(label);

					//List<Label> optionalLabelAfterSave = labelRepository.findAllByLabelName(labelList.get(i));
					List<Label> optionalLabelAfterSave = labelRepo.findAllByLabelName(labelList.get(i));
					
					for (int k = 0; k < optionalLabelAfterSave.size(); k++) {
						if (optionalLabelAfterSave.get(k).getUserId().equals(userId)) {
							Label viewLabelToSave = new Label();
							viewLabelToSave.setLabelName(optionalLabelAfterSave.get(k).getLabelName());
							listOfLabels.add(viewLabelToSave);
						}
					}

				} else {
					//List<Label> optionalLabelToSave = labelRepository.findAllByLabelName(labelList.get(i));
					List<Label> optionalLabelToSave = labelRepo.findAllByLabelName(labelList.get(i));
					
					for (int j = 0; j < optionalLabelToSave.size(); j++) {
						if (optionalLabelToSave.get(j).getUserId().equals(userId)) {
							Label viewLabel = new Label();
							viewLabel.setLabelName(optionalLabelToSave.get(j).getLabelName());
							listOfLabels.add(viewLabel);
						}
					}

				}

			}
		}

		note.setLabelList(listOfLabels);

		noteRepository.save(note);
		
		noteRepo.save(note);

		ViewNoteDTO viewNote = modelMapper.map(note, ViewNoteDTO.class);

		return viewNote;

	}

	/*************************************************************
	 * update
	 * @throws UnAuthorisedAccess 
	 **********************************************************************************/
	@Override
	public void updateNote(UpdateNoteDTO updateNote, String userId)
			throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<User> optional = userRepo.findById(userId);
		//Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		Optional<Note> optionalNote = noteRepo.findById(updateNote.getId());
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note not present");
		}

		if (!userId.equals(optionalNote.get().getUserId())) {
			throw new UnAuthorisedAccess("User doesnot have the note");
		}

		Note note = optionalNote.get();

		if (note.isTrash()) {
			throw new NoteNotFoundException("Note not present..Please check your trash");
		}

		Date updatedDate = new Date();
		updateNote.setUpdatedDate(updatedDate);

		note.setTitle(updateNote.getTitle());
		note.setDescription(updateNote.getDescription());
		note.setUpdatedAt(updateNote.getUpdatedDate());

		noteRepository.save(note);
		noteRepo.save(note);

	}

	/******************************************
	 * trash
	 * @throws UnAuthorisedAccess 
	 *********************************************************************************/
	@Override
	public void trashNoteAndRestore(String noteId, String userId, boolean restore)
			throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);
		Note note = optionalNote.get();

		note.setTrash(restore);

		noteRepository.save(note);
		noteRepo.save(note);

	}

	/**********************************************************
	 * emptyTrash
	 * 
	 * @throws UserNotFoundException
	 ************************************************************************/
	@Override
	public void emptyTrash(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		//List<Note> notes = noteRepository.findAllByUserId(optional.get().getUserId());
		//List<Note> notes = noteRepo.findAllByUserId(optional.get().getUserId());
		List<Note> notes = noteRepo.findAllByUserIdAndIsTrash(optional.get().getUserId(), true);
		for (Note note : notes) {
			//if (note.isTrash())
				noteRepository.delete(note);
				noteRepo.delete(note);

		}

	}

	/***********************************************************
	 * view-trash
	 * 
	 * @throws UserNotFoundException
	 ************************************************************************/
	@Override
	public List<Note> viewTrash(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		//List<Note> notes = noteRepository.findAllByUserId(optional.get().getUserId());
		//List<Note> notes = noteRepo.findAllByUserId(optional.get().getUserId());
		List<Note> notes = noteRepo.findAllByUserIdAndIsTrash(userId, true);
		List<Note> noteList = new ArrayList<Note>();
		for (Note note : notes) {
			if (note.isArchive())
				noteList.add(note);
		}

		return noteList;
	}

	/********************************************
	 * delete
	 * @throws UnAuthorisedAccess 
	 ******************************************************************************/
	@Override
	public void deleteNote(String noteId, String userId)
			throws UserNotFoundException, NoteNotTrashedException, NoteNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);
		Note note = optionalNote.get();
		if (!note.isTrash()) {
			throw new NoteNotTrashedException("Note is not in trash");
		}

		noteRepository.deleteById(noteId);
		noteRepo.deleteById(noteId);
	}

	/***********************************************
	 * read
	 *****************************************************************************/
	@Override
	public List<Note> readNotes(String userId) throws UserNotFoundException {

	/*	Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}
*/
		//List<Note> notes = noteRepository.findAllByUserId(optional.get().getUserId());
		List<Note> notes = noteRepo.findAllByUserId(userId);
		List<Note> noteList = new ArrayList<Note>();
		for (Note note : notes) {
			if (!note.isTrash())
				noteList.add(note);
		}

		return noteList;

	}

	/***************************************
	 * add remainder
	 * @throws UnAuthorisedAccess 
	 ****************************************************************************/
	@Override
	public void addReminder(String noteId, String userId, String remindDate) throws NoteNotFoundException,
			UserNotFoundException, DateNotFoundException, RemainderSetException, ParseException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);

		if (remindDate == null) {
			throw new RemainderSetException("Please enter date");
		}

		Date reminderDate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(remindDate);

		if (reminderDate.before(new Date())) {
			throw new DateNotFoundException("Past dates cannot be recorded");
		}
		optionalNote.get().setReminder(reminderDate);

		noteRepository.save(optionalNote.get());
		noteRepo.save(optionalNote.get());
	}

	/*****************************************
	 * remove remainder
	 * @throws UnAuthorisedAccess 
	 ***********************************************************************/
	@Override
	public void removeReminder(String noteId, String userId) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);
		optionalNote.get().setReminder(null);
		noteRepository.save(optionalNote.get());
		noteRepo.save(optionalNote.get());
	}

	/******************************************
	 * PinNote
	 * @throws UnAuthorisedAccess 
	 *******************************************************************************/

	@Override
	public void pinNote(String noteId, String userId , boolean pin) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);

		Note note = optionalNote.get();
		
		if(pin) {
			note.setPin(true);
			if(note.isArchive())
				note.setArchive(false);
		}
		note.setPin(false);

		noteRepository.save(note);
		noteRepo.save(note);
	}

	/*********************************************
	 * Archive
	 ***************************************************************************/

	@Override
	public void archiveNote(String noteId, String userId , boolean archive)
			throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);

		Note note = optionalNote.get();
		if(archive) {
		note.setArchive(true);
		if(note.isPin())
			note.setPin(false);
		}
		note.setArchive(false);
		
		noteRepository.save(note);
		noteRepo.save(note);

	}
	
	/*************************************
	 * read-archive-notes
	 ************************************************************************/

	@Override
	public List<Note> readArchiveNotes(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		List<Note> notes = noteRepo.findAllByUserId(optional.get().getUserId());
		List<Note> noteList = new ArrayList<Note>();
		for (Note note : notes) {
			if (note.isArchive())
				noteList.add(note);
		}

		return noteList;
	}
	
/**************************************add-color
 * @throws UnAuthorisedAccess ***********************************************************************/
	@Override
	public void addColor(String userId, String noteId, String color) throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);

		Note note = optionalNote.get();
		note.setColor(color);
		
		noteRepository.save(note);
		noteRepo.save(note);
	}

	
	/*********************************************************************************************************************************
	 * @param noteId
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 * @throws UnAuthorisedAccess 
	 *********************************************************************************************************************************/
	public Optional<Note> isValidRequest(String noteId, String userId)
			throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		if (noteId == null) {
			throw new NoteNotFoundException("NoteId required");
		}

		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}
		
		//Optional<Note> optionalNote = noteRepository.findById(noteId);
		Optional<Note> optionalNote = noteRepo.findById(noteId);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note not present");
		}

		if (!userId.equals(optionalNote.get().getUserId())) {
			throw new UnAuthorisedAccess("User doesnot have the note");
		}
		return optionalNote;
	}

}
