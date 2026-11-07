import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFrame;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


public class Game {
    private boolean running = true;
    private Screen screen;
    private Arena arena;

    public Game() {
        try {

            URL resource = getClass().getClassLoader().getResource("spaceInvaders.ttf");
            File fontFile = new File(resource.toURI());
            Font font =  Font.createFont(Font.TRUETYPE_FONT, fontFile);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

            DefaultTerminalFactory factory = new DefaultTerminalFactory();

            Font loadedFont = font.deriveFont(Font.PLAIN, 25);
            AWTTerminalFontConfiguration fontConfig = AWTTerminalFontConfiguration.newInstance(loadedFont);
            factory.setTerminalEmulatorFontConfiguration(fontConfig);
            factory.setForceAWTOverSwing(true);
            factory.setInitialTerminalSize(new TerminalSize(120, 40));

            Terminal terminal = factory.createTerminal();
            ((AWTTerminalFrame)terminal).addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    e.getWindow().dispose();
                }
            });

            this.screen = new TerminalScreen(terminal);
            this.screen.setCursorPosition(null);
            this.screen.startScreen();
            this.screen.doResizeIfNecessary();

            TerminalSize size_arena = terminal.getTerminalSize();
            this.arena = new Arena(size_arena.getColumns(), size_arena.getRows());
        } catch (IOException | FontFormatException | URISyntaxException e) {
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
            while (running) {
                KeyStroke stroke = screen.pollInput();
                if (stroke != null) {
                    if (stroke.getKeyType() == KeyType.ArrowLeft) {
                        arena.setSpaceShipDirection(-1);
                    } else if (stroke.getKeyType() == KeyType.ArrowRight) {
                        arena.setSpaceShipDirection(1);
                    } else if (stroke.getKeyType() == KeyType.Character && stroke.getCharacter() == 'q') {
                        running = false;
                    } else if (stroke.getKeyType() == KeyType.EOF) {
                        running = false;
                    }
                }

                arena.moveSpaceShip();

                draw();

                Thread.sleep(30);
            }

            screen.stopScreen();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
