package util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static ThemeManager instance;
    private boolean isDarkMode;
    private List<JFrame> registeredWindows;

    private ThemeManager() {
        this.isDarkMode = false;
        this.registeredWindows = new ArrayList<>();
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void registerWindow(JFrame window) {
        if (!registeredWindows.contains(window)) {
            registeredWindows.add(window);
        }
    }

    public void unregisterWindow(JFrame window) {
        registeredWindows.remove(window);
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
        applyTheme();
    }

    public void applyTheme() {
        try {
            if (isDarkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }

            for (JFrame window : registeredWindows) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
