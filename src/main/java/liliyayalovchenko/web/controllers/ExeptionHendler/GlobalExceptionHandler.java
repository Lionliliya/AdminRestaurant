package liliyayalovchenko.web.controllers.ExeptionHendler;

import liliyayalovchenko.web.exeptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DishWithOutIngredientsException.class)
    public ModelAndView handleDishWithOutIngredientsException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Requested URL=" + request.getRequestURL());
        LOGGER.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("wrongInput");
        return modelAndView;
    }

    @ExceptionHandler({OrderNotFoundException.class, MenuNotFoundException.class,
            DishNotFoundException.class, EmployeeNotFoundException.class, IngredientNotFoundException.class})
    public ModelAndView handleOrderNotFoundException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Requested URL=" + request.getRequestURL());
        LOGGER.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("404");
        return modelAndView;
    }

    @ExceptionHandler(WrongDateInputFormatException.class)
    public ModelAndView handleWrongDateInputFormatException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Requested URL=" + request.getRequestURL());
        LOGGER.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("wrongInput");
        return modelAndView;
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSQLException(HttpServletRequest request, Exception ex) {
        LOGGER.error("SQLException Occurred:: URL=" + request.getRequestURL());
        LOGGER.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "IOException occurred")
    @ExceptionHandler(IOException.class)
    public void handleIOException() {
        LOGGER.error("IOException occurred");
    }

    @ExceptionHandler({NoHandlerFoundException.class, MethodArgumentTypeMismatchException.class})
    public ModelAndView handleWrongHandleException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Requested URL=" + request.getRequestURL());
        LOGGER.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("404");
        return modelAndView;
    }

    @ExceptionHandler({RuntimeException.class})
    public ModelAndView handleRuntimeException(HttpServletRequest request, Exception ex) {
        LOGGER.error("Requested URL=" + request.getRequestURL());
        LOGGER.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
