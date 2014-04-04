import java.awt.event.*;
import javax.swing.*;


public class EndGame implements ActionListener
{   
    
    public EndGame()
    {
        
    }

    public void actionPerformed(ActionEvent event)
    {
        
        Server.Playing = false;
        System.out.printf("Game Over\n");
        
        
    }
}
