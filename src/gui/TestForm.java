package gui;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class TestForm extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextArea manageSongsTextInputField;

    private String[] songs = new String[] {"Amazing Grace", "Fairest Lord Jesus", "O Church Arise", "Power of the Cross", "It is Well with My Soul", "My Jesus I Love Thee", "Sing, Sing, Sing", "In Christ Alone", "Angels We Have Heard On High"};

    /**
     * Launch the application.
     */
    
    public static TestForm frame;
    public static void main(String[] args) {
        frame = new TestForm();
        frame.setVisible(true);
//		frame.
    }
    /**
     * Create the frame.
     */
    public TestForm() {
        setTitle("PowerPoint Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 478, 500);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmAddNewSong = new JMenuItem("Add new song to library...");
        mntmAddNewSong.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                importSong();
            }
        });
        mnFile.add(mntmAddNewSong);

        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTabbedPane paneMenu = new JTabbedPane(JTabbedPane.TOP);
        paneMenu.setBounds(6, 6, 466, 444);
        contentPane.add(paneMenu);

        JPanel panSlide = new JPanel();
        paneMenu.addTab("Slideshow", null, panSlide, null);
        panSlide.setLayout(null);

        textField = new JTextField("type something");
        textField.setBounds(85, 18, 190, 28);
        panSlide.add(textField);
        textField.setColumns(10);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSearch.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        lblSearch.setBounds(25, 24, 61, 16);
        panSlide.add(lblSearch);

        JButton btnAddSong = new JButton("Add Song");
        btnAddSong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

            }
        });
        btnAddSong.setBounds(287, 95, 136, 29);
        panSlide.add(btnAddSong);

        JList lstCurrentList = new JList();
        lstCurrentList.setBounds(90, 201, 185, 126);
        panSlide.add(lstCurrentList);

        JLabel lblCurrentList = new JLabel("Current List");
        lblCurrentList.setHorizontalAlignment(SwingConstants.CENTER);
        lblCurrentList.setBounds(134, 183, 92, 16);
        panSlide.add(lblCurrentList);

        JButton btnCreateSlideshow = new JButton("Create Slideshow");
        btnCreateSlideshow.setBounds(287, 245, 136, 29);
        panSlide.add(btnCreateSlideshow);

        JScrollPane scList = new JScrollPane();
        scList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scList.setBounds(95, 58, 185, 114);
        panSlide.add(scList);

        createSongJListBuildTab(scList);

        JButton btnAddBlankSlike = new JButton("Add Blank Slide");
        btnAddBlankSlike.setBounds(287, 269, 136, 29);
        panSlide.add(btnAddBlankSlike);

        JButton btnViewHistory = new JButton("View History");
        btnViewHistory.setBounds(287, 121, 136, 29);
        panSlide.add(btnViewHistory);

        JPanel panEdit = new JPanel();
        paneMenu.addTab("Manage Songs", null, panEdit, null);
        panEdit.setLayout(null);

        JLabel lblLibrary = new JLabel("Library");
        lblLibrary.setHorizontalAlignment(SwingConstants.CENTER);
        lblLibrary.setBounds(133, 6, 61, 16);
        panEdit.add(lblLibrary);

        JScrollPane scrollPane_3 = new JScrollPane();
        scrollPane_3.setBounds(133, 150, -127, -117);
        panEdit.add(scrollPane_3);

        textField_2 = new JTextField("last");
        textField_2.setBounds(6, 23, 294, 28);
        panEdit.add(textField_2);
        textField_2.setColumns(10);

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(6, 51, 294, 84);
        panEdit.add(scrollPane_2);

        createSongJListManageTab(scrollPane_2);

        JButton btnEditSong = new JButton("Edit Song");
        btnEditSong.setBounds(302, 51, 117, 29);
        panEdit.add(btnEditSong);

        JButton btnDeleteSong = new JButton("Delete Song");
        btnDeleteSong.setBounds(302, 75, 117, 29);
        panEdit.add(btnDeleteSong);

        JButton btnNewButton = new JButton("Backup Library");
        btnNewButton.setBounds(302, 24, 117, 28);
        panEdit.add(btnNewButton);

        JScrollPane scrollPane_4 = new JScrollPane();
        scrollPane_4.setBounds(6, 160, 413, 200);
        panEdit.add(scrollPane_4);

        manageSongsTextInputField = new JTextArea("Hallo");
        scrollPane_4.setViewportView(manageSongsTextInputField);

        JButton btnSaveSong = new JButton("Save Song");
        btnSaveSong.setBounds(152, 363, 117, 29);
        panEdit.add(btnSaveSong);
        btnSaveSong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveNewSong();
            }
        });

        JButton btnNewSong = new JButton("Import Song");
        btnNewSong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                importSong();
            }
        });
        btnNewSong.setBounds(302, 102, 117, 29);
        panEdit.add(btnNewSong);

        JPanel panSearch = new JPanel();
        paneMenu.addTab("Search Library", null, panSearch, null);
        panSearch.setLayout(null);

        textField_1 = new JTextField("second");
        textField_1.setBounds(98, 20, 297, 28);
        panSearch.add(textField_1);
        textField_1.setColumns(10);

        JLabel lblSearch_1 = new JLabel("Search:");
        lblSearch_1.setBounds(25, 26, 61, 16);
        lblSearch_1.setHorizontalAlignment(SwingConstants.RIGHT);
        panSearch.add(lblSearch_1);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(278, 46, 117, 29);
        panSearch.add(btnSearch);

        JLabel lblSearchBy = new JLabel("Search By:");
        lblSearchBy.setBounds(6, 51, 80, 16);
        lblSearchBy.setHorizontalAlignment(SwingConstants.RIGHT);
        panSearch.add(lblSearchBy);

        JComboBox comboBox = new JComboBox();
        comboBox.setBounds(98, 47, 168, 27);
        comboBox.setModel(new DefaultComboBoxModel(new String[] {"Title", "Lyrics"}));
        panSearch.add(comboBox);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_1.setBounds(6, 231, 433, 132);
        panSearch.add(scrollPane_1);

        JList list_1 = new JList();
        scrollPane_1.setViewportView(list_1);

        JLabel lblResults = new JLabel("Results");
        lblResults.setHorizontalAlignment(SwingConstants.CENTER);
        lblResults.setBounds(188, 203, 61, 16);
        panSearch.add(lblResults);

        JComboBox comboBox_1 = new JComboBox();
        comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B"}));
        comboBox_1.setBounds(109, 103, 67, 27);
        panSearch.add(comboBox_1);

        JButton btnAddToSong = new JButton("Add to Song List");
        btnAddToSong.setBounds(152, 363, 132, 29);
        panSearch.add(btnAddToSong);

        JSeparator separator = new JSeparator();
        separator.setBounds(6, 87, 433, 16);
        panSearch.add(separator);

        JLabel lblSearchByKey = new JLabel("Search By Key:");
        lblSearchByKey.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSearchByKey.setBounds(6, 107, 96, 16);
        panSearch.add(lblSearchByKey);

        JLabel lblSearchByKeyword = new JLabel("Search By Keyword:");
        lblSearchByKeyword.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSearchByKeyword.setBounds(188, 107, 132, 16);
        panSearch.add(lblSearchByKeyword);

        JComboBox comboBox_2 = new JComboBox();
        comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"[none]", "Easter", "Christmas", "Thanksgiving"}));
        comboBox_2.setBounds(321, 103, 105, 27);
        panSearch.add(comboBox_2);

        JButton btnSearch_1 = new JButton("Search");
        btnSearch_1.setBounds(59, 135, 117, 29);
        panSearch.add(btnSearch_1);

        JButton button = new JButton("Search");
        button.setBounds(278, 135, 117, 29);
        panSearch.add(button);
    }

    JList lstSongs;
    JList list_2;

    private void createSongJListBuildTab(JScrollPane scList) {
        lstSongs = new JList();
        scList.setViewportView(lstSongs);
        lstSongs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstSongs.setModel(new AbstractListModel() {
            String[] values = songs;
            public int getSize() {
                return values.length;
            }
            public Object getElementAt(int index) {
                return values[index];
            }
        });
    }

    private void createSongJListManageTab(JScrollPane scrollPane_2) {
        list_2 = new JList();
        list_2.setModel(new AbstractListModel() {
            String[] values = songs;
            public int getSize() {
                return values.length;
            }
            public Object getElementAt(int index) {
                return values[index];
            }
        });
        scrollPane_2.setViewportView(list_2);
    }

    private void saveNewSong() {
        String input = manageSongsTextInputField.getText();
        songs = Arrays.copyOf(songs, songs.length + 1);
        songs[songs.length-1] = input;

        System.out.println(Arrays.toString(songs));

        list_2.setModel(new AbstractListModel() {
            String[] values = songs;
            public int getSize() {
                return values.length;
            }
            public Object getElementAt(int index) {
                return values[index];
            }
        });

        lstSongs.setModel(new AbstractListModel() {
            String[] values = songs;
            public int getSize() {
                return values.length;
            }
            public Object getElementAt(int index) {
                return values[index];
            }
        });
    }

    /*FUNCTION: importSong
      * Purpose: when this function is accessed it will open a dialog
      * box that will have the user give the program a file to add to
      * the library.
      *
      * NOTE: This function is not completed...
     */
    public static void importSong()
    {
    }
}
