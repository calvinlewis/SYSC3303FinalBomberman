import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.*;

public class clientbuttons implements ActionListener
{   
    
    public clientbuttons()
    {
        
    }
   
    public void actionPerformed(ActionEvent event)
    {
        JButton button = (JButton) event.getSource();
        
        String key = button.getText();
        InputStream temp = new ByteArrayInputStream(key.getBytes());
        PlayerClient.input(new InputStreamReader(temp));
        System.out.println(key);
    }
}
