package com.org.citius.pms.user.util.exception.handler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.CountryNotFoundException;
import com.org.citius.pms.user.util.exception.FirstTimeLoginException;
import com.org.citius.pms.user.util.exception.InvalidUserPasswordException;
import com.org.citius.pms.user.util.exception.InvalidUserRegistrationDetailsException;
import com.org.citius.pms.user.util.exception.TitleNotFoundException;
import com.org.citius.pms.user.util.exception.UserAccountDeletedException;
import com.org.citius.pms.user.util.exception.UserAccountDisabledException;
import com.org.citius.pms.user.util.exception.UserAccountLockException;
import com.org.citius.pms.user.util.exception.UserAccountPasswordExpiredException;
import com.org.citius.pms.user.util.exception.UserAlreadyExistsException;
import com.org.citius.pms.user.util.exception.UserNotFoundException;
import com.org.citius.pms.user.util.exception.UserRoleNotFoundException;
import com.org.citius.pms.user.util.exception.UserStatusNotFoundException;
import com.org.citius.pms.user.util.exception.handler.response.ApplicationResponse;

@ControllerAdvice
public class CustomRestApiExceptionHandler extends ResponseEntityExceptionHandler {

	// Generic Exception
	// 400

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final List<String> errors = new ArrayList<>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errors);
		return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errors);
		return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
				+ ex.getRequiredType();

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getRequestPartName() + " part is missing";
		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getParameterName() + " parameter is missing";
		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final List<String> errors = new ArrayList<String>();
		for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());
		}

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errors);
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	// User Defined Exception
	@ExceptionHandler({ InvalidUserPasswordException.class, IllegalArgumentException.class,
			BadCredentialsException.class, InvalidUserRegistrationDetailsException.class,
			DateTimeParseException.class })
	public ResponseEntity<Object> handleInvalidDataException(final RuntimeException ex, final WebRequest request) {
		logger.info(ex.getClass().getName());

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	// 404
	// User Defined Exception
	@ExceptionHandler({ UserNotFoundException.class, UserStatusNotFoundException.class, CountryNotFoundException.class,
			UserRoleNotFoundException.class, UserStatusNotFoundException.class, TitleNotFoundException.class })
	public final ResponseEntity<Object> handleItemNotFoundException(RuntimeException ex, WebRequest request) {
		logger.info(ex.getClass().getName());

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.NOT_FOUND, HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.NOT_FOUND, HttpStatus.NOT_FOUND.value(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	// 405
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.value(),
				builder.toString());
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
	}

	// 409
	// User Defined Exception
	@ExceptionHandler({ UserAlreadyExistsException.class })
	public final ResponseEntity<Object> handleDuplicateRequestException(RuntimeException ex, WebRequest request) {
		logger.info(ex.getClass().getName());

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.CONFLICT, HttpStatus.CONFLICT.value(), ex.getLocalizedMessage());
		return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
	}

	// 415
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.UNSUPPORTED_MEDIA_TYPE,
				HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), builder.substring(0, builder.length() - 1));
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	// 422
	// User Defined Exception
	@ExceptionHandler({ FirstTimeLoginException.class, UserAccountLockException.class,
			UserAccountDisabledException.class, UserAccountDeletedException.class,
			UserAccountPasswordExpiredException.class })
	public final ResponseEntity<Object> handleUnProcessableEntityException(RuntimeException ex, WebRequest request) {
		logger.info(ex.getClass().getName());

		final ApplicationResponse apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				ex.getLocalizedMessage(), AppConstants.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.value(),
				ex.getMessage());
		return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	// 500
	@ExceptionHandler({ Exception.class, AuthenticationFailedException.class, MailAuthenticationException.class })
	public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		ApplicationResponse apiError = null;
		if (ex instanceof AuthenticationFailedException || ex instanceof MailAuthenticationException) {
			String message = "Unable to send email for password, kindly try after some time or try to change/reset password.";
			apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC), message,
					AppConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
		} else {
			apiError = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC), ex.getLocalizedMessage(),
					AppConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		}
		return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}