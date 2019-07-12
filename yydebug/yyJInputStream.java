package jay.yydebug;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTextArea;
/** used to reroute standard input from a {@link javax.swing.JTextArea}.
    Feeds all read methods from listening to typed keys.
	Should not deadlock because one should generally not
    read from within the event thread.
    <p>
    While this implementation avoids Java generics, code for a generic
    version has simply been commented out.
 */
public class yyJInputStream extends InputStream implements KeyListener {
  /** line edit buffer.
    */
  protected final StringBuffer line = new StringBuffer();
  /** completed lines, ready to be read.
      Invariant: null after {@link #close}.
    */
  /// protected ArrayList<byte[]> queue = new ArrayList<byte[]>();
  protected ArrayList queue = new ArrayList();
  
  public synchronized int available () throws IOException {
	if (queue == null) throw new IOException("closed");
	/// return queue.isEmpty() ? 0 : queue.get(0).length;
	return queue.isEmpty() ? 0 : ((byte[])queue.get(0)).length;
  }

  public synchronized void close () throws IOException {
	if (queue == null) throw new IOException("closed");
	queue = null;
  }

  public synchronized int read () throws IOException {
	if (queue == null) throw new IOException("closed");
	while (queue.isEmpty())
	  try {
		wait();
	  } catch (InterruptedException ie) {
		throw new IOException("interrupted");
	  }

	/// byte[] buf = queue.get(0);
	byte[] buf = (byte[])queue.get(0);
	switch (buf.length) {
	case 0:
	  return -1;
	case 1:
	  queue.remove(0);
	  break;
	default:
	  byte[] nbuf = new byte[buf.length-1];
	  System.arraycopy(buf, 1, nbuf, 0, nbuf.length);
	  queue.set(0, nbuf); notifyAll(); // others could be waiting...
	}
	return buf[0] & 255;
  }

  public synchronized int read(byte[] b, int off, int len) throws IOException {
	if (queue == null) throw new IOException("closed");
	while (queue.isEmpty())
	  try {
		wait();
	  } catch (InterruptedException ie) {
		throw new IOException("interrupted");
	  }

	/// byte[] buf = queue.get(0);
	byte[] buf = (byte[])queue.get(0);
	if (buf.length == 0) return -1;

	if (buf.length <= len) {
	  System.arraycopy(buf, 0, b, off, buf.length);
	  queue.remove(0);
	  return buf.length;
	}
	
	System.arraycopy(buf, 0, b, off, len);
	byte[] nbuf = new byte[buf.length-len];
	System.arraycopy(buf, len, nbuf, 0, nbuf.length);
	queue.set(0, nbuf); notifyAll(); // others could be waiting...
	return len;
  }
  /** returns 0: cannot skip on a terminal.
    */
  public long skip (long len) {
    return 0;
  }
  /** this one ensures that you can only type at the end
      and implements control-C as copy and control-V as paste key if
      <tt>AWTPermission(accessClipboard)</tt> is granted.
      This is executed within the event thread.
      
      <p>BUG: paste should be based on the platform paste key;
      however, Safari does not send that one to an applet.
    */
  public void keyPressed (KeyEvent ke) {
    JTextArea ta = (JTextArea)ke.getComponent();
    int key = ke.getKeyCode();

    if (ke.isControlDown())
      try {
        switch (key) {
        case KeyEvent.VK_C: // ^C to copy
          ta.getToolkit().getSystemClipboard()
              .setContents(new StringSelection(ta.getSelectedText()), 
                 new ClipboardOwner() {
                   public void lostOwnership (Clipboard c, Transferable t) { }
                 }
              );
          ke.consume();
          break;
          
        case KeyEvent.VK_V: // ^V to paste
          String string = (String)ta.getToolkit().getSystemClipboard()
            .getContents(this).getTransferData(DataFlavor.stringFlavor);
          for (int n = 0; n < string.length(); ++ n)
            doKey(ta, string.charAt(n), true);
          ke.consume();
          break;
          
        default: // do not clear selection on control alone
          if (key < KeyEvent.VK_A || key > KeyEvent.VK_Z)
            return;
        }
      } catch (Exception e) { } // no selection, no access to clipboard, etc.

  	int pos = ta.getText().length();
	  ta.setCaretPosition(pos);
  }
  
  // BUG: Rhapsody DR2 seems to not send some keys to keyTyped()
  //	e.g. German keyboard + is dropped, but numeric pad + is processed

  public void keyTyped (KeyEvent ke) {
    JTextArea ta = (JTextArea)ke.getComponent();
    doKey(ta, ke.getKeyChar(), false);
  }
  /** process one typed or pasted character.
      The caret position is not updated for pasting.
    */
  protected void doKey (JTextArea ta, char ch, boolean paste) {
    switch (ch) {
    case '\n': case '\r':		// \n|\r -> \n, release line
      line.append('\n');
      if (paste) ta.append("\n");
      break;

    case 'D'&31:			// ^D: release line
      ta.append("^D\n"); ta.setCaretPosition(ta.getText().length());
      break;

    case '\b':			// \b: erase char, if any
      int len = line.length();
      if (len > 0) line.setLength(len-1);
      if (paste) ta.append("\b");
      return;

    case 'U'&31:			// ^U: erase line, if any
      line.setLength(0);
      ta.append("^U\n"); ta.setCaretPosition(ta.getText().length());
      // fall through
    case 'C'&31:			// ^C: ignore (used as copy key)
    case 'V'&31:			// ^V: ignore (used as paste key)
      return;

    default:
      line.append(ch);
      if (paste) ta.append(""+ch);
      return;
    }
    synchronized (this) {
      queue.add(line.toString().getBytes());
      notifyAll(); // there could be several reading threads 
    }
    line.setLength(0);
  }

  public void keyReleased (KeyEvent ke) {
  }
}
