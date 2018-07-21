package com.bridgelabz.fundoonotes.note.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.fundoonotes.note.models.Note;
import com.bridgelabz.fundoonotes.note.models.ViewNoteDTO;

public interface NoteRepository extends MongoRepository<Note , String>{

	public List<ViewNoteDTO> findAllByUserId(String userId); 
}
