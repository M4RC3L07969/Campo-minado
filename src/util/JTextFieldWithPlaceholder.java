package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldWithPlaceholder extends JTextField {
    private String placeholder;
    private Color placeholderColor = Color.GRAY;
    private Color originalColor;

    public JTextFieldWithPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        this.originalColor = getForeground();
        setText(placeholder);
        setForeground(placeholderColor);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(originalColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(placeholderColor);
                }
            }
        });
    }

    public String getRealText() {
        if (getText().equals(placeholder)) {
            return "";
        }
        return getText();
    }

    @Override
    public String getText() {
        String text = super.getText();
        return text.equals(placeholder) ? "" : text;
    }
}
