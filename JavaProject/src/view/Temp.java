package view;

import java.awt.Frame;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.attribute.standard.Finishings;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Temp {

    private Popup popup;

    public Temp () {
        Display d = new Display();
        final Shell shell = new Shell(d);
        final Button b = new Button(shell, SWT.PUSH);
        b.setText("test");
        b.addListener(SWT.MouseEnter, new Listener() {
            public void handleEvent(Event e) {
                callPopUp(e,shell);
            }
        });
        b.addListener(SWT.MouseExit, new Listener() {
            public void handleEvent(Event e) {
                hidePopUp();
            }
        });
        b.pack();
        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!d.readAndDispatch()) {
                d.sleep();
            }
        }
        d.dispose();
    }

    private void hidePopUp() {
        if(popup != null){
            popup.hide();
        }
    }

    private void callPopUp(Event e, Shell shell) {
        Composite composite = new Composite(shell, SWT.EMBEDDED| SWT.NO_BACKGROUND);
        Frame frame = SWT_AWT.new_Frame(composite);
        JPanel p = new JPanel();
        frame.add(p);
        popup = PopupFactory.getSharedInstance().getPopup(p, new JLabel("Howdy"), shell.getBounds().x + 20,shell.getBounds().y+20);
        popup.show();
    }
    
	private static String findIP(String in) {
		  Matcher m = Pattern.compile("((\\d+\\.){3}\\d+):(\\d+)").matcher(in);
		  if (m.find()) {
		    String[] p = m.group(1).split("\\.");
		    for (int i = 0; i < 4; i++)
		      if (Integer.parseInt(p[i]) > 255) return null;
		    if (Integer.parseInt(m.group(3)) > 65535) return null;
		    return m.group(0);
		  }
		  return null;
	}

    public static void main(String[] args) {
        //new Temp (); 
        System.out.println(findIP("10.0.1.1:40"));
        
        
    }
}