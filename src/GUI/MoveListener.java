package GUI;

import GUI.Model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MoveListener implements KeyListener {

    private final Model model;

    public MoveListener(Model model) {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP:
            {
                model.moveUp();
                break;
            }
            case KeyEvent.VK_DOWN:
            {
                model.moveDown();
                break;
            }
            case KeyEvent.VK_LEFT:
            {
                model.moveLeft();
                break;
            }
            case KeyEvent.VK_RIGHT:
            {
                model.moveRight();
                break;
            }
            case KeyEvent.VK_DELETE:
            {
                model.stop();
                break;
            }
            case KeyEvent.VK_SPACE:
            {
                model.swap();
                model.update();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
