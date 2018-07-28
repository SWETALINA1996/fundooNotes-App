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
import com.bridgelabz.fundoonotes.note.repositories.LabelRepository;
import com.bridgelabz.fundoonotes.note.repositories.NoteRepository;
import com.bridgelabz.fundoonotes.note.utility.Utility;
import com.bridgelabz.fundoonotes.user.models.User;
import com.bridgelabz.fundoonotes.user.repositories.UserRepository;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LabelRepository labelRepository;

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
		List<Label> labelListF = new ArrayList<>();

		List<LabelDTO> userLabels = labelRepository.findAllByUserId(userId);

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

					List<Label> optionalLabelAfterSave = labelRepository.findAllByLabelName(labelList.get(i));

					for (int k = 0; k < optionalLabelAfterSave.size(); k++) {
						if (optionalLabelAfterSave.get(k).getUserId().equals(userId)) {
							Label viewLabelToSave = new Label();
							viewLabelToSave.setLabelName(optionalLabelAfterSave.get(k).getLabelName());
							labelListF.add(viewLabelToSave);
						}
					}

				} else {
					List<Label> optionalLabelToSave = labelRepository.findAllByLabelName(labelList.get(i));

					for (int j = 0; j < optionalLabelToSave.size(); j++) {
						if (optionalLabelToSave.get(j).getUserId().equals(userId)) {
							Label viewLabel = new Label();
							viewLabel.setLabelName(optionalLabelToSave.get(j).getLabelName());
							labelListF.add(viewLabel);
						}
					}

				}

			}
		}

		note.setLabelList(labelListF);

		noteRepository.save(note);

		ViewNoteDTO viewNote = modelMapper.map(note, ViewNoteDTO.class);

		return viewNote;

	}

	/*************************************************************
	 * update
	 **********************************************************************************/
	@Override
	public void updateNote(UpdateNoteDTO updateNote, String userId)
			throws NoteNotFoundException, UserNotFoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		Optional<Note> optionalNote = noteRepository.findById(updateNote.getId());
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note not present");
		}

		if (!userId.equals(optionalNote.get().getUserId())) {
			throw new UserNotFoundException("User doesnot have the note");
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
	}

	/******************************************
	 * trash
	 *********************************************************************************/
	@Override
	public void trashNoteAndRestore(String noteId, String userId, boolean restore)
			throws NoteNotFoundException, UserNotFoundException {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);
		Note note = optionalNote.get();

		note.setTrash(restore);

		noteRepository.save(note);
	}

	/**********************************************************
	 * emptyTrash
	 * 
	 * @throws UserNotFoundException
	 ************************************************************************/
	@Override
	public void emptyTrash(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		List<Note> notes = noteRepository.findAllByUserId(optional.get().getUserId());
		for (Note note : notes) {
			if (note.isTrash())
				noteRepository.delete(note);
		}

	}

	/***********************************************************
	 * view-trash
	 * 
	 * @throws UserNotFoundException
	 ************************************************************************/
	@Override
	public List<Note> viewTrash(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		List<Note> notes = noteRepository.findAllByUserId(optional.get().getUserId());
		List<Note> noteList = new ArrayList<Note>();
		for (Note note : notes) {
			if (note.isArchive())
				noteList.add(note);
		}

		return noteList;
	}

	/********************************************
	 * delete
	 ******************************************************************************/
	@Override
	public void deleteNote(String noteId, String userId)
			throws UserNotFoundException, NoteNotTrashedException, NoteNotFoundException {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);
		Note note = optionalNote.get();
		if (!note.isTrash()) {
			throw new NoteNotTrashedException("Note is not in trash");
		}

		noteRepository.deleteById(noteId);
	}

	/***********************************************
	 * read
	 *****************************************************************************/
	@Override
	public List<Note> readNotes(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		List<Note> notes = noteRepository.findAllByUserId(optional.get().getUserId());
		List<Note> noteList = new ArrayList<Note>();
		for (Note note : notes) {
			if (!note.isTrash())
				noteList.add(note);
		}

		return noteList;

	}

	/***************************************
	 * add remainder
	 ****************************************************************************/
	@Override
	public void addReminder(String noteId, String userId, String remindDate) throws NoteNotFoundException,
			UserNotFoundException, DateNotFoundException, RemainderSetException, ParseException {

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
	}

	/*****************************************
	 * remove remainder
	 ***********************************************************************/
	@Override
	public void removeReminder(String noteId, String userId) throws NoteNotFoundException, UserNotFoundException {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);
		optionalNote.get().setReminder(null);
		noteRepository.save(optionalNote.get());
	}

	/******************************************
	 * PinNote
	 *******************************************************************************/

	@Override
	public void pinNote(String noteId, String userId) throws NoteNotFoundException, UserNotFoundException {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);

		Note note = optionalNote.get();
		note.setPin(true);

		noteRepository.save(note);
	}

	/****************************************
	 * UnpinNote
	 *******************************************************************************/

	@Override
	public void unPinNote(String noteId, String userId)
			throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);

		Note note = optionalNote.get();
		if (!note.isPin()) {
			throw new NoteNotFoundException("Note is not present in pin");
		}
		note.setPin(false);

		noteRepository.save(note);
	}

	/*********************************************
	 * Archive
	 ***************************************************************************/

	@Override
	public void archiveNote(String noteId, String userId)
			throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);

		Note note = optionalNote.get();
		note.setArchive(true);

		noteRepository.save(note);
	}

	/*****************************************
	 * Unarchive
	 ******************************************************************************/

	@Override
	public void unArchive(String noteId, String userId)
			throws NoteNotFoundException, UserNotFoundException, UnAuthorisedAccess {

		Optional<Note> optionalNote = isValidRequest(noteId, userId);

		Note note = optionalNote.get();
		if (!note.isArchive()) {
			throw new NoteNotFoundException("Note is not archived");
		}
		note.setPin(false);

		noteRepository.save(note);
	}

	/*************************************
	 * read-archive-notes
	 ************************************************************************/

	@Override
	public List<Note> readArchiveNotes(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		List<Note> notes = noteRepository.findAllByUserId(optional.get().getUserId());
		List<Note> noteList = new ArrayList<Note>();
		for (Note note : notes) {
			if (note.isArchive())
				noteList.add(note);
		}

		return noteList;
	}

	/**************************************
	 * create-label
	 *********************************************************************************/
	@Override
	public LabelViewDTO createLabel(LabelDTO labelDto, String userId)
			throws CreationException, UserNotFoundException, LabelCreationException {

		Utility.isLabelValidate(labelDto);

		Optional<User> optionalUser = userRepository.findById(userId);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		Optional<Label> optional = labelRepository.findByLabelNameAndUserId(labelDto.getLabelName(), userId);
		if (optional.isPresent()) {
			throw new LabelCreationException("Label already exists");
		}
		
		Label label = new Label();
		label.setLabelName(labelDto.getLabelName());
		label.setUserId(userId);

		labelRepository.save(label);

		LabelViewDTO viewLabel = modelMapper.map(label, LabelViewDTO.class);

		return viewLabel;
	}

	/**************************************
	 * add-label
	 **********************************************************************************/
	@Override
	public void addLabel(String userId, String noteId, List<String> labelName)
			throws CreationException, UserNotFoundException, NoteNotFoundException {

		Optional<User> optionalUser = userRepository.findById(userId);
		// Preconditions.checkNotNull(optionalUser, "User not present");

		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note is not present");
		}

		if (!userId.equals(optionalNote.get().getUserId())) {
			throw new UserNotFoundException("User doesnot have the note");
		}

		for (int i = 0; i < labelName.size(); i++) {
			Optional<Label> optionalLabel = labelRepository.findByLabelNameAndUserId(userId, labelName.get(i));

			if (!optionalLabel.isPresent()) {
				Label label = new Label();
				label.setLabelName(labelName.get(i));
				label.setUserId(userId);

				labelRepository.save(label);

				List<Label> labelList = optionalNote.get().getLabelList();
				if (labelList == null) {
					List<Label> newLabelList = new ArrayList<Label>();
					newLabelList.add(label);
					optionalNote.get().setLabelList(newLabelList);
					noteRepository.save(optionalNote.get());
				}
				optionalNote.get().getLabelList().add(label);
				noteRepository.save(optionalNote.get());
			}

			List<Label> labelList = optionalNote.get().getLabelList();
			List<String> labelsInNote;
			if (labelList != null) {
				labelsInNote = new ArrayList<String>();
				for (int k = 0; k < labelList.size(); k++) {
					labelsInNote.add(labelList.get(k).getLabelName());
				}
				if (!labelsInNote.contains(optionalLabel.get().getLabelName())) {
					optionalNote.get().getLabelList().add(optionalLabel.get());
				}
			} else {
				List<Label> list = new ArrayList<Label>();
				list.add(optionalLabel.get());
				optionalNote.get().setLabelList(list);
			}
			noteRepository.save(optionalNote.get());
		}

	}

	/**************************************
	 * remove-label
	 ******************************************************************************/
	@Override
	public void removeLabel(String labelName, String userId) throws UserNotFoundException, LabelNotfoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		if (labelName == null) {
			throw new LabelNotfoundException("LabelName required");
		}

		Optional<Label> optionalLabel = labelRepository.findByLabelNameAndUserId(labelName, userId);
		if (!optionalLabel.isPresent()) {
			throw new LabelNotfoundException("Label not present");
		}

		labelRepository.deleteByLabelName(labelName);

		List<Note> listOfNotes = new ArrayList<>();
		listOfNotes = noteRepository.findAllByUserId(userId);

		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getLabelList() != null) {
				for (int j = 0; j < listOfNotes.get(i).getLabelList().size(); j++) {
					if (listOfNotes.get(i).getLabelList().get(j).getLabelName().equals(labelName)) {

						listOfNotes.get(i).getLabelList().remove(j);
						Note note = listOfNotes.get(i);
						noteRepository.save(note);
					}
				}
			}
		}
	}

	/************************************************************
	 * remove-label-from-note
	 * 
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 * @throws LabelNotfoundException
	 *********************************************************/

	@Override
	public void removeLabelFromNote(String userId, String noteId, String labelName)
			throws UserNotFoundException, NoteNotFoundException, LabelNotfoundException {

		// check for null
		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note is not present");
		}

		Optional<Label> optionalLabel = labelRepository.findByLabelName(labelName);
		if (!optionalLabel.isPresent()) {
			throw new LabelNotfoundException("Label not present");
		}

		if (!userId.equals(optionalNote.get().getUserId())) {
			throw new NoteNotFoundException("User doesnot have the note");
		}

		Note note = optionalNote.get();
		note.getLabelList().remove(optionalLabel.get());
		
		noteRepository.save(optionalNote.get());
	}

	/**************************************
	 * change-label
	 *****************************************************************************/
	@Override
	public void updateLabel(String userId, String labelName, String renameLabel)
			throws LabelNotfoundException, UserNotFoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		if (labelName == null) {
			throw new LabelNotfoundException("Label name required");
		}

		Optional<Label> optionalLabel = labelRepository.findByLabelNameAndUserId(labelName, userId);
		if (!optionalLabel.isPresent()) {
			throw new LabelNotfoundException("Label not present");
		}
		List<Note> listOfNotes = new ArrayList<>();
		listOfNotes = noteRepository.findAllByUserId(userId);

		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getLabelList() != null) {
				for (int j = 0; j < listOfNotes.get(i).getLabelList().size(); j++) {
					if (listOfNotes.get(i).getLabelList().get(j).getLabelName().equals(renameLabel)) {

						listOfNotes.get(i).getLabelList().get(j).setLabelName(renameLabel);

						Note note = listOfNotes.get(i);

						noteRepository.save(note);
					}
				}
			}
		}
		optionalLabel.get().setLabelName(renameLabel);
		labelRepository.save(optionalLabel.get());
	}

	/*************************************
	 * view-label
	 *******************************************************************************/
	@Override
	public List<LabelDTO> readLabels(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		List<LabelDTO> labels = labelRepository.findAllByUserId(optional.get().getUserId());
		List<LabelDTO> labelList = new ArrayList<LabelDTO>();
		for (LabelDTO label : labels) {
			labelList.add(label);
		}

		return labelList;
	}

	/****************************************************
	 * read-labels-from-note
	 * 
	 * @throws UserNotFoundException
	 * @throws NoteNotFoundException
	 **************************************************************/
	@Override
	public List<Label> readNoteLabels(String userId, String noteId)
			throws UserNotFoundException, NoteNotFoundException {

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note is not present");
		}

		return optionalNote.get().getLabelList();
	}

	/*********************************************************************************************************************************
	 * @param noteId
	 * @param token
	 * @return
	 * @throws NoteNotFoundException
	 * @throws UserNotFoundException
	 *********************************************************************************************************************************/
	public Optional<Note> isValidRequest(String noteId, String userId)
			throws NoteNotFoundException, UserNotFoundException {

		if (noteId == null) {
			throw new NoteNotFoundException("NoteId required");
		}

		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		Optional<Note> optionalNote = noteRepository.findById(noteId);
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note not present");
		}

		return optionalNote;
	}

}
