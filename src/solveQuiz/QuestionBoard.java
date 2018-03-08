/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveQuiz;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.shape.Circle;
import javax.swing.*;

/**
 *
 * @author Hamid
 */
public class QuestionBoard extends JPanel {

    static Color color = Color.BLACK;
    static String shape = "normal";
    static boolean markerCheck = false;
    static RandomAccessFile output;
    static PadDraw3 drawPad;
    static String fileName;

    static ArrayList<String> shapesList = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        QuestionBoard obj = new QuestionBoard("1");

        obj.setBackground(Color.BLACK);
        obj.setBounds(5, 100, 390, 220);

        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(900, 600);
        //sets the size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(obj);
        //makes it so you can close

        frame.setVisible(true);
        //makes it so you can see it
    }

    QuestionBoard(String fileName) throws IOException {
        this.fileName = fileName;
        createFile();
        //for showing the previous content
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            reloadFile(fileName + ".text");
                        } catch (Exception exx) {
                        }
                    }
                });
//        output.setLength(0);

        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(null);
        drawPad = new PadDraw3();
        //white board size
        drawPad.setBounds(10, 10, 470, 330);
        boardPanel.add(drawPad);

        add(boardPanel, BorderLayout.CENTER);

        JPanel optionsMenu = new JPanel();
        //menu optons overall size
        optionsMenu.setPreferredSize(new Dimension(100, 10));

        JButton redButton = new JButton();
        redButton.setBackground(Color.RED);
        redButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuestionBoard.color = Color.RED;
            }

        });

        JButton blackButton = new JButton();
        blackButton.setBackground(Color.BLACK);
        blackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuestionBoard.color = Color.BLACK;
            }
        });

        JButton blueButton = new JButton();
        blueButton.setBackground(Color.BLUE);
        blueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuestionBoard.color = Color.BLUE;
            }
        });

        JButton greenButton = new JButton();
        greenButton.setBackground(Color.GREEN);
        greenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuestionBoard.color = Color.GREEN;
            }
        });
        //clear the whole board
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    output.setLength(0);
                    drawPad.clear();
                } catch (IOException ex) {
                    Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        JButton chooseColor = new JButton("Color");
        chooseColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuestionBoard.color = JColorChooser.showDialog(null, "Choose Color", color);
            }
        });

        JButton normal = new JButton("Normal");
        normal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shape = "normal";
            }
        });

        JButton marker = new JButton("F/Marker");
        marker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shape = "normal";
                if (markerCheck) {
                    markerCheck = false;
                    marker.setText("F/Marker");
                } else {
                    markerCheck = true;
                    marker.setText("T/Marker");
                }
            }
        });

        JButton line = new JButton("Line");
        line.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shape = "line";
            }
        });
        JButton circle = new JButton("Circle");
        circle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shape = "circle";
            }
        });
        JButton oval = new JButton("Oval");
        oval.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shape = "oval";
            }
        });
        JButton rectangle = new JButton("Rectangle");
        rectangle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shape = "rectangle";
            }
        });
        JButton razor = new JButton("Razor");
        razor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shape = "razor";
            }
        });

        JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    removeLastShape();
                    drawPad.clear();
                    reloadFile(fileName + ".text");
                } catch (IOException ex) {
                    Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JButton redo = new JButton("Redo");
        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addLastShape();
                } catch (IOException ex) {
                    Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        ///setting size for each option menu button 
        blackButton.setPreferredSize(new Dimension(16, 16));
        redButton.setPreferredSize(new Dimension(16, 16));
        blueButton.setPreferredSize(new Dimension(16, 16));
        greenButton.setPreferredSize(new Dimension(16, 16));
        clearButton.setPreferredSize(new Dimension(80, 25));
        chooseColor.setPreferredSize(new Dimension(80, 25));
        normal.setPreferredSize(new Dimension(80, 25));
        marker.setPreferredSize(new Dimension(80, 25));
        line.setPreferredSize(new Dimension(80, 25));
        circle.setPreferredSize(new Dimension(80, 25));
        oval.setPreferredSize(new Dimension(80, 25));
        rectangle.setPreferredSize(new Dimension(80, 25));
        razor.setPreferredSize(new Dimension(80, 25));
        undo.setPreferredSize(new Dimension(80, 25));
        redo.setPreferredSize(new Dimension(80, 25));

//adds the buttons to the optionsMenu
        optionsMenu.add(greenButton);
        optionsMenu.add(blueButton);
        optionsMenu.add(blackButton);
        optionsMenu.add(redButton);
        optionsMenu.add(clearButton);
        optionsMenu.add(chooseColor);
        optionsMenu.add(normal);
        optionsMenu.add(marker);
        optionsMenu.add(line);
        optionsMenu.add(circle);
        optionsMenu.add(oval);
        optionsMenu.add(rectangle);
        optionsMenu.add(razor);
        optionsMenu.add(undo);
        optionsMenu.add(redo);

        add(optionsMenu, BorderLayout.WEST);

    }

    class PadDraw3 extends JComponent {

        Image image;
        Graphics2D graphics2D;
        int currentX, currentY, oldX, oldY;
        private Rectangle selection;
        private Rectangle rectangleTemp;
        private Rectangle rectangleRazor;

        public PadDraw3() {
            setDoubleBuffered(false);

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (graphics2D != null) {
                        if (shape.equals("normal")) {
                            oldX = e.getX();
                            oldY = e.getY();
                            try {

                                output.seek(output.length());
                                output.write("!@#@!".getBytes());
                                output.write(",NORMAL".getBytes());
                                output.write(("," + QuestionBoard.color.getRed() + "," + QuestionBoard.color.getGreen() + "," + QuestionBoard.color.getBlue()).getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else if (shape.equals("line")) {
                            oldX = e.getX();
                            oldY = e.getY();
                            try {

                                output.seek(output.length());
                                output.write("!@#@!".getBytes());
                                output.write(",LINE".getBytes());
                                output.write(("," + QuestionBoard.color.getRed() + "," + QuestionBoard.color.getGreen() + "," + QuestionBoard.color.getBlue()).getBytes());

                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else if (shape.equals("circle")) {
                            oldX = e.getX();
                            oldY = e.getY();
                            try {
                                output.seek(output.length());
                                output.write("!@#@!".getBytes());
                                output.write(",CIRCLE".getBytes());
                                output.write(("," + QuestionBoard.color.getRed() + "," + QuestionBoard.color.getGreen() + "," + QuestionBoard.color.getBlue()).getBytes());

                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (shape.equals("oval")) {
                            oldX = e.getX();
                            oldY = e.getY();
                            try {
                                output.seek(output.length());
                                output.write("!@#@!".getBytes());
                                output.write(",OVAL".getBytes());
                                output.write(("," + QuestionBoard.color.getRed() + "," + QuestionBoard.color.getGreen() + "," + QuestionBoard.color.getBlue()).getBytes());

                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (shape.equals("rectangle")) {
                            oldX = e.getX();
                            oldY = e.getY();
                            selection = new Rectangle(e.getPoint());
                            try {
                                output.seek(output.length());
                                output.write("!@#@!".getBytes());
                                output.write(",RECTANGLE".getBytes());
                                output.write(("," + QuestionBoard.color.getRed() + "," + QuestionBoard.color.getGreen() + "," + QuestionBoard.color.getBlue()).getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else if (shape.equals("razor")) {

                            try {
                                output.seek(output.length());
                                output.write("!@#@!".getBytes());
                                output.write(",RAZOR".getBytes());
                                output.write(("," + 255 + "," + 255 + "," + 255).getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("Pressing Razor");
                        }
                    }

                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {

                    if (graphics2D != null) {
                        if (shape.equals("normal")) {
                            currentX = e.getX();
                            currentY = e.getY();
                            graphics2D.setColor(QuestionBoard.color);
                            try {
                                graphics2D.drawLine(oldX, oldY, currentX, currentY);
                                output.seek(output.length());
                                output.write(("," + oldX + "," + oldY + "," + currentX + "," + currentY).getBytes());
                                if (markerCheck) {
                                    graphics2D.drawLine(oldX + 1, oldY + 1, currentX + 1, currentY + 1);
                                    graphics2D.drawLine(oldX - 1, oldY - 1, currentX - 1, currentY - 1);
                                    graphics2D.drawLine(oldX + 2, oldY + 2, currentX + 2, currentY + 2);
                                    graphics2D.drawLine(oldX - 2, oldY - 2, currentX - 2, currentY - 2);
                                    output.write(("," + (oldX + 1) + "," + (oldY + 1) + "," + (currentX + 1) + "," + (currentY + 1)).getBytes());
                                    output.write(("," + (oldX + 2) + "," + (oldY + 2) + "," + (currentX + 2) + "," + (currentY + 2)).getBytes());
                                    output.write(("," + (oldX - 1) + "," + (oldY - 1) + "," + (currentX - 1) + "," + (currentY - 1)).getBytes());
                                    output.write(("," + (oldX - 2) + "," + (oldY - 2) + "," + (currentX - 2) + "," + (currentY - 2)).getBytes());

                                }

                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            repaint();
//                            System.out.println("Dragging Normal Line:" + oldX + ":" + oldY + ":" + currentX + ":" + currentY);
                            oldX = currentX;
                            oldY = currentY;

                        } else if (shape.equals("line")) {

                            drawPad.clear();
                            try {
                                reloadFile(fileName + ".text");
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            currentX = e.getX();
                            currentY = e.getY();
                            graphics2D.setColor(QuestionBoard.color);

                            graphics2D.drawLine(oldX, oldY, currentX, currentY);

                            repaint();

                        } else if (shape.equals("circle")) {
                            drawPad.clear();
                            try {
                                reloadFile(fileName + ".text");
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            currentX = e.getX();
                            currentY = e.getY();
                            int r = Math.abs(Math.abs(oldX - currentX));
                            graphics2D.setColor(QuestionBoard.color);

                            graphics2D.drawOval(oldX, oldY, r, r);

                            repaint();
                        } else if (shape.equals("oval")) {
                            drawPad.clear();
                            try {
                                reloadFile(fileName + ".text");
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            currentX = e.getX();
                            currentY = e.getY();
                            graphics2D.setColor(QuestionBoard.color);
                            graphics2D.drawOval(oldX, oldY, Math.abs(oldX - currentX), Math.abs(oldY - currentY));
                            repaint();
                        } else if (shape.equals("rectangle")) {
                            drawPad.clear();
                            try {
                                reloadFile(fileName + ".text");
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            int width = Math.max(selection.x - e.getX(), e.getX() - selection.x);
                            int height = Math.max(selection.y - e.getY(), e.getY() - selection.y);
                            graphics2D.setColor(QuestionBoard.color);

                            selection.setSize(width, height);
                            graphics2D.draw(selection);
                            repaint();

                        } else if (shape.equals("razor")) {
                            Thread queryThread = new Thread() {
                                public void run() {
                                    currentX = e.getX();
                                    currentY = e.getY();
                                    selection = new Rectangle(currentX, currentY, 15, 15);
                                    graphics2D.setColor(Color.white);
                                    graphics2D.fill(selection);
                                    //border color
                                    graphics2D.setColor(Color.BLACK);
                                    graphics2D.draw(selection);
                                    repaint();
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    selection = new Rectangle(e.getX() - 1, e.getY() - 1, 17, 17);
                                    graphics2D.setColor(Color.white);
                                    graphics2D.fill(selection);
                                    //border color
                                    graphics2D.setColor(Color.white);
                                    graphics2D.draw(selection);
                                    repaint();
                                    try {
                                        output.seek(output.length());
                                        output.write(("," + (e.getX() - 1) + "," + (e.getY() - 1) + "," + 17 + "," + 17).getBytes());
                                    } catch (IOException ex) {
                                        Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            };
                            queryThread.start();

                        }

                    }

                }

            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {

                    if (graphics2D != null) {
                        if (shape.equals("normal")) {
                            try {
                                output.seek(output.length());
                                output.write("!@#@!".getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (shape.equals("line")) {

                            try {
                                output.seek(output.length());
                                output.write(("," + oldX + "," + oldY + "," + currentX + "," + currentY).getBytes());
                                output.write("!@#@!".getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (shape.equals("circle")) {
                            currentX = e.getX();
                            currentY = e.getY();
                            int r = Math.abs(Math.abs(oldX - currentX));
                            try {
                                output.seek(output.length());
                                output.write(("," + oldX + "," + oldY + "," + r + "," + r).getBytes());
                                output.write("!@#@!".getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else if (shape.equals("oval")) {
                            currentX = e.getX();
                            currentY = e.getY();
                            try {
                                output.seek(output.length());
                                output.write(("," + oldX + "," + oldY + "," + Math.abs(oldX - currentX) + ","
                                        + Math.abs(oldY - currentY)).getBytes());
                                output.write("!@#@!".getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else if (shape.equals("rectangle")) {
                            try {
                                output.seek(output.length());
                                output.write(("," + selection.getX() + "," + selection.getY() + "," + selection.getWidth() + "," + selection.getHeight()).getBytes());
                                output.write("!@#@!".getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else if (shape.equals("razor")) {
                            System.out.println("Relesing Razor:" + currentX + ":" + currentY + ":" + 15 + ":" + 15);
                            try {
                                output.seek(output.length());
                                output.write("!@#@!".getBytes());
                            } catch (IOException ex) {
                                Logger.getLogger(QuestionBoard.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });
        }

        public void paintComponent(Graphics g) {
            if (image == null) {
                image = createImage(getSize().width, getSize().height);
                graphics2D = (Graphics2D) image.getGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                clear();

            }

            g.drawImage(image, 0, 0, null);
        }

        public void clear() {

            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(color);
            repaint();
        }

        private void showNormalLine(int[] coordinates) {
            graphics2D.setPaint(color);
            graphics2D.drawLine(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
            repaint();
        }

        private void showLine(int[] coordinates) {
            graphics2D.setColor(QuestionBoard.color);
            graphics2D.drawLine(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
            repaint();

        }

        private void showCircle(int[] coordinates) {
            graphics2D.setColor(QuestionBoard.color);
            graphics2D.drawOval(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
            repaint();

        }

        private void showRectangle(int[] coordinates) {

            rectangleTemp = new Rectangle(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
            graphics2D.setColor(QuestionBoard.color);
            graphics2D.draw(rectangleTemp);
            repaint();
        }

        private void showRazor(int[] coordinates) {
            rectangleRazor = new Rectangle(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
            graphics2D.setColor(Color.white);
            graphics2D.fill(rectangleRazor);
            graphics2D.setColor(Color.white);
            graphics2D.draw(rectangleRazor);
            repaint();

        }

    }

    public static void createFile() {
        //creating file
        try {
            output = new RandomAccessFile(fileName + ".text", "rw");
        } catch (Exception eX) {
            System.out.println("code not working");
        }
    }

    public static void closeFile() {
        try {

            output.close();
        } catch (Exception eX) {
            System.out.println("code not working");
        }

    }

    public static void addLastShape() throws IOException {
        if (shapesList.size() > 0) {
            int previousColorRed = QuestionBoard.color.getRed();//stores the previous color so that it can be changed back
            int previousColorGreen = QuestionBoard.color.getGreen();//stores the previous color so that it can be changed back
            int previousColorBlue = QuestionBoard.color.getBlue();//stores the previous color so that it can be changed back
            String previousShape = shape;//stores the previous shape
            //drawPad.clear();
            String oneFullShape = "";
            String oneFullShapeTemp = "!@#@!";
            StringTokenizer oneFullShapeTokens = new StringTokenizer(shapesList.get(shapesList.size() - 1), "!@#@!");
            while (oneFullShapeTokens.hasMoreTokens()) {
                oneFullShape = oneFullShapeTokens.nextToken();
                oneFullShape = oneFullShape.trim();
                System.out.println(oneFullShape);

                drawOneShape(oneFullShape);
                oneFullShapeTemp += oneFullShape;
            }
            oneFullShapeTemp += "!@#@!";

            output.seek(output.length());
            output.write(oneFullShapeTemp.getBytes());
            shapesList.remove(shapesList.size() - 1);
            QuestionBoard.color = new Color((previousColorRed), (previousColorGreen), (previousColorBlue));//set Prevois color to back normal
            shape = previousShape;
        }

    }

    public static void removeLastShape() throws IOException {
        //creating file
        String fullData = "", remainingShapes, lastShape;
        fullData = readFile(fileName + ".text");
        fullData = fullData.replaceAll(" ", "");
        String oneFullShape = "";//!@#@!,normal,color,5,5,6,6,!@#@!//without !@#@!

        if (!fullData.trim().isEmpty()) {

            remainingShapes = fullData.substring(0, fullData.lastIndexOf("!@#@!"));

            lastShape = remainingShapes.substring(remainingShapes.lastIndexOf("!@#@!"), remainingShapes.length());
            remainingShapes = remainingShapes.substring(0, remainingShapes.lastIndexOf("!@#@!"));
            lastShape += "!@#@!";

            output.setLength(remainingShapes.length());
            output.seek(output.length());

            shapesList.add(lastShape);
        }
    }

    //takes //!@#@!,normal,color,5,5,6,6,!@#@!//without !@#@!
    public static void drawOneShape(String oneFullShape) throws IOException {
        String oneShapeValue = "";//N
        int[] coordinates = new int[4];
        String oneShapeType = "";//normal,rectangle
        String oneShapeColorRED = "";//color
        String oneShapeColorGREEN = "";//color
        String oneShapeColorBLUE = "";//color

        if (!oneFullShape.isEmpty()) {
            StringTokenizer oneShapeTokens = new StringTokenizer(oneFullShape, ",");
            oneShapeType = oneShapeTokens.nextToken();//normal,rectangle

            oneShapeColorRED = oneShapeTokens.nextToken().trim();//color
            oneShapeColorGREEN = oneShapeTokens.nextToken().trim();//color
            oneShapeColorBLUE = oneShapeTokens.nextToken().trim();//color
            oneShapeType = oneShapeType.trim();
            QuestionBoard.color = new Color((int) Integer.parseInt(oneShapeColorRED), (int) Integer.parseInt(oneShapeColorGREEN), (int) Integer.parseInt(oneShapeColorBLUE));

            if (!oneShapeType.isEmpty()) {
                if (oneShapeType.equals("NORMAL")) {
                    shape = "normal";
                    while (oneShapeTokens.hasMoreTokens()) {
                        for (int i = 0; i < 4; i++) {
                            oneShapeValue = oneShapeTokens.nextToken();
                            oneShapeValue = oneShapeValue.trim();
                            if (!oneShapeValue.isEmpty()) {
                                coordinates[i] = Integer.parseInt(oneShapeValue);
                            }

                        }
                        drawPad.showNormalLine(coordinates.clone());
                    }
                } else if (oneShapeType.equals("LINE")) {
                    shape = "line";
                    while (oneShapeTokens.hasMoreTokens()) {
                        for (int i = 0; i < 4; i++) {
                            oneShapeValue = oneShapeTokens.nextToken();
                            oneShapeValue = oneShapeValue.trim();
                            if (!oneShapeValue.isEmpty()) {
                                coordinates[i] = (int) Double.parseDouble(oneShapeValue);
                            }
                        }
                        drawPad.showLine(coordinates.clone());
                    }
                } else if (oneShapeType.equals("CIRCLE") || oneShapeType.equals("OVAL")) {
                    shape = oneShapeType.toLowerCase();
                    while (oneShapeTokens.hasMoreTokens()) {
                        for (int i = 0; i < 4; i++) {
                            oneShapeValue = oneShapeTokens.nextToken();
                            oneShapeValue = oneShapeValue.trim();
                            if (!oneShapeValue.isEmpty()) {
                                coordinates[i] = (int) Double.parseDouble(oneShapeValue);
                            }
                        }
                        drawPad.showCircle(coordinates.clone());
                    }
                } else if (oneShapeType.equals("RECTANGLE")) {
                    shape = "rectangle";
                    while (oneShapeTokens.hasMoreTokens()) {
                        for (int i = 0; i < 4; i++) {
                            oneShapeValue = oneShapeTokens.nextToken();
                            oneShapeValue = oneShapeValue.trim();
                            if (!oneShapeValue.isEmpty()) {
                                coordinates[i] = (int) Double.parseDouble(oneShapeValue);
                            }
                        }
                        drawPad.showRectangle(coordinates.clone());
                    }
                } else if (oneShapeType.equals("RAZOR")) {
                    shape = "razor";
                    while (oneShapeTokens.hasMoreTokens()) {
                        for (int i = 0; i < 4; i++) {
                            oneShapeValue = oneShapeTokens.nextToken();
                            oneShapeValue = oneShapeValue.trim();
                            if (!oneShapeValue.isEmpty()) {
                                coordinates[i] = (int) Double.parseDouble(oneShapeValue);
                            }
                        }
                        drawPad.showRazor(coordinates.clone());
                    }
                }

            }
        }

    }

    public static void reloadFile(String filePath) throws IOException {

        int previousColorRed = QuestionBoard.color.getRed();//stores the previous color so that it can be changed back
        int previousColorGreen = QuestionBoard.color.getGreen();//stores the previous color so that it can be changed back
        int previousColorBlue = QuestionBoard.color.getBlue();//stores the previous color so that it can be changed back
        String previousShape = shape;//stores the previous shape
        String fileData = readFile(filePath);
        String oneFullShape = "";//!@#@!,normal,color,5,5,6,6,!@#@!//without !@#@!

        StringTokenizer oneFullShapeTokens = new StringTokenizer(fileData, "!@#@!");
        while (oneFullShapeTokens.hasMoreTokens()) {
            oneFullShape = oneFullShapeTokens.nextToken();
            oneFullShape = oneFullShape.trim();

            if (!oneFullShape.trim().isEmpty()) {
                try {
                    drawOneShape(oneFullShape);
                } catch (Exception e) {
                }
            }

        }
        QuestionBoard.color = new Color((previousColorRed), (previousColorGreen), (previousColorBlue));//set Prevois color to back normal
        shape = previousShape;
    }

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            return sb.toString();
        } finally {
            br.close();
        }
    }
}
