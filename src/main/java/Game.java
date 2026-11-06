import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.*;
import java.io.IOException;


public class Game {
    private Screen screen;
    private Arena arena;

    public Game() {
        try {

            TerminalSize terminalSize = new TerminalSize(120, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setInitialTerminalSize(terminalSize);

            Terminal terminal = terminalFactory.createTerminal();
            TerminalSize size_arena = terminal.getTerminalSize();

            this.screen = new TerminalScreen(terminal);
            this.screen.setCursorPosition(null);
            this.screen.startScreen();
            this.screen.doResizeIfNecessary();

            this.arena = new Arena(size_arena.getColumns(), size_arena.getRows());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void draw() throws IOException {
        screen.clear();
        arena.draw(screen.newTextGraphics());
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
