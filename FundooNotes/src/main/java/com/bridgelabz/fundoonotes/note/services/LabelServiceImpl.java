package com.bridgelabz.fundoonotes.note.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.note.exceptions.CreationException;
import com.bridgelabz.fundoonotes.note.exceptions.LabelCreationException;
import com.bridgelabz.fundoonotes.note.exceptions.LabelNotfoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.UnAuthorisedAccess;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.note.models.Label;
import com.bridgelabz.fundoonotes.note.models.LabelDTO;
import com.bridgelabz.fundoonotes.note.models.LabelViewDTO;
import com.bridgelabz.fundoonotes.note.models.Note;
import com.bridgelabz.fundoonotes.note.repositories.LabelRepo;
import com.bridgelabz.fundoonotes.note.repositories.LabelRepository;
import com.bridgelabz.fundoonotes.note.repositories.NoteRepo;
import com.bridgelabz.fundoonotes.note.repositories.NoteRepository;
import com.bridgelabz.fundoonotes.note.utility.Utility;
import com.bridgelabz.fundoonotes.user.models.User;
import com.bridgelabz.fundoonotes.user.repositories.UserESRepository;
import com.bridgelabz.fundoonotes.user.repositories.UserRepository;

@Service
public class LabelServiceImpl implements LabelService{

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private NoteRepo noteRepo;

	@Autowired
	private UserESRepository userRepo;

	@Autowired
	private LabelRepository labelRepository;
	
	@Autowired
	private LabelRepo labelRepo;

	@Autowired
	private ModelMapper modelMapper;

	
	/**************************************
	 * create-label
	 *********************************************************************************/
	@Override
	public LabelViewDTO createLabel(LabelDTO labelDto, String userId)
			throws CreationException, UserNotFoundException, LabelCreationException {

		Utility.isLabelValidate(labelDto);

		Optional<User> optionalUser = userRepo.findById(userId);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		Optional<Label> optional = labelRepo.findByLabelNameAndUserId(labelDto.getLabelName(), userId);
		if (optional.isPresent()) {
			throw new LabelCreationException("Label already exists");
		}
		
		Label label = new Label();
		label.setLabelName(labelDto.getLabelName());
		label.setUserId(userId);

		labelRepository.save(label);
		labelRepo.save(label);

		LabelViewDTO viewLabel = modelMapper.map(label, LabelViewDTO.class);

		return viewLabel;
	}

	/**************************************
	 * add-label
	 * @throws UserNotFoundException 
	 * @throws NoteNotFoundException 
	 * @throws UnAuthorisedAccess 
	 * @throws LabelNotfoundException 
	 **********************************************************************************/
	@Override
	public void addLabel(String userId, String noteId, String labelName) throws UserNotFoundException, NoteNotFoundException, UnAuthorisedAccess, LabelNotfoundException {
		
		Optional<User> optionalUser = userRepo.findById(userId);
		
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		Optional<Note> optionalNote = noteRepo.findById(noteId);
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note is not present");
		}
		
		if (!userId.equals(optionalNote.get().getUserId())) {
			throw new UnAuthorisedAccess("User doesnot have the note");
		}
		
		List<LabelDTO> listOfLabels = labelRepo.findAllByUserId(userId);
		List<Label> newLabelList;
		List<Label> labelList = new ArrayList<Label>();
		Optional<Label> optionalLabel = labelRepo.findByLabelNameAndUserId(labelName, userId);
		if(!optionalLabel.isPresent()) {
			throw new LabelNotfoundException("Label is not present");
		}
		
		for (int i = 0; i < labelList.size(); i++) {
			labelList.add(optionalNote.get().getLabelList().get(i));
		}
		
		if(labelList == null) {
			newLabelList = new ArrayList<Label>();
			
			
		}
		
	}
	/*public void addLabel(String userId, String noteId, List<String> labelName)
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
			//Optional<Label> optionalLabel = labelRepository.findByLabelNameAndUserId(userId, labelName.get(i));
			Optional<Label> optionalLabel = labelRepo.findByLabelNameAndUserId(userId, labelName.get(i));
			
			if (!optionalLabel.isPresent()) {
				Label label = new Label();
				label.setLabelName(labelName.get(i));
				label.setUserId(userId);

				labelRepository.save(label);
				labelRepo.save(label);

				List<Label> labelList = optionalNote.get().getLabelList();
				if (labelList == null) {
					List<Label> newLabelList = new ArrayList<Label>();
					newLabelList.add(label);
					optionalNote.get().setLabelList(newLabelList);
					noteRepository.save(optionalNote.get());
					noteRepo.save(optionalNote.get());
				}
				optionalNote.get().getLabelList().add(label);
				noteRepository.save(optionalNote.get());
				noteRepo.save(optionalNote.get());
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
			noteRepo.save(optionalNote.get());
		}

	}*/

	/**************************************
	 * remove-label
	 ******************************************************************************/
	@Override
	public void removeLabel(String labelId , String userId) throws UserNotFoundException, LabelNotfoundException {

		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		if (labelId == null) {
			throw new LabelNotfoundException("LabelName required");
		}

		//Optional<Label> optionalLabel = labelRepository.findByLabelNameAndUserId(labelName, userId);
		Optional<Label> optionalLabel = labelRepo.findByLabelIdAndUserId(labelId, userId);
		if (!optionalLabel.isPresent()) {
			throw new LabelNotfoundException("Label not present");
		}

		labelRepository.deleteByLabelId(labelId);
		labelRepo.deleteByLabelId(labelId);

		List<Note> listOfNotes = new ArrayList<>();
		//listOfNotes = noteRepository.findAllByUserId(userId);
		listOfNotes = noteRepo.findAllByUserId(userId);


		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getLabelList() != null) {
				for (int j = 0; j < listOfNotes.get(i).getLabelList().size(); j++) {
					if (listOfNotes.get(i).getLabelList().get(j).getLabelName().equals(labelId)) {

						listOfNotes.get(i).getLabelList().remove(j);
						Note note = listOfNotes.get(i);
						noteRepository.save(note);
						noteRepo.save(note);
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
		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		Optional<Note> optionalNote = noteRepo.findById(noteId);
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note is not present");
		}

		Optional<Label> optionalLabel = labelRepo.findByLabelName(labelName);
		if (!optionalLabel.isPresent()) {
			throw new LabelNotfoundException("Label not present");
		}

		if (!userId.equals(optionalNote.get().getUserId())) {
			throw new NoteNotFoundException("User doesnot have the note");
		}

		Note note = optionalNote.get();
		note.getLabelList().remove(optionalLabel.get());
		
		noteRepository.save(optionalNote.get());
		noteRepo.save(optionalNote.get());
	}

	/**************************************
	 * change-label
	 *****************************************************************************/
	@Override
	public void updateLabel(String userId, String labelName, String renameLabel)
			throws LabelNotfoundException, UserNotFoundException {

		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("UserId is invalid to access note");
		}

		if (labelName == null) {
			throw new LabelNotfoundException("Label name required");
		}

		//Optional<Label> optionalLabel = labelRepository.findByLabelNameAndUserId(labelName, userId);
		Optional<Label> optionalLabel = labelRepo.findByLabelNameAndUserId(labelName, userId);
		if (!optionalLabel.isPresent()) {
			throw new LabelNotfoundException("Label not present");
		}
		List<Note> listOfNotes = new ArrayList<>();
		//listOfNotes = noteRepository.findAllByUserId(userId);
		listOfNotes = noteRepo.findAllByUserId(userId);

		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getLabelList() != null) {
				for (int j = 0; j < listOfNotes.get(i).getLabelList().size(); j++) {
					if (listOfNotes.get(i).getLabelList().get(j).getLabelName().equals(renameLabel)) {

						listOfNotes.get(i).getLabelList().get(j).setLabelName(renameLabel);

						Note note = listOfNotes.get(i);

						noteRepository.save(note);
						noteRepo.save(note);
					}
				}
			}
		}
		optionalLabel.get().setLabelName(renameLabel);
		labelRepository.save(optionalLabel.get());
		labelRepo.save(optionalLabel.get());
	}

	/*************************************
	 * view-label
	 *******************************************************************************/
	@Override
	public List<LabelDTO> readLabels(String userId) throws UserNotFoundException {

		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		//List<LabelDTO> labels = labelRepository.findAllByUserId(optional.get().getUserId());
		List<LabelDTO> labels = labelRepo.findAllByUserId(optional.get().getUserId());
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

		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			throw new UserNotFoundException("User not present");
		}

		//Optional<Note> optionalNote = noteRepository.findById(noteId);
		Optional<Note> optionalNote = noteRepo.findById(noteId);
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note is not present");
		}

		return optionalNote.get().getLabelList();
	}


	
}
