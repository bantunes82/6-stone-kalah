package com.kalah.util;

import com.kalah.exception.ResourceConflictException;
import com.kalah.exception.ResourceNotFoundException;
/**
 * 
 * @author bruno.romao.antunes
 *
 */
public interface ResourceHandlingUtils {

	/**
	 * 
	 * @param entity
	 * @return
	 */
   public static <T> T entityOrNotFoundException(T entity) {
      if ( entity == null  ) {
         throw new ResourceNotFoundException();
      }
      return entity;
   }
   
   /**
    * 
    * @param entity
    */
   public static <T> void  noEntityOrConflictException(T entity) {
	      if ( entity != null  ) {
	         throw new ResourceConflictException();
	      }
	   }
}
