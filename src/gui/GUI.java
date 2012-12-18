package gui;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.*;

import controllers.ManageTabController;
import controllers.SearchTabController;
import controllers.SlideshowTabController;
import data.domain.Song;
import services.History;
import services.SongList;
import services.BackUp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

@SuppressWarnings("serial")
public class GUI extends JFrame {

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
                    GUI frame = new GUI();
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
    private JList lstSlideCurrent;
    private JButton btnBackup;
    private JButton btnEdit;
    private JButton btnDeleteSong;
    private JButton importBttn;
    private static JTextField txtTitle;
    private static JTextField txtComposer;
    private JLabel lblTitle;
    private JLabel lblComposer;
    private JLabel lblLyricist;
    private JScrollPane scrollPane_3;
    private static JTextArea txtLyrics;
    private JLabel lblSearch_2;
    private JTextField txtSearchSearch;
    private JLabel lblSearchBy;
    private JComboBox cmbSearch;
    private JButton btnSearchSearch;
    private JLabel lblSearchBy_1;
    private JComboBox cmbKey;
    private JButton btnSearchByKey;
    private JScrollPane scResults;
    private JSeparator separator;
    private JLabel lblSearchResults;
    private JButton btnSearchAdd;
    /**
     * Create the frame.
     */
    public GUI() {
        setTitle("PowerPoint Creator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(200, 200, 450, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.add(getTabbedPane_2());
    }
    private JTabbedPane getTabbedPane_2() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            tabbedPane.setBounds(6, 6, 438, 566);
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
            panManage.add(getBtnBackup());
            panManage.add(getBtnEdit());
            panManage.add(getBtnDeleteSong());
            panManage.add(getImportBttn());
            panManage.add(getTxtTitle());
            panManage.add(getTxtComposer());
            panManage.add(getLblTitle());
            panManage.add(getLblComposer());
            panManage.add(getLblLyricist());
            panManage.add(getScrollPane_3());
            panManage.add(getHistoryButton());
            panManage.add(getBtnSave());
            panManage.add(getLblCopyright());
            panManage.add(getTxtCopyright());
            panManage.add(getTxtLyricist());
            panManage.add(getLblKey());
            panManage.add(getCmbManageKey());
            panManage.add(getScrollManage());
            panManage.add(getLblKeywords());
            panManage.add(getTxtKeywords());
            panManage.add(getLblSeperateByCommas());
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
            panSearch.add(getScResults());
            panSearch.add(getSeparator());
            panSearch.add(getLblSearchResults());
            panSearch.add(getBtnSearchAdd());
        }
        return panSearch;
    }
    private static DefaultListModel searchCreate = new DefaultListModel();
    private JTextField getSlideSearch() {
        if (slideSearch == null) {
            slideSearch = new JTextField();
            slideSearch.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent arg0) {
                    //will hopefully search inside the library
                    String searchSongs = slideSearch.getText().toLowerCase();
                    searchCreate.removeAllElements();

                    for(int i=0;i<lm.size();i++)
                    {
                        if (lm.elementAt(i).toString().toLowerCase().contains(searchSongs))
                        {
                            searchCreate.addElement(lm.elementAt(i).toString());
                        }
                    }
                    lstSlideLibrary = new JList(searchCreate);
                    scLib.setViewportView(lstSlideLibrary);
                }
            });
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
            scLib.setBounds(31, 52, 245, 290);
            scLib.setViewportView(getLstSlideLibrary());
        }
        return scLib;
    }
    private JList getLstSlideLibrary() {
        if (lstSlideLibrary == null) {
            lstSlideLibrary = new JList();
            lstSlideLibrary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
            btnAddToList.setBounds(282, 156, 136, 29);
        }
        return btnAddToList;
    }
    private JScrollPane getScCurrentList() {
        if (scCurrentList == null) {
            scCurrentList = new JScrollPane();
            scCurrentList.setBounds(31, 373, 245, 141);
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
                    List<String> songs = new ArrayList<String>();
                    for(int i = 0; i<lmCurrentList.getSize();i++)
                    {
                        songs.add(lmCurrentList.get(i).toString());
                    }
                    final JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Choose a place to save the PowerPoint...");
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        slideshowTabController.createSlideShow(songs, fc.getCurrentDirectory(),fc.getSelectedFile().getName());
                    }
                }
            });
            btnCreateSlideshow.setBounds(283, 485, 135, 29);
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
            btnAddBlankSlide.setBounds(282, 186, 136, 29);
        }
        return btnAddBlankSlide;
    }
    public static DefaultListModel manageSearch = new DefaultListModel();
    private JTextField getTxtManageSearch() {
        if (txtManageSearch == null) {
            txtManageSearch = new JTextField();
            txtManageSearch.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    String searchSongs = txtManageSearch.getText().toLowerCase();
                    manageSearch.removeAllElements();

                    for(int i=0;i<lm.size();i++)
                    {
                        if (lm.elementAt(i).toString().toLowerCase().contains(searchSongs))
                        {
                            manageSearch.addElement(lm.elementAt(i).toString());
                        }
                    }
                    lstManageSongs = new JList(manageSearch);
                    scrollManage.setViewportView(lstManageSongs);
                }
            });
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
                    Object[] possibilities = {"Library", "PowerPoints", "Both"};
                    String s = (String) JOptionPane.showInputDialog(
                            null,
                            "Do you want to export the Library, the PowerPoints, or both?",
                            "Backing up the Library",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            "Both");

                    //If a string was returned, say so.
                    if ((s != null) && (s.length() > 0)) {
                        final JFileChooser fc = new JFileChooser();
                        fc.setDialogTitle("Choose a place to backup the library...");
                        fc.showSaveDialog(null);
                        File target = fc.getSelectedFile();
                        if (s.equalsIgnoreCase("Library")) {
                            manageTabController.backupLibrary(target, true, false);
                        } else if (s.equalsIgnoreCase("PowerPoints")) {
                            manageTabController.backupLibrary(target, false, true);
                        } else if (s.equalsIgnoreCase("Both")) {
                            manageTabController.backupLibrary(target, true, true);
                        }
                    }
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
                    clearManageText();
                    String title = lstManageSongs.getSelectedValue().toString();
                    Song song = searchTabController.getSong(title);
                    txtTitle.setText(song.getTitle());
                    txtComposer.setText(song.getAuthor());
                    txtLyricist.setText(song.getLyricist());
                    txtLyrics.setText(song.getLyrics());
                    txtCopyright.setText(song.getCopyright());
                    cmbManageKey.setSelectedItem(song.getMusicalKey());
                    String keyString = "";
                    List<String> keywords = song.getKeywords();
                    if (keywords.size() >0)
                    {
                        for(String keyword: keywords)
                        {
                            keyString += keyword;
                            keyString += ", ";
                        }
                        keyString = keyString.substring(0, keyString.length()-2);
                    }
                    txtKeywords.setText(keyString);
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
                    if (lstManageSongs.getSelectedValue() != null) {
                        int n =
                                JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " +
                                        lstManageSongs.getSelectedValue().toString() +"?","Warning",JOptionPane.YES_NO_OPTION);
                        if(n == JOptionPane.YES_OPTION)
                        {
                            searchTabController.deleteSong(lstManageSongs.getSelectedValue().toString());
                            loadSongs();
                        }
                    }
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
                    fc.showOpenDialog(null);
                    File targetFile = fc.getSelectedFile();

                    manageTabController.importFromPPT(targetFile);
                    loadSongs();
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
            scrollPane_3.setBounds(6, 306, 405, 176);
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
            cmbSearch.setModel(new DefaultComboBoxModel(new String[]{"Title", "Author", "Keywords"}));
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
                    resultsList.removeAllElements();
                    lstSearchResults = new JList(resultsList);
                    scResults.setViewportView(lstSearchResults);
                    String search = txtSearchSearch.getText();
                    String type = cmbSearch.getSelectedItem().toString();

                    List<String> results = searchTabController.searchByType(type, search);
                    for (String song : results) {
                        resultsList.addElement(song);
                    }
                    lstSearchResults = new JList(resultsList);
                    scResults.setViewportView(lstSearchResults);
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
    DefaultListModel resultsList = new DefaultListModel();
    private JButton getBtnSearchByKey() {
        if (btnSearchByKey == null) {
            btnSearchByKey = new JButton("Search By Key");
            btnSearchByKey.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String key = cmbKey.getSelectedItem().toString();
                    resultsList.removeAllElements();
                    lstSearchResults = new JList(resultsList);
                    scResults.setViewportView(lstSearchResults);
                    List<String> results = searchTabController.searchByType(KEY, key);

                    for (String song : results) {
                        resultsList.addElement(song);
                    }
                    lstSearchResults = new JList(resultsList);
                    scResults.setViewportView(lstSearchResults);
                }
            });
            btnSearchByKey.setBounds(205, 69, 117, 29);
        }
        return btnSearchByKey;
    }
    private JScrollPane getScResults() {
        if (scResults == null) {
            scResults = new JScrollPane();
            scResults.setBounds(6, 142, 405, 331);
            scResults.setViewportView(getLstSearchResults());
        }
        return scResults;
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
            btnSearchAdd.setBounds(150, 485, 117, 29);
        }
        return btnSearchAdd;
    }

    DefaultListModel lmCurrentList = new DefaultListModel();
    private JButton btnRemove;
    private JButton btnMoveDown;
    private JButton btnMoveUp;
    private JButton button;
    private JButton btnSave;
    private JList lstSearchResults;
    private JLabel lblCopyright;
    private static JTextField txtCopyright;
    private static JTextField txtLyricist;
    private JLabel lblKey;
    private static JComboBox cmbManageKey;
    private static JScrollPane scrollManage;
    private static JList lstManageSongs;
    private static DefaultListModel lm = new DefaultListModel();
    private static DefaultListModel lmManage = new DefaultListModel();
    private JLabel lblKeywords;
    private JTextField txtKeywords;
    private JLabel lblSeperateByCommas;
    private static void loadSongs()
    {
        SongList songlist = SongList.getInstance();
        List<String> songs = songlist.getSongTitles();
        lm.removeAllElements();
        lmManage.removeAllElements();
        for (String song : songs) {
            lm.addElement(song);
            lmManage.addElement(song);

        }
        lstSlideLibrary = new JList(lm);
        lstManageSongs = new JList(lm);
        scLib.setViewportView(lstSlideLibrary);
        scrollManage.setViewportView(lstManageSongs);
    }
    private JButton getBtnRemove() {
        if (btnRemove == null) {
            btnRemove = new JButton("Remove");
            btnRemove.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (lstSlideCurrent.getSelectedIndex() != -1)
                        lmCurrentList.remove(lstSlideCurrent.getSelectedIndex());
                }
            });
            btnRemove.setBounds(283, 425, 135, 29);
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
            btnMoveDown.setBounds(283, 397, 135, 29);
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
            btnMoveUp.setBounds(283, 370, 135, 29);
        }
        return btnMoveUp;
    }
    private JButton getHistoryButton() {
        if (button == null) {
            button = new JButton("View History");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {

                    History history = History.getInstance();
                    clearManageText();
                    try {
                        txtLyrics.setText(history.getHistory());
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null,"No history found!");
                    }
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
                    String key = cmbManageKey.getSelectedItem().toString();
                    Song updatedSong = new Song();
                    updatedSong.setLyricist(txtLyricist.getText());
                    updatedSong.setAuthor(txtComposer.getText());
                    updatedSong.setLyrics(txtLyrics.getText());
                    updatedSong.setCopyright(txtCopyright.getText());
                    updatedSong.setTitle(title);
                    updatedSong.setMusicalKey(key);
                    String keyword = txtKeywords.getText();
                    String[] keywords = keyword.split(",");
                    for (String word: keywords)
                    {
                        updatedSong.addKeyword(word.trim());
                        JOptionPane.showMessageDialog(null, "a" +word);
                    }
                    if(cmbManageKey.getSelectedIndex()>1)
                    {
                        updatedSong.setMusicalKey(cmbManageKey.getSelectedItem().toString());
                    }
                    manageTabController.saveSong(updatedSong);
                }
            });
            btnSave.setBounds(155, 485, 117, 29);
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
    private JLabel getLblCopyright() {
        if (lblCopyright == null) {
            lblCopyright = new JLabel("Copyright:");
            lblCopyright.setHorizontalAlignment(SwingConstants.RIGHT);
            lblCopyright.setBounds(6, 251, 71, 16);
        }
        return lblCopyright;
    }
    private JTextField getTxtCopyright() {
        if (txtCopyright == null) {
            txtCopyright = new JTextField();
            txtCopyright.setBounds(77, 245, 161, 28);
            txtCopyright.setColumns(10);
        }
        return txtCopyright;
    }
    private JTextField getTxtLyricist() {
        if (txtLyricist == null) {
            txtLyricist = new JTextField();
            txtLyricist.setBounds(77, 217, 320, 28);
            txtLyricist.setColumns(10);
        }
        return txtLyricist;
    }
    private JLabel getLblKey() {
        if (lblKey == null) {
            lblKey = new JLabel("Key:");
            lblKey.setHorizontalAlignment(SwingConstants.RIGHT);
            lblKey.setBounds(241, 251, 31, 16);
        }
        return lblKey;
    }
    private JComboBox getCmbManageKey() {
        if (cmbManageKey == null) {
            cmbManageKey = new JComboBox();
            cmbManageKey.setModel(new DefaultComboBoxModel(new String[] {"[no key]", "C", "C#", "D", "Db", "E", "F", "F#", "G", "Ab", "A", "Bb", "B"}));
            cmbManageKey.setBounds(280, 247, 106, 27);
        }
        return cmbManageKey;
    }
    private JScrollPane getScrollManage() {
        if (scrollManage == null) {
            scrollManage = new JScrollPane();
            scrollManage.setBounds(6, 53, 255, 106);
            scrollManage.setViewportView(getLstManageSongs());
        }
        return scrollManage;
    }
    private JList getLstManageSongs() {
        if (lstManageSongs == null) {
            lstManageSongs = new JList();
        }
        return lstManageSongs;
    }
    public static void clearManageText()
    {
        txtTitle.setText("");
        txtComposer.setText("");
        txtLyrics.setText("");
        txtLyricist.setText("");
        txtCopyright.setText("");
        cmbManageKey.setSelectedIndex(0);
    }
    private JLabel getLblKeywords() {
        if (lblKeywords == null) {
            lblKeywords = new JLabel("Keywords:");
            lblKeywords.setHorizontalAlignment(SwingConstants.RIGHT);
            lblKeywords.setBounds(6, 279, 71, 16);
        }
        return lblKeywords;
    }
    private JTextField getTxtKeywords() {
        if (txtKeywords == null) {
            txtKeywords = new JTextField();
            txtKeywords.setBounds(77, 273, 210, 28);
            txtKeywords.setColumns(10);
        }
        return txtKeywords;
    }
    private JLabel getLblSeperateByCommas() {
        if (lblSeperateByCommas == null) {
            lblSeperateByCommas = new JLabel("*Seperate by commas");
            lblSeperateByCommas.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
            lblSeperateByCommas.setBounds(294, 280, 117, 16);
        }
        return lblSeperateByCommas;
    }
}
