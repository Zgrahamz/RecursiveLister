import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;

public class RecursiveGenerator extends JFrame implements ActionListener {
    JPanel mainPnl, searchPnl, displayPnlC, controlPnl;
    JTextArea textAreaC;
    JScrollPane scrollPaneC;
    JButton searchBtn, quitBtn;

    JFileChooser chooser = new JFileChooser();
    File selectedFile;
    String rec = "";
    Path path = Paths.get(System.getProperty("user.dir"));
    ArrayList<String> searchList = new ArrayList<>();

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            try (Stream<String> lines = Files.lines(path)) {
                File workingDirectory = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(workingDirectory);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = chooser.getSelectedFile();
                    Path file = selectedFile.toPath();
                    InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    int line = 0;
                    while (reader.ready()) {
                        rec = reader.readLine();
                        searchList.add(rec);
                        textAreaC.append(rec + "\n");
                        line++;
                        System.out.printf("\nLine %4d %-60s ", line, rec);
                    }

                    reader.close();
                    System.out.println("\n\nData file read!");

                } else {
                    System.out.println("No file selected!!! ... exiting.\nRun the program again and select a file.");
                }
            } catch (FileNotFoundException ae) {
                System.out.println("File not found!!!");
                JOptionPane.showMessageDialog(null, "Error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ae) {
                JOptionPane.showMessageDialog(null, "Error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


        if (e.getSource() == quitBtn) {
            int closer = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (closer == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public RecursiveGenerator() {
        setTitle("Recursive File Lister");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        add(mainPnl);
        createSearchPnl();
        createDisplayPanelC();
        createControlPnl();
        setVisible(true);

    }

    public void createSearchPnl() {
        searchPnl = new JPanel();
        searchPnl.setLayout(new BorderLayout());
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(this);
        searchPnl.add(searchBtn, BorderLayout.EAST);
        mainPnl.add(searchPnl, BorderLayout.NORTH);
    }

    public void createDisplayPanelC() {
        displayPnlC = new JPanel();
        textAreaC = new JTextArea(50, 20);
        scrollPaneC = new JScrollPane(textAreaC);
        displayPnlC.add(scrollPaneC);
        mainPnl.add(displayPnlC, BorderLayout.CENTER);
    }

    public void createControlPnl() {
        controlPnl = new JPanel();
        quitBtn = new JButton("Quit");
        quitBtn.addActionListener(this);
        controlPnl.add(quitBtn);
        mainPnl.add(controlPnl, BorderLayout.SOUTH);
    }
}
