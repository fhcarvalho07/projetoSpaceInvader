import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.*;
import java.io.IOException;


public class Game {
    private Screen screen;

    public Game() {
        try {

            TerminalSize terminalSize = new TerminalSize(120, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setInitialTerminalSize(terminalSize);

            Terminal terminal = terminalFactory.createTerminal();

            this.screen = new TerminalScreen(terminal);
            this.screen.setCursorPosition(null);
            this.screen.startScreen();
            this.screen.doResizeIfNecessary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void draw() throws IOException {
        screen.clear();
        screen.refresh();
    }

    public void run() {
        try {
            draw();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
