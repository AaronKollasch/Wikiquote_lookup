package main;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.*;

import org.apache.commons.io.IOUtils;

import javax.swing.*;
//import javax.script.*;

public class Wikiquote_window extends JPanel implements KeyListener{
	
	private JTextField textField;
	private JTextField outputField;
	//private ScriptEngine engine;
	//private Invocable inv;
	//private Object wqa;
	private static Pattern quoteResultStartPat = Pattern.compile("<li><div class='mw-search-result-heading'>");;
	private static Pattern quoteResultEndPat = Pattern.compile("</li>");
	
	public Wikiquote_window(){
		super();
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		/*ScriptEngineManager factory = new ScriptEngineManager();
		engine = factory.getEngineByName("JavaScript");*/
		
		textField = new JTextField();
		textField.setEditable(true);
		textField.addKeyListener(this);
		this.add(textField);
		
		outputField = new JTextField();
		outputField.setEditable(false);
		outputField.addKeyListener(this);
		this.add(outputField);
		
		this.setPreferredSize(new Dimension(300, 500));
		
		/*
		try {
			engine.eval(new java.io.FileReader("src/main/jquery-2.0.2.min.js"));
			engine.eval(new java.io.FileReader("src/main/wikiquote-api.js"));
			inv = (Invocable) engine;
			wqa = engine.get("wqa");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Wikiquote_window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        Wikiquote_window newContentPane = new Wikiquote_window();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        //frame.setLocation(100, 100);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	private void collect(){
		System.out.println("collect");
		String txt = textField.getText();
		try {
			String out = querySite(txt);
			outputField.setText(out);
			System.out.println(findResults(out, quoteResultStartPat, quoteResultEndPat));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String querySite(String inputString) throws IOException{
		final URL url = new URL("http://en.wikiquote.org/w/index.php?format=json&action=search"
				+ "&search="+URLEncoder.encode(inputString,"UTF-8")
				+ "&namespace=0"
				+ "&suggest=");
		final URLConnection urlConnection = url.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		urlConnection.connect();
		final OutputStream outputStream = urlConnection.getOutputStream();
		/*outputStream.write(("{\"format\": \"json\","
							+ "\"action\": \"opensearch\","
							+ "\"namespace\": 0,"
							+ "\"suggest\": \"\","
							+ "\"search\": \"" + inputString + "\"}").getBytes("UTF-8"));*/
//	{\"fNamn\": \"" + inputString + "\"}").getBytes("UTF-8"));
		outputStream.flush();
		final InputStream inputStream = urlConnection.getInputStream();
		/*byte[] b = new byte[10000];
		inputStream.read(b);*/
		String theString = IOUtils.toString(inputStream); 
		IOUtils.closeQuietly(inputStream);
		return theString;
	}
	
	public static ArrayList<String> findResults(String inputString, Pattern quoteResultStartPat, Pattern quoteResultEndPat){
		String string = new String(inputString);
		ArrayList<String> results = new ArrayList<String>();
		Matcher matcherStart = quoteResultStartPat.matcher(string);
		Matcher matcherEnd = quoteResultEndPat.matcher(string);
		while (matcherStart.find()) {
			int startIndex = matcherStart.start();
			int endIndex = startIndex;
			if (matcherEnd.find(startIndex))
				endIndex = matcherEnd.end();
			results.add(string.substring(startIndex, endIndex));
		}
		return results;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER)
		{
			collect();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
