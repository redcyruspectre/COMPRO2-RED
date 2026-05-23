package project;

public class ChessException extends Exception {
    public ChessException(String message) { 
        super(message); 
    }
}


public class InvalidMoveException extends ChessException {
    public InvalidMoveException(String message) { 
        super(message); 
    }
}

public class NetworkSessionException extends ChessException {
    public NetworkSessionException(String message) { 
        super(message); 
    }
}

public class DataPersistenceException extends ChessException {
    public DataPersistenceException(String message) { 
        super(message); 
    }
}