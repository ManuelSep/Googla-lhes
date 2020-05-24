import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class GUI implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private JTextArea newsContent = new JTextArea();
	private DefaultListModel<FinalResult> results = new DefaultListModel<>();
	private JList<FinalResult> titleList;
	private JPanel mainPanel;
	private JScrollPane scrollPaneList = new JScrollPane();
	private JScrollPane scrollPaneText = new JScrollPane(newsContent);
	private JTextField searchField;
	private Border border = BorderFactory.createLineBorder(Color.black, 1);
	private String searchedWord;
	private JButton searchButton;
	private boolean pressed = false;
	private Client client;
	
	public GUI(Client client){ 
		this.client = client;
		launchInterface();
	}
	
	private void launchInterface() {
		frame = new JFrame("Googla_lhes");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		addFrameContent();
		frame.pack();
		launch();
	}
	
	public void launch(){
		frame.setVisible(true);
	}

	private void searchBar() { 
		
		JPanel panelNORTH = new JPanel();
		panelNORTH.setLayout(new FlowLayout());
		frame.add(panelNORTH,BorderLayout.NORTH);
		
		searchField = new JTextField("write something here...");
		searchField.setSize(200, 24);

		panelNORTH.add(searchField);
		searchButton = new JButton("SEARCH");
		panelNORTH.add(searchButton);
		
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// first search
				if(!pressed){
					mainPanel();
					pressed = true;
				}
				searchedWord = searchField.getText();
				System.out.println(searchedWord);
				client.sendObject(searchedWord);
				showTitleList();
	      }
	    });
	}
	
	private void addFrameContent() {
		searchBar();
	}

	private void mainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,0));
		frame.add(mainPanel, BorderLayout.CENTER);
		
	}

	private void showTitleList() {
		newsContent("Select the news you want to read...");
		titleList = new JList<FinalResult>(results);
		titleList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println(titleList.getSelectedValue().getResult().getContent());
				newsContent.setText(titleList.getSelectedValue().getResult().getContent());
				highLightWord(newsContent, searchedWord);
			}
		});

	    titleList.setBorder(border);
	    scrollPaneList.setViewportView(titleList);
	    mainPanel.add(scrollPaneList, BorderLayout.EAST);
	    newsContent("Select the news you want to read...");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // MAXIMIZA A JANELA
		
	}
	
	private void newsContent(String content) {
		newsContent.setText(content);
		newsContent.setEnabled(false);
		newsContent.setLineWrap(true);
		newsContent.setWrapStyleWord(true);
		newsContent.setBorder(border);
		mainPanel.add(scrollPaneText, BorderLayout.WEST);
	}
	
	public void showResults(ArrayList<Result> resultsAList) {
		DefaultListModel<FinalResult> newResult = new DefaultListModel<>();
		for (Result result : resultsAList) {
			if(result.getNumberOfOccurrences() > 0) {
				newResult.addElement(new FinalResult(result));
			}
		}
		titleList.setModel(newResult);
	}
	
	private void highLightWord(JTextArea newsContent, String word){
		// sublinha as palavras a amarelo. 
		Highlighter highlighter = newsContent.getHighlighter();
	    HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);

	    int length = word.length();
		List<String> list = Arrays.asList(newsContent.getText().replaceAll("\\p{P}", " ").split(" "));
		ArrayList<Integer> indexes = new ArrayList<>();
		int currentIndex = 0;
	
		for (String string : list) {
			if(string.toLowerCase().equals(( word.toLowerCase()  )))
				indexes.add(currentIndex);
			currentIndex += string.length() + 1;
		}
		
		for (Integer integer : indexes) {
			int limit = integer + length;
			try {
				highlighter.addHighlight(integer, limit, painter);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
	}
}
