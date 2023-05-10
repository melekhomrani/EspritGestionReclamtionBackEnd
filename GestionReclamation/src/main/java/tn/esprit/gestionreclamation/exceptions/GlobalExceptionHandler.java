package tn.esprit.gestionreclamation.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorObject> handleBadRequestException(BadRequestException e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(400);
        errorObject.setMessage(e.getMessage());
        errorObject.setDate(new Date());
        errorObject.setType(e.getClass().getTypeName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObject);
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<ErrorObject> handleUserNotAuthorizedException(UserNotAuthorizedException e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(401);
        errorObject.setMessage(e.getMessage());
        errorObject.setType("Unauthorized");
        errorObject.setDate(new Date());
        errorObject.setType(e.getClass().getTypeName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorObject);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorObject> handleUserNotAuthenticatedException(UserNotAuthenticatedException e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(401);
        errorObject.setMessage(e.getMessage());
        errorObject.setDate(new Date());
        errorObject.setType(e.getClass().getTypeName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorObject);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(EntityNotFoundException e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(404);
        errorObject.setMessage(e.getMessage());
        errorObject.setDate(new Date());
        errorObject.setType(e.getClass().getTypeName());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorObject);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorObject> handleForbiddenException(ForbiddenException e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(403);
        errorObject.setMessage(e.getMessage());
        errorObject.setDate(new Date());
        errorObject.setType(e.getClass().getTypeName());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorObject);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorObject> handleAlreadyExistsException(AlreadyExistsException e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(409);
        errorObject.setMessage(e.getMessage());
        errorObject.setDate(new Date());
        errorObject.setType(e.getClass().getTypeName());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorObject);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleException(Exception e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(500);
        errorObject.setMessage(e.getMessage());
        errorObject.setDate(new Date());
        errorObject.setType(e.getClass().getTypeName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorObject);
    }

}
