package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldEmail extends JTextField {
    private String placeholder = "exemplo@email.com";
    private Color placeholderColor = Color.GRAY;
    public Color originalColor;
    private Color errorColor = new Color(255, 100, 100);
    private boolean isValid = true;

    public JTextFieldEmail() {
        this.placeholder = "exemplo@email.com";
        this.originalColor = getForeground();
        setText(placeholder);
        setForeground(placeholderColor);
        setToolTipText("exemplo@email.com");

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
                    isValid = true;
                    setBorder(null);
                } else {
                    validateEmail();
                }
            }
        });
    }

    private void validateEmail() {
        String email = getRealText();
        isValid = isValidEmail(email);

        if (!isValid) {
            setBorder(BorderFactory.createLineBorder(errorColor, 2));
        } else {
            setBorder(null);
        }
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return false;
        }
        if (atIndex == 0) {
            return false;
        }
        if (atIndex == email.length() - 1) {
            return false;
        }
        return true;
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

    public boolean isEmailValid() {
        if (getText().equals(placeholder)) {
            return false;
        }
        return isValid;
    }
}
