package com.kalah.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Resource already exists") 
public class ResourceConflictException extends RuntimeException {
   private static final long serialVersionUID = 1L;

}
