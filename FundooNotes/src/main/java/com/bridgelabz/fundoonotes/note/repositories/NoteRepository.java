package com.bridgelabz.fundoonotes.note.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.note.models.Note;
import com.bridgelabz.fundoonotes.note.models.ViewNoteDTO;

@Repository
public interface NoteRepository extends MongoRepository<Note , String>{

	//public List<ViewNoteDTO> findAllByUserId(String userId); 
	
	public List<Note> findAllByUserId(String userId); 
	
	//public Optional<Note> findByUserId(String userId);

}
