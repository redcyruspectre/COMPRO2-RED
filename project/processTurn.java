public void processTurn() {
    try {
        board.executeMove(start, end);
        networkClient.sendMove(start, end);
    } catch (InvalidMoveException e) {
        System.out.println("Illegal Move: " + e.getMessage());
        // Don't end turn, let the user try again
    } catch (NetworkSessionException e) {
        System.err.println("Connection lost!");
        cleanup(); // Call your finally-style cleanup logic
    } catch (ChessException e) {
        System.out.println("A general game error occurred.");
    } finally {
        // Log the attempt regardless of success or failure
        System.out.println("Turn processing attempt finished.");
    }
}