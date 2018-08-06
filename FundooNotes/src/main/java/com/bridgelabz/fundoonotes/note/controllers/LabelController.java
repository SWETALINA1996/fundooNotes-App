package com.bridgelabz.fundoonotes.note.controllers;

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
import com.bridgelabz.fundoonotes.note.exceptions.LabelCreationException;
import com.bridgelabz.fundoonotes.note.exceptions.LabelNotfoundException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.UnAuthorisedAccess;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.note.models.LabelDTO;
import com.bridgelabz.fundoonotes.note.models.LabelViewDTO;
import com.bridgelabz.fundoonotes.note.services.LabelService;
import com.bridgelabz.fundoonotes.user.models.Response;

@RestController
@RequestMapping("/labels")
public class LabelController {

	@Autowired
	private LabelService labelService;
	
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
	public ResponseEntity<LabelViewDTO> createLabel(@RequestBody LabelDTO labelDto , HttpServletRequest req) throws CreationException, UserNotFoundException, LabelCreationException{
		
		String userId = (String) req.getAttribute("token");
		LabelViewDTO viewLabel = labelService.createLabel(labelDto , userId);
		
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
	public ResponseEntity<Response> deleteLabel(@RequestParam String labelId , HttpServletRequest req , @RequestAttribute("token") String token) throws CreationException, UserNotFoundException, LabelNotfoundException{
		
		String userId = (String) req.getAttribute("token");
		labelService.removeLabel(labelId , userId);
		
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
	 * @return5b5c1ae1bebbe961f4c381c6
	 * @throws CreationException
	 * @throws UserNotFoundException
	 * @throws LabelNotfoundException
	 * @throws NoteNotFoundException 
	 * @throws UnAuthorisedAccess 
	 *****************************************************************************************************************************************/
	@RequestMapping(value = "/add-label" , method = RequestMethod.POST)
	public ResponseEntity<Response> addLabel(@RequestParam String noteId , @RequestBody String labelList , HttpServletRequest req , @RequestAttribute("token") String token) throws CreationException, UserNotFoundException, LabelNotfoundException, NoteNotFoundException, UnAuthorisedAccess{
		
		String userId = (String) req.getAttribute("token");
		labelService.addLabel(userId , noteId , labelList);
		
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
		List<LabelDTO> viewLabel = labelService.readLabels(userId);
		
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
	public ResponseEntity<Response> updateLabel( HttpServletRequest req , @RequestAttribute("token") String token ,@RequestParam String labelId , @RequestParam String renameLabel ) throws UserNotFoundException, LabelNotfoundException{
		
		String userId = (String) req.getAttribute("token");
		labelService.updateLabel(userId, labelId , renameLabel);
		
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
		labelService.removeLabelFromNote(userId, noteId, labelName);
		
		Response dto = new Response();
		dto.setMessage("Successfully removed label from note.");
		dto.setStatus(18);

		return new ResponseEntity<>(dto , HttpStatus.OK);
		
	}
	
}
