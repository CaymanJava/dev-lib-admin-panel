package org.cayman.exception;


import org.jetbrains.annotations.NonNls;

public class DeleteEntityException extends RuntimeException{
    public DeleteEntityException(@NonNls String message) {
        super(message);
    }
}
