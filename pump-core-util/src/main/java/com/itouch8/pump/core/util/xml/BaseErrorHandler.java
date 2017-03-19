package com.itouch8.pump.core.util.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class BaseErrorHandler implements ErrorHandler {

    
    private final List<SAXParseException> warns = new ArrayList<SAXParseException>();

    
    private final List<SAXParseException> errors = new ArrayList<SAXParseException>();

    
    private final List<SAXParseException> fatals = new ArrayList<SAXParseException>();

    
    private boolean throwWhenWarning = false;

    
    private boolean throwWhenError = true;

    
    private boolean throwWhenFatal = true;

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        warns.add(exception);
        if (this.isThrowWhenWarning()) {
            throw exception;
        }
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        errors.add(exception);
        if (this.isThrowWhenWarning() || this.isThrowWhenError()) {
            throw exception;
        }
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        fatals.add(exception);
        if (this.isThrowWhenWarning() || this.isThrowWhenError() || this.isThrowWhenFatal()) {
            throw exception;
        }
    }

    public boolean isThrowWhenWarning() {
        return throwWhenWarning;
    }

    public void setThrowWhenWarning(boolean throwWhenWarning) {
        this.throwWhenWarning = throwWhenWarning;
    }

    public boolean isThrowWhenError() {
        return throwWhenError;
    }

    public void setThrowWhenError(boolean throwWhenError) {
        this.throwWhenError = throwWhenError;
    }

    public boolean isThrowWhenFatal() {
        return throwWhenFatal;
    }

    public void setThrowWhenFatal(boolean throwWhenFatal) {
        this.throwWhenFatal = throwWhenFatal;
    }

    public List<SAXParseException> getWarns() {
        return warns;
    }

    public List<SAXParseException> getErrors() {
        return errors;
    }

    public List<SAXParseException> getFatals() {
        return fatals;
    }
}
