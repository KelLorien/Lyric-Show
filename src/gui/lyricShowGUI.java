package gui;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Arrays;
import java.util.List;

import controllers.ManageTabController;
import controllers.SearchTabController;
import controllers.SlideshowTabController;
import data.domain.Song;
import services.SongList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LyricShowGUI extends JFrame {

    public static final String TITLE = "Title";
    public static final String AUTHOR = "Author";
    public static final String KEYWORDS = "Keywords";
    public static final String KEY = "Key";

	ManageTabController manageTabController = ManageTabController.getInstance();
    SearchTabController searchTabController = SearchTabController.getInstance();
    SlideshowTabController slideshowTabController = SlideshowTabController.getInstance();

    /**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LyricShowGUI frame = new LyricShowGUI();
					loadSongs();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel panSlideshow;
	private JPanel panManage;
	private JPanel panSearch;
	private JTextField slideSearch;
	private JLabel lblSearch;
	private static JScrollPane scLib;
	public static JList lstSlideLibrary;
	private JButton btnAddToList;
	private JScrollPane scCurrentList;
	private JButton btnCreateSlideshow;
	private JButton btnAddBlankSlide;
	private JTextField txtManageSearch;
	private JLabel lblSearch_1;
	private static JScrollPane scManageLibrary;
	private static JList lstManageLibrary;
	private JList lstSlideCurrent;
	private JButton btnBackup;
	private JButton btnEdit;
	private JButton btnDeleteSong;
	private JButton importBttn;
	private JTextField txtTitle;
	private JTextField txtComposer;
	private JTextField txtLryicist;
	private JLabel lblTitle;
	private JLabel lblComposer;
	private JLabel lblLyricist;
	private JScrollPane scrollPane_3;
	private JTextArea txtLyrics;
	private JLabel lblSearch_2;
	private JTextField txtSearchSearch;
	private JLabel lblSearchBy;
	private JComboBox cmbSearch;
	private JButton btnSearchSearch;
	private JLabel lblSearchBy_1;
	private JComboBox cmbKey;
	private JButton btnSearchByKey;
	private JScrollPane scrollPane_4;
	private JSeparator separator;
	private JLabel lblSearchResults;
	private JButton btnSearchAdd;
	/**
	 * Create the frame.
	 */
	public LyricShowGUI() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(200, 200, 450, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getTabbedPane_2());
	}
	private JTabbedPane getTabbedPane_2() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(6, 6, 438, 441);
			tabbedPane.addTab("Slideshow", null, getPanSlideshow(), null);
			tabbedPane.addTab("Manage Songs", null, getPanManage(), null);
			tabbedPane.addTab("Search", null, getPanSearch(), null);
		}
		return tabbedPane;
	}
	private JPanel getPanSlideshow() {
		if (panSlideshow == null) {
			panSlideshow = new JPanel();
			panSlideshow.setLayout(null);
			panSlideshow.add(getSlideSearch());
			panSlideshow.add(getLblSearch());
			panSlideshow.add(getScLib());
			panSlideshow.add(getBtnAddToList());
			panSlideshow.add(getScCurrentList());
			panSlideshow.add(getBtnCreateSlideshow());
			panSlideshow.add(getBtnAddBlankSlide());
			panSlideshow.add(getBtnRemove());
			panSlideshow.add(getBtnMoveDown());
			panSlideshow.add(getBtnMoveUp());
		}
		return panSlideshow;
	}
	private JPanel getPanManage() {
		if (panManage == null) {
			panManage = new JPanel();
			panManage.setLayout(null);
			panManage.add(getTxtManageSearch());
			panManage.add(getLblSearch_1());
			panManage.add(getScManageLibrary());
			panManage.add(getBtnBackup());
			panManage.add(getBtnEdit());
			panManage.add(getBtnDeleteSong());
			panManage.add(getImportBttn());
			panManage.add(getTxtTitle());
			panManage.add(getTxtComposer());
			panManage.add(getTxtLryicist());
			panManage.add(getLblTitle());
			panManage.add(getLblComposer());
			panManage.add(getLblLyricist());
			panManage.add(getScrollPane_3());
			panManage.add(getHistoryButton());
			panManage.add(getBtnSave());
		}
		return panManage;
	}
	private JPanel getPanSearch() {
		if (panSearch == null) {
			panSearch = new JPanel();
			panSearch.setLayout(null);
			panSearch.add(getLblSearch_2());
			panSearch.add(getTxtSearchSearch());
			panSearch.add(getLblSearchBy());
			panSearch.add(getCmbSearch());
			panSearch.add(getBtnSearchSearch());
			panSearch.add(getLblSearchBy_1());
			panSearch.add(getCmbKey());
			panSearch.add(getBtnSearchByKey());
			panSearch.add(getScrollPane_4());
			panSearch.add(getSeparator());
			panSearch.add(getLblSearchResults());
			panSearch.add(getBtnSearchAdd());
		}
		return panSearch;
	}
	private JTextField getSlideSearch() {
		if (slideSearch == null) {
			slideSearch = new JTextField();
			slideSearch.setBounds(31, 24, 245, 28);
			slideSearch.setColumns(10);
		}
		return slideSearch;
	}
	private JLabel getLblSearch() {
		if (lblSearch == null) {
			lblSearch = new JLabel("Search");
			lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
			lblSearch.setBounds(118, 6, 61, 16);
		}
		return lblSearch;
	}
	private JScrollPane getScLib() {
		if (scLib == null) {
			scLib = new JScrollPane();
			scLib.addMouseListener(new MouseAdapter() {
			});
			scLib.setBounds(31, 52, 245, 171);
			scLib.setViewportView(getLstSlideLibrary());
		}
		return scLib;
	}
	private JList getLstSlideLibrary() {
		if (lstSlideLibrary == null) {
			lstSlideLibrary = new JList();
			lstSlideLibrary.addMouseListener(new MouseAdapter() {
			});
		}
		
		return lstSlideLibrary;
	}
	private JButton getBtnAddToList() {
		if (btnAddToList == null) {
			btnAddToList = new JButton("Add to List");
			btnAddToList.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					String song = lstSlideLibrary.getSelectedValue().toString();
					lmCurrentList.addElement(song);
					lstSlideCurrent = new JList(lmCurrentList);
					scCurrentList.setViewportView(lstSlideCurrent);
				}
			});
			btnAddToList.setBounds(288, 116, 117, 29);
		}
		return btnAddToList;
	}
	private JScrollPane getScCurrentList() {
		if (scCurrentList == null) {
			scCurrentList = new JScrollPane();
			scCurrentList.setBounds(31, 241, 245, 148);
			scCurrentList.setViewportView(getLstSlideCurrent());
		}
		return scCurrentList;
	}
	private JButton getBtnCreateSlideshow() {
		if (btnCreateSlideshow == null) {
			btnCreateSlideshow = new JButton("Create Slideshow");
			btnCreateSlideshow.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
                    //TODO: create slideshow
					//This needs to be modified
					slideshowTabController.createSlideShow(Arrays.asList("testing"), new File(""));
				}
			});
			btnCreateSlideshow.setBounds(282, 360, 135, 29);
		}
		return btnCreateSlideshow;
	}
	private JButton getBtnAddBlankSlide() {
		if (btnAddBlankSlide == null) {
			btnAddBlankSlide = new JButton("Add Blank Slide");
			btnAddBlankSlide.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					lmCurrentList.addElement(SlideshowTabController.BLANK_SLIDE_TITLE);
					lstSlideCurrent = new JList(lmCurrentList);
					scCurrentList.setViewportView(lstSlideCurrent);
				}
			});
			btnAddBlankSlide.setBounds(282, 336, 136, 29);
		}
		return btnAddBlankSlide;
	}
	private JTextField getTxtManageSearch() {
		if (txtManageSearch == null) {
			txtManageSearch = new JTextField();
			txtManageSearch.setBounds(6, 22, 255, 28);
			txtManageSearch.setColumns(10);
		}
		return txtManageSearch;
	}
	private JLabel getLblSearch_1() {
		if (lblSearch_1 == null) {
			lblSearch_1 = new JLabel("Search");
			lblSearch_1.setHorizontalAlignment(SwingConstants.CENTER);
			lblSearch_1.setBounds(101, 6, 61, 16);
		}
		return lblSearch_1;
	}
	private JScrollPane getScManageLibrary() {
		if (scManageLibrary == null) {
			scManageLibrary = new JScrollPane();
			scManageLibrary.setBounds(6, 48, 255, 99);
			scManageLibrary.setViewportView(getLstManageLibrary());
		}
		return scManageLibrary;
	}
	private JList getLstManageLibrary() {
		if (lstManageLibrary == null) {
			lstManageLibrary = new JList();
		}
		return lstManageLibrary;
	}
	private JList getLstSlideCurrent() {
		if (lstSlideCurrent == null) {
			lstSlideCurrent = new JList();
		}
		return lstSlideCurrent;
	}
	private JButton getBtnBackup() {
		if (btnBackup == null) {
			btnBackup = new JButton("Backup Library");
			btnBackup.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
                    //TODO: backup library
                    //needs to get a target directory AND user's selection of PPT, text, or both
                    File target = null;
                    searchTabController.backupLibrary(target);
				}
			});
			btnBackup.setBounds(280, 48, 117, 29);
		}
		return btnBackup;
	}
	private JButton getBtnEdit() {
		if (btnEdit == null) {
			btnEdit = new JButton("Edit Song");
			btnEdit.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
                    //TODO: start editing song
                    String title = "";
                    Song song = searchTabController.getSong(title);
                    manageTabController.editSong(song);
				}
			});
			btnEdit.setBounds(280, 130, 117, 29);
		}
		return btnEdit;
	}
	private JButton getBtnDeleteSong() {
		if (btnDeleteSong == null) {
			btnDeleteSong = new JButton("Delete Song");
			btnDeleteSong.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String songName = lstManageLibrary.getSelectedValue().toString();
                    //TODO: delete song
                    searchTabController.deleteSong(songName);
				}
			});
			btnDeleteSong.setBounds(280, 104, 117, 29);
		}
		return btnDeleteSong;
	}
	private JButton getImportBttn() {
		if (importBttn == null) {
			importBttn = new JButton("Import File");
			importBttn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					//Create a file chooser
					final JFileChooser fc = new JFileChooser();
					fc.setDialogTitle("Choose a file to add...");
					FileNameExtensionFilter ffilter = new FileNameExtensionFilter("PowerPoints", "ppt");
					fc.setFileFilter(ffilter);
					
					//In response to a button click:
					int returnVal = fc.showOpenDialog(null);
					File targetFile = fc.getSelectedFile();
					//TODO: import song from PPT
                    manageTabController.importFromPPT(targetFile);
				}
			});
			importBttn.setBounds(280, 78, 117, 29);
		}
		return importBttn;
	}
	private JTextField getTxtTitle() {
		if (txtTitle == null) {
			txtTitle = new JTextField();
			txtTitle.setBounds(77, 159, 320, 28);
			txtTitle.setColumns(10);
		}
		return txtTitle;
	}
	private JTextField getTxtComposer() {
		if (txtComposer == null) {
			txtComposer = new JTextField();
			txtComposer.setBounds(77, 187, 320, 28);
			txtComposer.setColumns(10);
		}
		return txtComposer;
	}
	private JTextField getTxtLryicist() {
		if (txtLryicist == null) {
			txtLryicist = new JTextField();
			txtLryicist.setBounds(77, 217, 320, 28);
			txtLryicist.setColumns(10);
		}
		return txtLryicist;
	}
	private JLabel getLblTitle() {
		if (lblTitle == null) {
			lblTitle = new JLabel("Title:");
			lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTitle.setBounds(16, 165, 61, 16);
		}
		return lblTitle;
	}
	private JLabel getLblComposer() {
		if (lblComposer == null) {
			lblComposer = new JLabel("Composer:");
			lblComposer.setHorizontalAlignment(SwingConstants.RIGHT);
			lblComposer.setBounds(6, 193, 71, 16);
		}
		return lblComposer;
	}
	private JLabel getLblLyricist() {
		if (lblLyricist == null) {
			lblLyricist = new JLabel("Lyricist:");
			lblLyricist.setHorizontalAlignment(SwingConstants.RIGHT);
			lblLyricist.setBounds(16, 223, 61, 16);
		}
		return lblLyricist;
	}
	private JScrollPane getScrollPane_3() {
		if (scrollPane_3 == null) {
			scrollPane_3 = new JScrollPane();
			scrollPane_3.setBounds(6, 245, 405, 120);
			scrollPane_3.setViewportView(getTxtLyrics());
		}
		return scrollPane_3;
	}
	private JTextArea getTxtLyrics() {
		if (txtLyrics == null) {
			txtLyrics = new JTextArea();
		}
		return txtLyrics;
	}
	private JLabel getLblSearch_2() {
		if (lblSearch_2 == null) {
			lblSearch_2 = new JLabel("Search:");
			lblSearch_2.setHorizontalAlignment(SwingConstants.RIGHT);
			lblSearch_2.setBounds(18, 18, 61, 16);
		}
		return lblSearch_2;
	}
	private JTextField getTxtSearchSearch() {
		if (txtSearchSearch == null) {
			txtSearchSearch = new JTextField();
			txtSearchSearch.setBounds(79, 12, 301, 28);
			txtSearchSearch.setColumns(10);
		}
		return txtSearchSearch;
	}
	private JLabel getLblSearchBy() {
		if (lblSearchBy == null) {
			lblSearchBy = new JLabel("Search By:");
			lblSearchBy.setHorizontalAlignment(SwingConstants.RIGHT);
			lblSearchBy.setBounds(6, 46, 73, 16);
		}
		return lblSearchBy;
	}
	private JComboBox getCmbSearch() {
		if (cmbSearch == null) {
			cmbSearch = new JComboBox();
			cmbSearch.setModel(new DefaultComboBoxModel(new String[] {TITLE, AUTHOR, KEYWORDS}));
			cmbSearch.setBounds(79, 42, 126, 27);
		}
		return cmbSearch;
	}
	private JButton getBtnSearchSearch() {
		if (btnSearchSearch == null) {
			btnSearchSearch = new JButton("Search");
			btnSearchSearch.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String search = txtSearchSearch.getText();
					String type = cmbSearch.getSelectedItem().toString();
                    //TODO: search for song by artist/lyricist/keywords
                    searchTabController.searchByType(type, search);
				}
			});
			btnSearchSearch.setBounds(205, 41, 117, 29);
		}
		return btnSearchSearch;
	}
	private JLabel getLblSearchBy_1() {
		if (lblSearchBy_1 == null) {
			lblSearchBy_1 = new JLabel("Search By:");
			lblSearchBy_1.setHorizontalAlignment(SwingConstants.RIGHT);
			lblSearchBy_1.setBounds(16, 74, 63, 16);
		}
		return lblSearchBy_1;
	}
	private JComboBox getCmbKey() {
		if (cmbKey == null) {
			cmbKey = new JComboBox();
			cmbKey.setModel(new DefaultComboBoxModel(new String[] {"C", "C#", "D", "Db", "E", "F", "F#", "G", "Ab", "A", "Bb", "B"}));
			cmbKey.setBounds(79, 70, 126, 27);
		}
		return cmbKey;
	}
	private JButton getBtnSearchByKey() {
		if (btnSearchByKey == null) {
			btnSearchByKey = new JButton("Search By Key");
			btnSearchByKey.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String key = cmbKey.getSelectedItem().toString();
                    //TODO: search by key (via keyword)
                    searchTabController.searchByType(key, KEY);
				}
			});
			btnSearchByKey.setBounds(205, 69, 117, 29);
		}
		return btnSearchByKey;
	}
	private JScrollPane getScrollPane_4() {
		if (scrollPane_4 == null) {
			scrollPane_4 = new JScrollPane();
			scrollPane_4.setBounds(6, 142, 405, 208);
			scrollPane_4.setViewportView(getLstSearchResults());
		}
		return scrollPane_4;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
			separator.setBounds(6, 102, 405, 16);
		}
		return separator;
	}
	private JLabel getLblSearchResults() {
		if (lblSearchResults == null) {
			lblSearchResults = new JLabel("Search Results");
			lblSearchResults.setHorizontalAlignment(SwingConstants.CENTER);
			lblSearchResults.setBounds(150, 114, 117, 16);
		}
		return lblSearchResults;
	}
	private JButton getBtnSearchAdd() {
		if (btnSearchAdd == null) {
			btnSearchAdd = new JButton("Add to List");
			btnSearchAdd.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String song = lstSearchResults.getSelectedValue().toString();
					lmCurrentList.addElement(song);
					lstSlideCurrent = new JList(lmCurrentList);
					scCurrentList.setViewportView(lstSlideCurrent);
				}
			});
			btnSearchAdd.setBounds(150, 360, 117, 29);
		}
		return btnSearchAdd;
	}
	DefaultListModel lmLibrary = new DefaultListModel();
	DefaultListModel lmCurrentList = new DefaultListModel();
	private JButton btnRemove;
	private JButton btnMoveDown;
	private JButton btnMoveUp;
	private JButton button;
	private JButton btnSave;
//	private JButton btnSaveSong;
	private JList lstSearchResults;
	private static void loadSongs()
	{
		SongList songlist = SongList.getInstance();
		List<String> songs = songlist.getSongTitles();
		DefaultListModel lm = new DefaultListModel();

        for (String song : songs) {
            lm.addElement(song);
        }
		lm.addElement("This should work");
		lstSlideLibrary = new JList(lm);
		lstManageLibrary = new JList(lm);
		scLib.setViewportView(lstSlideLibrary);
		scManageLibrary.setViewportView(lstManageLibrary);
	}
	private JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton("Remove");
			btnRemove.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(lstSlideCurrent.getSelectedIndex()!=-1)
					lmCurrentList.remove(lstSlideCurrent.getSelectedIndex());
				}
			});
			btnRemove.setBounds(282, 310, 135, 29);
		}
		return btnRemove;
	}
	private JButton getBtnMoveDown() {
		if (btnMoveDown == null) {
			btnMoveDown = new JButton("Move Down");
			btnMoveDown.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(lstSlideCurrent.getSelectedIndex()!=-1)
					{
					if(lstSlideCurrent.getSelectedIndex()<lmCurrentList.getSize()-1)
					{
						int songPlace = lstSlideCurrent.getSelectedIndex();
						String song = lstSlideCurrent.getSelectedValue().toString();
						lmCurrentList.remove(songPlace);
						lmCurrentList.add(songPlace+1, song);
						lstSlideCurrent.setSelectedIndex(songPlace+1);
					}
					}
				}
			});
			btnMoveDown.setBounds(282, 282, 135, 29);
		}
		return btnMoveDown;
	}
	private JButton getBtnMoveUp() {
		if (btnMoveUp == null) {
			btnMoveUp = new JButton("Move Up");
			btnMoveUp.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(lstSlideCurrent.getSelectedIndex()>0 && lstSlideCurrent.getSelectedIndex()!=-1)
					{
						int songPlace = lstSlideCurrent.getSelectedIndex();
						String song = lstSlideCurrent.getSelectedValue().toString();
						lmCurrentList.remove(songPlace);
						lmCurrentList.add(songPlace-1, song);
						lstSlideCurrent.setSelectedIndex(songPlace-1);
					}
				}
			});
			btnMoveUp.setBounds(282, 255, 135, 29);
		}
		return btnMoveUp;
	}
	private JButton getHistoryButton() {
		if (button == null) {
			button = new JButton("View History");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
                    //TODO: display History
				}
			});
			button.setBounds(280, 23, 117, 29);
		}
		return button;
	}
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton("Save Song");
			btnSave.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String title = txtTitle.getText();
					String composer = txtComposer.getText();
					String lyricist = txtLryicist.getText();
					String lyrics = txtLyrics.getText();
                    //TODO: save song (update song)
                    Song song = new Song(title);
                    //instantiate the rest of the song
                    manageTabController.saveSong(song);
				}
			});
			btnSave.setBounds(158, 366, 117, 29);
		}
		return btnSave;
	}
	
	private JList getLstSearchResults() {
		if (lstSearchResults == null) {
			lstSearchResults = new JList();
			lstSearchResults.setModel(new AbstractListModel() {
				String[] values = new String[] {};
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
		}
		return lstSearchResults;
	}
}
