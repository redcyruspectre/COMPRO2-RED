package project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import java.io.*;

public class ChessIO {
    private static final String SAVE_FILE = "gamesave.json";
    private final Gson gson;

    public ChessIO() {
 
        RuntimeTypeAdapterFactory<Piece> adapter = RuntimeTypeAdapterFactory
            .of(Piece.class, "type")
            .registerSubclass(Pawn.class, "pawn")
            .registerSubclass(Knight.class, "knight")
            .registerSubclass(Bishop.class, "bishop")
            .registerSubclass(Rook.class, "rook")
            .registerSubclass(Queen.class, "queen")
            .registerSubclass(King.class, "king");

        this.gson = new GsonBuilder()
            .registerTypeAdapterFactory(adapter)
            .setPrettyPrinting()
            .create();
    }

    public void saveGame(Piece[][] board) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            gson.toJson(board, writer);
        }
    }

    public Piece[][] loadGame() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            return gson.fromJson(reader, Piece[][].class);
        }
    }
}