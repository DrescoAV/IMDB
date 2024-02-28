package org.example;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})

public class GUI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JPanel menuPanel;
    private boolean isMenuVisible = false;

    private boolean isAnimating = false;

    private static final String MOVIES = "Movies";
    private static final String ACTORS = "Actors";
    private static final String SERIES = "Series";

    private static final String MAIN_PANEL = "MainPanel";
    private static final String DETAILS_PANEL = "DetailsPanel";

    private int currentProductionIndex = 0;

    private int currentActorIndex = 0;
    private List<Production> productions;

    private List<Actor> actors;
    private JLabel titleLabel;
    private JButton upButton, downButton;

    private JPanel notificationsPanel;
    private boolean isNotificationsPanelVisible = false;

    private boolean viewingActors = false;

    JPanel notificationContentPanel;

    private JPanel reviewPanel;
    private boolean isReviewPanelVisible = false;

    private static GUI instance;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private JComboBox<Integer> ratingComboBox;
    private JTextArea commentTextArea;

    private int numberOfReviewPanelToggles = 0;

    private JButton addToFavoritesButton;

    private JButton viewMyRequestsButton;
    private JButton createRequestButton;

    private JButton modifyDatabaseButton;

    private JButton modifyUserButton;

    private JButton viewToDoRequests;

    private DefaultListModel<ActorPerformance> performanceListModel = new DefaultListModel<>();
    private JFrame addWindowFrame;
    private JTextArea biographyArea;
    private JButton removePerformanceButton;

    private JButton removeUserButton;

    JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"REGULAR", "CONTRIBUTOR", "ADMIN"});
    JTextField nameField = new JTextField(20);
    JTextField countryField = new JTextField(20);
    JTextField emailField = new JTextField(20);
    JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(18, 13, 120, 1));
    JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"F", "M", "N"});
    JFormattedTextField birthdayField = new JFormattedTextField("####-##-##");

    private GUI() {
        setGlobalOptionPaneUI();
        runGUI();
        createSlidingMenuPanel();
        createNotificationsPanel();
        createReviewPanel();
        notificationsPanel.setLocation(screenSize.width * 2, 0);
        reviewPanel.setLocation(0, screenSize.height * 2);
    }

    public static GUI getInstance() {
        if (instance == null) {
            instance = new GUI();
        }
        return instance;
    }

    private void runGUI() {
        ImageIcon icon = new ImageIcon("src/main/resources/images/logo.png");
        Image image = icon.getImage();

        frame = new JFrame("IMDB");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(image);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        frame.setLocationRelativeTo(null);

        JPanel startPanelBeforeLogin = createStartPanelBeforeLogin();
        JPanel loginPanel = createLoginPanel();

        cardPanel.add(startPanelBeforeLogin, "StartBeforeLogin");

        cardPanel.add(loginPanel, "Login");

        frame.add(cardPanel);
        cardLayout.show(cardPanel, "StartBeforeLogin");


        frame.setVisible(true);
    }

    private void setGlobalOptionPaneUI() {
        UIManager.put("OptionPane.background", Color.BLACK);
        UIManager.put("Panel.background", Color.BLACK);
        UIManager.put("OptionPane.messageForeground", Color.ORANGE);

        UIManager.put("Button.background", Color.ORANGE);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));

        UIManager.put("Label.foreground", Color.ORANGE);
        UIManager.put("Label.font", new Font("Arial", Font.BOLD, 12));

    }

    private JPanel createStartPanelBeforeLogin() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(20, 0, 20, 0);

        panel.setBackground(Color.BLACK);

        JLabel welcomeLabel = new JLabel("Welcome to IMDB!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.ORANGE);

        panel.add(welcomeLabel, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setForeground(Color.BLACK);
        loginButton.setBackground(Color.ORANGE);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(e -> {
            IMDB.setLoggedIn(false);
            IMDB.setCurrentUser(null);
            cardLayout.show(cardPanel, "Login");
        });

        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(Color.ORANGE.darker());
            }

            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(Color.ORANGE);
            }
        });

        panel.add(loginButton, gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return panel;
    }


    private JPanel createStartPanelAfterLogin() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.LINE_AXIS));
        headerPanel.setBackground(Color.BLACK);

        headerPanel.add(Box.createHorizontalGlue());

        JButton menuButton = createMenuButton();
        headerPanel.add(menuButton);

        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JLabel logoLabel = createLogoLabel();
        headerPanel.add(logoLabel);

        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JPanel searchPanel = createSearchPanel();
        headerPanel.add(searchPanel);

        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton favoritesButton = createFavoritesButton();
        headerPanel.add(favoritesButton);

        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton notificationsButton = createIconButton("src/main/resources/images/notification.png", 25, 25);
        headerPanel.add(notificationsButton);
        notificationsButton.addActionListener(e -> toggleNotificationsPanelVisibility());

        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton toggleDisplayButton = new JButton("Go to Actors");
        styleButton(toggleDisplayButton);
        toggleDisplayButton.addActionListener(e -> {
            viewingActors = !viewingActors;
            if (viewingActors) {
                toggleDisplayButton.setText("Go to Productions");
                updateTitleDisplayForActors();

                removeAllActionListeners(upButton);
                removeAllActionListeners(downButton);

                upButton.addActionListener(ae -> navigateActors(-1));
                downButton.addActionListener(ae -> navigateActors(1));
            } else {
                toggleDisplayButton.setText("Go to Actors");
                updateTitleDisplayForProductions();

                removeAllActionListeners(upButton);
                removeAllActionListeners(downButton);

                upButton.addActionListener(ae -> navigateProductions(-1));
                downButton.addActionListener(ae -> navigateProductions(1));
            }
            updateFavouriteButtonIcon();

        });


        headerPanel.add(toggleDisplayButton);

        headerPanel.add(Box.createHorizontalGlue());

        panel.add(headerPanel, BorderLayout.NORTH);

        IMDB imdb = IMDB.getInstance();
        productions = imdb.sortProductionsByNumberOfRatings();
        actors = imdb.sortActorsByName();

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.BLACK.brighter());

        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setForeground(Color.ORANGE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        updateTitleDisplayForProductions();

        makeTitleLabelClickable();

        titlePanel.add(titleLabel, BorderLayout.CENTER);

        upButton = new JButton("Up");
        styleButton(upButton);
        upButton.addActionListener(e -> navigateProductions(-1));
        titlePanel.add(upButton, BorderLayout.NORTH);

        downButton = new JButton("Down");
        styleButton(downButton);
        downButton.addActionListener(e -> navigateProductions(1));
        titlePanel.add(downButton, BorderLayout.SOUTH);

        panel.add(titlePanel, BorderLayout.CENTER);

        favoritesButton = createAddToFavoritesButton();
        titlePanel.add(favoritesButton, BorderLayout.EAST);
        updateFavouriteButtonIcon();
        return panel;
    }

    private void removeAllActionListeners(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
    }


    private void createReviewPanel() {
        reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        reviewPanel.setBackground(Color.DARK_GRAY);

        reviewPanel.setSize(frame.getWidth(), frame.getHeight() / 4);
        reviewPanel.setLocation(0, frame.getHeight());

        JLabel ratingLabel = new JLabel("Rating (1-10):");
        ratingLabel.setForeground(Color.ORANGE);
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ratingComboBox = new JComboBox<>(IntStream.rangeClosed(1, 10).boxed().toArray(Integer[]::new));
        ratingComboBox.setBackground(Color.BLACK);
        ratingComboBox.setForeground(Color.ORANGE);
        ratingComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleComboBoxInteger(ratingComboBox);

        JLabel commentLabel = new JLabel("Comment:");
        commentLabel.setForeground(Color.ORANGE);
        commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentTextArea = new JTextArea(5, 20);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setBackground(Color.BLACK);
        commentTextArea.setForeground(Color.ORANGE);
        JScrollPane commentScrollPane = new JScrollPane(commentTextArea);
        styleTextArea(commentTextArea);
        commentScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton submitButton = new JButton("Submit");
        styleButton(submitButton);
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, submitButton.getMinimumSize().height));

        submitButton.addActionListener(e ->
                submitReview(productions.get(currentProductionIndex), ratingComboBox.getSelectedItem(), commentTextArea.getText())
        );

        reviewPanel.add(ratingLabel);
        reviewPanel.add(ratingComboBox);
        reviewPanel.add(commentLabel);
        reviewPanel.add(commentScrollPane);
        reviewPanel.add(submitButton, BorderLayout.SOUTH);
        reviewPanel.add(Box.createVerticalGlue());

        frame.getLayeredPane().add(reviewPanel, JLayeredPane.MODAL_LAYER);
    }


    private void toggleReviewPanelVisibility() {
        if (!isAnimating) {
            numberOfReviewPanelToggles++;
            int panelHeight;
            if (numberOfReviewPanelToggles < 3)
                panelHeight = (int) (frame.getHeight() * 0.30);
            else
                panelHeight = (int) (frame.getHeight() * 0.40);
            reviewPanel.setSize(frame.getWidth(), panelHeight);

            int startY = isReviewPanelVisible ? frame.getHeight() - panelHeight : frame.getHeight();
            int endY = isReviewPanelVisible ? frame.getHeight() : frame.getHeight() - panelHeight;

            slideReviewPanel(startY, endY);
            isReviewPanelVisible = !isReviewPanelVisible;
        }
    }

    private void slideReviewPanel(int start, int end) {
        isAnimating = true;
        Timer timer = new Timer(2, new ActionListener() {
            int yPosition = start;

            public void actionPerformed(ActionEvent e) {
                if ((end > start && yPosition < end) || (end < start && yPosition > end)) {
                    yPosition += (end > start) ? 5 : -5;
                    reviewPanel.setLocation(0, yPosition);
                } else {
                    ((Timer) e.getSource()).stop();
                    isAnimating = false;
                    reviewPanel.setLocation(0, end);
                    reviewPanel.setVisible(end != frame.getHeight());
                }
            }
        });
        timer.start();
    }


    private void createNotificationsPanel() {
        Color backgroundColor = Color.ORANGE;
        Color textColor = Color.BLACK;

        Border blackLineBorder = new LineBorder(Color.BLACK);

        notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BorderLayout());
        notificationsPanel.setBackground(backgroundColor);
        notificationsPanel.setBorder(blackLineBorder);

        notificationContentPanel = new JPanel();
        notificationContentPanel.setLayout(new BoxLayout(notificationContentPanel, BoxLayout.Y_AXIS));
        notificationContentPanel.setBackground(backgroundColor);
        notificationContentPanel.setForeground(textColor);


        User currentUser = IMDB.getCurrentUser();
        if (currentUser != null) {
            List<String> notifications = currentUser.getNotifications();
            for (String notification : notifications) {
                JLabel notificationLabel = new JLabel(notification);
                notificationLabel.setForeground(textColor);
                notificationContentPanel.add(notificationLabel);
            }
        } else {
            JLabel noNotificationsLabel = new JLabel("No notifications available");
            noNotificationsLabel.setForeground(textColor);
            notificationContentPanel.add(noNotificationsLabel);
        }
        JScrollPane scrollPane = new JScrollPane(notificationContentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(backgroundColor);

        notificationsPanel.add(scrollPane, BorderLayout.CENTER);


        setupMousePositionChecker();
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (!notificationsPanel.contains(e.getPoint()) && isNotificationsPanelVisible) {
                    toggleNotificationsPanelVisibility();
                }
            }
        };

        notificationsPanel.addMouseListener(mouseAdapter);
        scrollPane.addMouseListener(mouseAdapter);

        int panelWidth = frame.getWidth() / 3;
        int panelHeight = frame.getHeight() / 3;
        notificationsPanel.setBounds(frame.getWidth(), 0, panelWidth, panelHeight);

        frame.getLayeredPane().add(notificationsPanel, JLayeredPane.POPUP_LAYER);
    }


    private void setupMousePositionChecker() {
        Timer mousePositionTimer = new Timer(100, e -> {
            if (isNotificationsPanelVisible && notificationsPanel.isShowing()) {
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(mousePoint, notificationsPanel.getParent());

                if (!notificationsPanel.getBounds().contains(mousePoint)) {
                    toggleNotificationsPanelVisibility();
                }
            }
        });

        mousePositionTimer.start();
    }

    private void toggleNotificationsPanelVisibility() {
        if (!isAnimating) {
            int panelWidth = notificationsPanel.getWidth();
            if (isNotificationsPanelVisible) {
                slideNotificationsPanel(frame.getWidth() - panelWidth, frame.getWidth());
            } else {
                slideNotificationsPanel(frame.getWidth(), frame.getWidth() - panelWidth);
                notificationsPanel.setVisible(true);
            }
            isNotificationsPanelVisible = !isNotificationsPanelVisible;
        }
    }

    public void updateNotificationsPanel() {

        User<?> currentUser = IMDB.getCurrentUser();

        notificationContentPanel.removeAll();

        List<String> notifications = currentUser.getNotifications();
        for (String notification : notifications) {
            JLabel notificationLabel = new JLabel(notification);
            notificationLabel.setForeground(Color.BLACK);
            notificationContentPanel.add(notificationLabel);
        }
        showPopupNotification("New Notification: " + notifications.get((notifications.size()) - 1));
        notificationContentPanel.revalidate();
        notificationContentPanel.repaint();
    }

    private void showPopupNotification(String message) {
        JWindow popup = new JWindow();
        popup.setSize(200, 100);
        popup.setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.ORANGE);
        label.setForeground(Color.BLACK);

        popup.add(label);

        Point centerPoint = frame.getLocationOnScreen();
        Dimension frameSize = frame.getSize();
        int x = centerPoint.x + (frameSize.width - popup.getWidth()) / 2;
        int y = centerPoint.y + (frameSize.height - popup.getHeight()) / 2;

        popup.setLocation(x, y);

        popup.setVisible(true);

        new Timer(1000, e -> popup.dispose()).start();
    }

    private void slideNotificationsPanel(int start, int end) {
        isAnimating = true;
        int panelHeight = notificationsPanel.getHeight();
        notificationsPanel.setSize(frame.getWidth() / 3, panelHeight);
        notificationsPanel.setLocation(start, 0);
        Timer timer = new Timer(2, new ActionListener() {
            int xPosition = start;

            public void actionPerformed(ActionEvent e) {
                if ((end > start && xPosition < end) || (end < start && xPosition > end)) {
                    xPosition += (end > start) ? 10 : -10;
                    notificationsPanel.setLocation(xPosition, 0);
                } else {
                    ((Timer) e.getSource()).stop();
                    isAnimating = false;
                    if (end == frame.getWidth()) {
                        notificationsPanel.setVisible(false);
                    }
                }
            }
        });
        timer.start();
    }


    private JButton createIconButton(String iconPath, int width, int height) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(img));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    private void changeButtonIcon(JButton button, String iconPath, int width, int height) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(img);
        button.setIcon(scaledIcon);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
    }


    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.ORANGE);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.ORANGE.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.ORANGE);
            }
        });
    }


    private void submitReview(Production production, Object rating, String comment) {
        if (production != null && rating instanceof Integer && !commentTextArea.getText().equals("Type message here...")) {
            production.addOrUpdateRating(IMDB.getCurrentUser().getUsername(), (int) rating, comment);
            JOptionPane.showMessageDialog(frame, "Review submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            commentTextArea.setText("");
            ratingComboBox.setSelectedIndex(0);
            production.updateRatingSorting();
            refreshDetailsPanel(production);
            toggleReviewPanelVisibility();

        } else {
            JOptionPane.showMessageDialog(frame, "Failed to add review, please complete everything.", "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshDetailsPanel(Production production) {
        switchToDetailsPanel(production instanceof Movie ? MOVIES : SERIES, production);
    }

    private void updateFavouriteButtonIcon() {
        User user = IMDB.getCurrentUser();
        if (viewingActors) {
            Actor currentActor = actors.get(currentActorIndex);
            if (user.searchFavouriteActor(currentActor.getName()) == null) {
                changeButtonIcon(addToFavoritesButton, "src/main/resources/images/not-added-to-favourites.png", 30, 30);
            } else
                changeButtonIcon(addToFavoritesButton, "src/main/resources/images/added-to-favourites.png", 30, 30);
        } else {
            Production currentProduction = productions.get(currentProductionIndex);
            if (user.searchFavouriteMovie(currentProduction.getTitle()) == null && user.searchFavouriteSeries(currentProduction.getTitle()) == null) {
                changeButtonIcon(addToFavoritesButton, "src/main/resources/images/not-added-to-favourites.png", 30, 30);
            } else {
                changeButtonIcon(addToFavoritesButton, "src/main/resources/images/added-to-favourites.png", 30, 30);
            }
        }
    }

    private JButton createAddToFavoritesButton() {
        addToFavoritesButton = createIconButton("src/main/resources/images/added-to-favourites.png", 30, 30);
        addToFavoritesButton.addActionListener(e -> {
            User user = IMDB.getInstance().getCurrentUser();
            if (viewingActors) {
                Actor currentActor = actors.get(currentActorIndex);
                if (user.searchFavouriteActor(currentActor.getName()) == null) {
                    changeButtonIcon(addToFavoritesButton, "src/main/resources/images/added-to-favourites.png", 30, 30);
                    user.addToFavorites(currentActor);
                    JOptionPane.showMessageDialog(frame, currentActor.getName() + " added to favourites!");
                } else {
                    changeButtonIcon(addToFavoritesButton, "src/main/resources/images/not-added-to-favourites.png", 30, 30);
                    user.removeActorFromFavourites(currentActor);
                    JOptionPane.showMessageDialog(frame, currentActor.getName() + " removed from favourites!");
                }
            } else {
                Production currentProduction = productions.get(currentProductionIndex);
                if (user.searchFavouriteMovie(currentProduction.getTitle()) == null && user.searchFavouriteSeries(currentProduction.getTitle()) == null) {
                    changeButtonIcon(addToFavoritesButton, "src/main/resources/images/added-to-favourites.png", 30, 30);
                    user.addToFavorites(currentProduction);
                    JOptionPane.showMessageDialog(frame, currentProduction.getTitle() + " added to favourites!");
                } else {
                    changeButtonIcon(addToFavoritesButton, "src/main/resources/images/not-added-to-favourites.png", 30, 30);
                    user.removeProductionFromFavourites(currentProduction);
                    JOptionPane.showMessageDialog(frame, currentProduction.getTitle() + " removed from favourites!");
                }
            }
        });
        return addToFavoritesButton;
    }

    private void updateTitleDisplayForActors() {
        actors = IMDB.getInstance().sortActorsByName();
        if (actors != null && !actors.isEmpty()) {
            titleLabel.setText(actors.get(currentActorIndex % actors.size()).getName());
        }
    }

    private void navigateActors(int direction) {
        currentActorIndex = (currentActorIndex + direction + actors.size()) % actors.size();
        updateTitleDisplayForActors();
        updateFavouriteButtonIcon();
    }

    private void updateTitleDisplayForProductions() {
        productions = IMDB.getInstance().sortProductionsByNumberOfRatings();
        if (productions != null && !productions.isEmpty()) {
            titleLabel.setText(productions.get(currentProductionIndex % productions.size()).getTitle());
        }
    }

    private void navigateProductions(int direction) {
        currentProductionIndex = (currentProductionIndex + direction + productions.size()) % productions.size();
        updateTitleDisplayForProductions();
        updateFavouriteButtonIcon();
    }

    private void makeTitleLabelClickable() {
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && !e.isConsumed()) {
                    e.consume();
                    if (viewingActors) {
                        Actor selectedActor = actors.get(currentActorIndex);
                        switchToDetailsPanel(ACTORS, selectedActor);
                    } else {
                        Production selectedProduction = productions.get(currentProductionIndex);
                        String category = selectedProduction instanceof Movie ? MOVIES : SERIES;
                        switchToDetailsPanel(category, selectedProduction);
                    }
                }
            }
        });
    }

    private JButton createFavoritesButton() {
        ImageIcon favoriteIcon = new ImageIcon("src/main/resources/images/favourites-icon.jpeg");
        Image favoriteImage = favoriteIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        JButton favoriteButton = new JButton(new ImageIcon(favoriteImage));
        favoriteButton.setBorder(BorderFactory.createEmptyBorder());
        favoriteButton.setContentAreaFilled(false);
        favoriteButton.setFocusPainted(false);
        favoriteButton.addActionListener(e -> switchToFavoritesPanel());

        return favoriteButton;
    }

    public void switchToFavoritesPanel() {
        JPanel favoritesPanel = createFavoritesPanel();
        cardPanel.add(favoritesPanel, "FavoritesPanel");
        cardLayout.show(cardPanel, "FavoritesPanel");
    }

    private JPanel createFavoritesPanel() {
        JPanel favoritesPanel = new JPanel(new BorderLayout());
        favoritesPanel.setBackground(Color.DARK_GRAY);

        SortedSet<Object> favoritesObjects = (SortedSet<Object>) IMDB.getCurrentUser().getFavourites();
        List<String> favoritesNames = favoritesObjects.stream()
                .map(object -> {
                    if (object instanceof Movie) {
                        return ((Movie) object).getTitle();
                    } else if (object instanceof Series) {
                        return ((Series) object).getTitle();
                    } else if (object instanceof Actor) {
                        return ((Actor) object).getName();
                    } else {
                        return "Unknown";
                    }
                })
                .toList();

        JList<String> favoritesList = new JList<>(new Vector<>(favoritesNames));
        favoritesList.setBackground(Color.DARK_GRAY);
        favoritesList.setForeground(Color.WHITE);
        favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        favoritesList.setFixedCellHeight(100);
        favoritesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 2),
                            BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));
                }
                return c;
            }
        });
        favoritesList.revalidate();
        favoritesList.repaint();

        favoritesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList<?> list = (JList<?>) evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    Object selectedObject = list.getModel().getElementAt(index);
                    Movie movie = IMDB.getInstance().searchMovie((String) selectedObject);
                    Series series = IMDB.getInstance().searchSeries((String) selectedObject);
                    Actor actor = IMDB.getInstance().searchActor((String) selectedObject);

                    if (movie != null) {
                        switchToDetailsPanel(MOVIES, movie);
                    } else if (series != null) {
                        switchToDetailsPanel(SERIES, series);
                    } else if (actor != null) {
                        switchToDetailsPanel(ACTORS, actor);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(favoritesList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        favoritesPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e ->
                cardLayout.show(cardPanel, MAIN_PANEL));
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.BLACK);
        favoritesPanel.add(backButton, BorderLayout.SOUTH);

        return favoritesPanel;
    }


    private JButton createMenuButton() {
        ImageIcon menuIcon = new ImageIcon("src/main/resources/images/menu-icon.png");
        Image menuImage = menuIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton menuButton = new JButton(new ImageIcon(menuImage));
        menuButton.setBorder(BorderFactory.createEmptyBorder());
        menuButton.setContentAreaFilled(false);
        menuButton.setFocusPainted(false);
        menuButton.addActionListener(e -> toggleMenuVisibility());
        return menuButton;
    }

    private JLabel createLogoLabel() {
        ImageIcon logoIcon = new ImageIcon("src/main/resources/images/logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(65, 25, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(logoImage));
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBackground(Color.DARK_GRAY);

        String[] categories = {"Movies", "Series", "Actors"};
        JComboBox<String> categoryDropdown = new JComboBox<>(categories);
        styleComboBox(categoryDropdown);
        categoryDropdown.setMaximumSize(new Dimension(80, 30));
        categoryDropdown.setFocusable(false);


        categoryDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(Color.ORANGE);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(Color.BLACK);
                    setForeground(Color.WHITE);
                }
                setBorder(null);
                return this;
            }
        });

        categoryDropdown.validate();
        categoryDropdown.repaint();


        searchPanel.add(categoryDropdown);

        searchPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        JTextField searchField = new JTextField("Search IMDb...");
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setMaximumSize(new Dimension(500, 30));
        searchField.setBackground(Color.GRAY);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createEmptyBorder());

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search IMDb...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search IMDb...");
                }
            }
        });

        searchPanel.add(searchField);

        searchPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton searchButton = createSearchButton();
        searchButton.addActionListener(e -> performSearch(searchField.getText(), (String) categoryDropdown.getSelectedItem()));
        searchField.addActionListener(e -> performSearch(searchField.getText(), (String) categoryDropdown.getSelectedItem()));

        searchPanel.add(searchButton);

        return searchPanel;
    }

    private JButton createSearchButton() {
        ImageIcon searchIcon = new ImageIcon("src/main/resources/images/search-icon.png");
        Image searchImage = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton searchButton = new JButton(new ImageIcon(searchImage));
        searchButton.setBorder(BorderFactory.createEmptyBorder());
        searchButton.setContentAreaFilled(false);
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(30, 30));
        return searchButton;
    }

    private void performSearch(String query, String category) {
        if (query.equals("Search IMDb...") || query.trim().isEmpty()) {
            return;
        }
        boolean found = false;

        IMDB imdb = IMDB.getInstance();
        Production productionFound = null;
        Actor actorFound = null;
        switch (category) {
            case MOVIES:
                productionFound = imdb.searchMovie(query);
                if (productionFound != null) {
                    found = true;
                }
                break;
            case SERIES:
                productionFound = imdb.searchSeries(query);
                if (productionFound != null) {
                    found = true;
                }
                break;
            case ACTORS:
                actorFound = imdb.searchActor(query);
                if (actorFound != null) {
                    found = true;
                }
                break;
        }

        if (found) {
            if (actorFound != null)
                switchToDetailsPanel(category, actorFound);
            else
                switchToDetailsPanel(category, productionFound);
        } else {
            JOptionPane.showMessageDialog(frame, "No results found for: " + query, "Not Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void switchToDetailsPanel(String category, Object item) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(Color.DARK_GRAY);

        switch (category) {
            case MOVIES -> {
                Movie movie = (Movie) item;
                detailsPanel.add(createMoviePanel(movie), BorderLayout.CENTER);
            }
            case SERIES -> {
                Series series = (Series) item;
                detailsPanel.add(createSeriesPanel(series), BorderLayout.CENTER);
            }
            case ACTORS -> {
                Actor actor = (Actor) item;
                detailsPanel.add(createActorPanel(actor), BorderLayout.CENTER);
            }
        }

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 1));
        JButton reviewButton = null;
        if (category.equals(MOVIES) || category.equals(SERIES)) {
            reviewButton = new JButton("Review this production");
            styleButton(reviewButton);
            reviewButton.addActionListener(e -> toggleReviewPanelVisibility());
            buttonsPanel.add(reviewButton);
        }

        if (IMDB.getCurrentUser() instanceof Admin || IMDB.getCurrentUser() instanceof Contributor) {
            buttonsPanel.setVisible(false);
        }

        if ((category.equals(MOVIES) || category.equals(SERIES)) && ((Production) item).findRatingByUsername(IMDB.getCurrentUser().getUsername()) != null) {
            JButton removeReview = new JButton("Remove current review");
            styleButton(removeReview);
            removeReview.addActionListener(e -> {
                ((Production) item).removeRating(IMDB.getCurrentUser().getUsername());
                refreshDetailsPanel(((Production) item));
            });
            buttonsPanel = new JPanel(new GridLayout(0, 2));
            buttonsPanel.add(reviewButton);
            buttonsPanel.add(removeReview);
        }
        buttonsPanel.setMaximumSize(buttonsPanel.getPreferredSize());
        detailsPanel.add(buttonsPanel, BorderLayout.NORTH);

        cardPanel.add(detailsPanel, DETAILS_PANEL);

        JButton backButton = new JButton("Back to main page");
        styleButton(backButton);
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, MAIN_PANEL);
            if (isReviewPanelVisible) toggleReviewPanelVisibility();
        });
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.BLACK);
        detailsPanel.add(backButton, BorderLayout.SOUTH);

        cardPanel.add(detailsPanel, DETAILS_PANEL);

        cardLayout.show(cardPanel, DETAILS_PANEL);
    }

    private JPanel createMoviePanel(Movie movie) {
        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.PAGE_AXIS));
        moviePanel.setBackground(Color.DARK_GRAY);
        moviePanel.setForeground(Color.WHITE);

        moviePanel.add(createLabel("Title: " + movie.getTitle()));
        moviePanel.add(createLabel("Director: " + String.join(", ", movie.getDirectors())));
        moviePanel.add(createLabel("Stars: " + String.join(", ", movie.getCast())));
        moviePanel.add(createLabel("Genres: " + movie.getGenresString()));
        moviePanel.add(createLabel("Description: " + movie.getDescription()));
        moviePanel.add(createLabel("Ratings: "));
        String[] ratingLines = movie.getRatingAsString().split("\n");
        for (String ratingLine : ratingLines) {
            moviePanel.add(createLabel(ratingLine));
        }
        moviePanel.add(createLabel("Release Year: " + movie.getReleaseYear()));
        moviePanel.add(createLabel("Duration: " + movie.getDuration() + " min"));

        return moviePanel;
    }

    private JPanel createSeriesPanel(Series series) {
        JPanel seriesPanel = new JPanel();
        seriesPanel.setLayout(new BoxLayout(seriesPanel, BoxLayout.PAGE_AXIS));
        seriesPanel.setBackground(Color.DARK_GRAY);
        seriesPanel.setForeground(Color.WHITE);

        seriesPanel.add(createLabel("Title: " + series.getTitle()));
        seriesPanel.add(createLabel("Director: " + String.join(", ", series.getDirectors())));
        seriesPanel.add(createLabel("Stars: " + String.join(", ", series.getCast())));
        seriesPanel.add(createLabel("Genres: " + series.getGenresString()));
        seriesPanel.add(createLabel("Description: " + series.getDescription()));
        seriesPanel.add(createLabel("Ratings: "));

        String[] ratingLines = series.getRatingAsString().split("\n");
        for (String ratingLine : ratingLines) {
            seriesPanel.add(createLabel(ratingLine));
        }
        seriesPanel.add(createLabel("Release Year: " + series.getReleaseYear()));

        if (series.getSeasons() != null && !series.getSeasons().isEmpty()) {
            seriesPanel.add(createLabel("Seasons:"));
            series.getSeasons().forEach((key, value) -> {
                seriesPanel.add(createLabel(key + ":"));
                for (Episode episode : value) {
                    seriesPanel.add(createLabel("Episode: " + episode.getName() + ", Duration: " + episode.getDuration() + " min"));
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(seriesPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.DARK_GRAY);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createActorPanel(Actor actor) {
        JPanel actorPanel = new JPanel();
        actorPanel.setLayout(new BoxLayout(actorPanel, BoxLayout.Y_AXIS));
        actorPanel.setBackground(Color.DARK_GRAY);
        actorPanel.add(createLabel("Name: " + nonNull(actor.getName())));
        actorPanel.add(createLabel("Biography: " + nonNull(actor.getBiography())));

        if (actor.getPerformances() != null) {
            for (ActorPerformance performance : actor.getPerformances()) {
                actorPanel.add(createLabel("Performance: " + nonNull(performance.getTitle()) + ", " + performance.getType()));
            }
        }

        JScrollPane scrollPane = new JScrollPane(actorPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.DARK_GRAY);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private String nonNull(String str) {
        return str != null ? str : "N/A";
    }

    private JLabel createLabel(String text) {
        int labelWidth = 400;

        String htmlText = "<html><body style='width: " + labelWidth + "px'>" + text + "</body></html>";
        JLabel label = new JLabel(htmlText);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        return label;
    }

    private void createViewMyRequestsWindow() {
        List<Request> requests;
        if (IMDB.getCurrentUser().getAccountType().equals(AccountType.REGULAR))
            requests = ((Regular) IMDB.getCurrentUser()).getPersonalRequests();
        else
            requests = ((Contributor) IMDB.getCurrentUser()).getPersonalRequests();

        if (requests.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "You don't have any requests.", "No Requests", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        JFrame requestsFrame = new JFrame("View My Requests");
        requestsFrame.setLayout(new BorderLayout());
        requestsFrame.setSize(500, 500);
        requestsFrame.setLocationRelativeTo(frame);
        requestsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        requestsFrame.addWindowFocusListener(new WindowAdapter() {
            public void windowLostFocus(WindowEvent e) {
                requestsFrame.dispose();
            }
        });

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.BLACK);
        requestsFrame.setContentPane(contentPanel);


        DefaultListModel<String> listModel = new DefaultListModel<>();
        int i = 1;
        for (Request request : requests) {
            String requestText = "<html>No: " + i + "<br/>" +
                    "<strong>Type:</strong> " + request.getType() + "<br/>" +
                    "<strong>Description:</strong> " + request.getDescription() + "<br/>" +
                    "<strong>Status:</strong> " + request.getRequestStatusAsString(request) + "</html>";
            listModel.addElement(requestText);
            i++;
        }

        JList<String> requestsList = new JList<>(listModel);
        requestsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsList.setVisibleRowCount(-1);

        requestsList.setBackground(Color.BLACK);
        requestsList.setOpaque(true);

        requestsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                label.setBackground(Color.BLACK);
                label.setForeground(Color.ORANGE);
                if (isSelected) {
                    label.setBackground(Color.ORANGE.darker());
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        });

        JScrollPane listScroller = new JScrollPane(requestsList);

        JScrollBar verticalScrollBar = listScroller.getVerticalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.ORANGE;
                this.trackColor = Color.BLACK;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        verticalScrollBar = listScroller.getHorizontalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.ORANGE;
                this.trackColor = Color.BLACK;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        requestsFrame.add(listScroller, BorderLayout.CENTER);

        JButton removeButton = new JButton("Remove");
        styleButton(removeButton);
        removeButton.setEnabled(false);

        removeButton.addActionListener(e -> {
            int selectedIndex = requestsList.getSelectedIndex();
            if (selectedIndex != -1) {
                ((Regular) IMDB.getCurrentUser()).removeRequest(requests.get(selectedIndex));
                listModel.remove(selectedIndex);
                removeButton.setEnabled(false);
            }
        });

        requestsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removeButton.setEnabled(requestsList.getSelectedIndex() != -1);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(removeButton);


        contentPanel.add(buttonPanel, BorderLayout.PAGE_START);

        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.addActionListener(e -> requestsFrame.dispose());
        contentPanel.add(closeButton, BorderLayout.PAGE_END);

        requestsFrame.setVisible(true);
    }

    private void createRequestPanel() {
        JDialog requestDialog = new JDialog(frame, "Create Request", true);
        requestDialog.setLayout(new BorderLayout());
        requestDialog.setSize(500, 500);
        requestDialog.setLocationRelativeTo(frame);

        String[] requestTypes = {"DELETE_ACCOUNT", "MOVIE_ISSUE", "ACTOR_ISSUE", "OTHERS"};
        JComboBox<String> requestTypeComboBox = new JComboBox<>(requestTypes);
        styleComboBox(requestTypeComboBox);

        JComboBox<String> detailsComboBox = new JComboBox<>();
        detailsComboBox.setVisible(false);
        styleComboBox(detailsComboBox);

        JTextArea messageTextArea = new JTextArea(5, 20);
        styleTextArea(messageTextArea);

        JButton submitButton = new JButton("Submit");
        styleButton(submitButton);
        submitButton.addActionListener(e -> {
            submitRequest((String) requestTypeComboBox.getSelectedItem(), messageTextArea.getText(), (String) detailsComboBox.getSelectedItem());
            requestDialog.dispose();
        });

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(e -> requestDialog.dispose());

        requestTypeComboBox.addActionListener(e -> {
            String selectedType = (String) requestTypeComboBox.getSelectedItem();
            if ("MOVIE_ISSUE".equals(selectedType)) {
                detailsComboBox.setModel(new DefaultComboBoxModel<>(getProductionTitles()));
                detailsComboBox.setVisible(true);
            } else if ("ACTOR_ISSUE".equals(selectedType)) {
                detailsComboBox.setModel(new DefaultComboBoxModel<>(getActorNames()));
                detailsComboBox.setVisible(true);
            } else {
                detailsComboBox.setVisible(false);
            }
        });

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.BLACK);
        formPanel.add(requestTypeComboBox);
        formPanel.add(detailsComboBox);
        formPanel.add(new JScrollPane(messageTextArea));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);

        requestDialog.add(formPanel, BorderLayout.CENTER);
        requestDialog.add(buttonPanel, BorderLayout.SOUTH);

        requestDialog.pack();
        requestDialog.setMinimumSize(new Dimension(500, 500));
        requestDialog.setVisible(true);
    }

    private String[] getProductionTitles() {
        String[] list = new String[productions.size()];
        int index = 0;
        for (Production production : productions) {
            list[index] = production.getTitle();
            index++;
        }
        return list;
    }

    private String[] getActorNames() {
        String[] list = new String[actors.size()];
        int index = 0;
        for (Actor actor : actors) {
            list[index] = actor.getName();
            index++;
        }
        return list;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboBox.getMinimumSize().height));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setBackground(Color.BLACK);
        comboBox.setForeground(Color.ORANGE);
        comboBox.setFocusable(false);

        comboBox.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Object popup = comboBox.getUI().getAccessibleChild(comboBox, 0);
                    if (popup instanceof BasicComboPopup) {
                        JScrollPane scrollPane = findScrollPane((BasicComboPopup) popup);
                        if (scrollPane != null) {
                            styleScrollPane(scrollPane);
                        }
                    }
                });
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
    }

    private void styleComboBoxInteger(JComboBox<Integer> comboBox) {
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboBox.getMinimumSize().height));
        comboBox.setBackground(Color.BLACK);
        comboBox.setForeground(Color.ORANGE);
        comboBox.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Object popup = comboBox.getUI().getAccessibleChild(comboBox, 0);
                    if (popup instanceof BasicComboPopup) {
                        JScrollPane scrollPane = findScrollPane((BasicComboPopup) popup);
                        if (scrollPane != null) {
                            styleScrollPane(scrollPane);
                        }
                    }
                });
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
    }

    private JScrollPane findScrollPane(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JScrollPane) {
                return (JScrollPane) c;
            } else if (c instanceof Container) {
                JScrollPane sp = findScrollPane((Container) c);
                if (sp != null) {
                    return sp;
                }
            }
        }
        return null;
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.ORANGE;
                this.trackColor = Color.BLACK;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroSizeButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroSizeButton();
            }

            private JButton createZeroSizeButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
    }

    private void styleTextArea(JTextArea textArea) {
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.ORANGE);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        String placeholderText = "Type message here...";
        textArea.setText(placeholderText);
        textArea.setForeground(Color.ORANGE.darker());

        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholderText)) {
                    textArea.setText("");
                    textArea.setForeground(Color.ORANGE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setForeground(Color.GRAY);
                    textArea.setText(placeholderText);
                }
            }
        });
    }

    private void submitRequest(String type, String message, String nameOrTitle) {
        if (type != null && message != null && !message.isEmpty() && !message.equals("Type message here...")) {
            Request request = Request.buildRequest(RequestsTypes.valueOf(type), nameOrTitle, message, IMDB.getCurrentUser().getUsername());
            if (IMDB.getCurrentUser() instanceof Regular) {
                ((Regular) IMDB.getCurrentUser()).createRequest(request);
            } else {
                Contributor contributor = (Contributor) IMDB.getCurrentUser();
                if (contributor.searchInAddedItems(nameOrTitle)) {
                    JOptionPane.showMessageDialog(cardPanel, "Failed to create the request. You can not create a request about your own contributions!" +
                            "!", "Failure.", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                contributor.createRequest(request);
            }
            JOptionPane.showMessageDialog(cardPanel, "Request submitted successfully!", "Request Submitted", JOptionPane.INFORMATION_MESSAGE);
        } else
            JOptionPane.showMessageDialog(cardPanel, "Failed to create the request. Please fill everything!", "Failure.", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel createNewMoviePanel() {
        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new GridBagLayout());
        moviePanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        JTextField titleField = new JTextField(30);
        styleTextField(titleField);

        DefaultListModel<String> directorsListModel = new DefaultListModel<>();
        JList<String> directorsList = new JList<>(directorsListModel);
        styleList(directorsList);

        DefaultListModel<String> castListModel = new DefaultListModel<>();
        JList<String> castList = new JList<>(castListModel);
        styleList(castList);

        DefaultComboBoxModel<CheckComboItem> genreModel = new DefaultComboBoxModel<>();
        for (Genre genre : Genre.values()) {
            genreModel.addElement(new CheckComboItem(genre.name(), false));
        }
        JComboBox<CheckComboItem> genreComboBox = new JComboBox<>(genreModel);
        genreComboBox.setRenderer(new CheckComboRenderer());
        genreComboBox.addActionListener(e -> {
            JComboBox<CheckComboItem> combo = (JComboBox<CheckComboItem>) e.getSource();
            CheckComboItem item = (CheckComboItem) combo.getSelectedItem();
            assert item != null;
            item.setSelected(!item.isSelected());
            combo.repaint();
        });

        JTextArea descriptionArea = new JTextArea(5, 30);
        styleTextArea(descriptionArea);

        JTextField durationField = new JTextField(10);
        styleTextField(durationField);

        JTextField releaseYearField = new JTextField(10);
        styleTextField(releaseYearField);

        addLabel(moviePanel, "Title:", gbc);
        moviePanel.add(titleField, gbc);

        addLabel(moviePanel, "Directors:", gbc);
        moviePanel.add(new JScrollPane(directorsList), gbc);
        addButtons(moviePanel, directorsList, directorsListModel, "Director", gbc);

        addLabel(moviePanel, "Cast:", gbc);
        moviePanel.add(new JScrollPane(castList), gbc);
        addButtons(moviePanel, castList, castListModel, "Cast", gbc);

        addLabel(moviePanel, "Genre:", gbc);
        moviePanel.add(genreComboBox, gbc);

        addLabel(moviePanel, "Description:", gbc);
        moviePanel.add(new JScrollPane(descriptionArea), gbc);

        addLabel(moviePanel, "Duration (minutes):", gbc);
        moviePanel.add(durationField, gbc);

        addLabel(moviePanel, "Release Year:", gbc);
        moviePanel.add(releaseYearField, gbc);

        JButton submitMovieButton = new JButton("Submit Movie");
        styleButton(submitMovieButton);

        submitMovieButton.addActionListener(e -> submitMovie(
                titleField, directorsListModel, castListModel, genreComboBox, descriptionArea, durationField, releaseYearField));

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        moviePanel.add(submitMovieButton, gbc);

        return moviePanel;
    }

    private void submitMovie(JTextField titleField,
                             DefaultListModel<String> directorsListModel,
                             DefaultListModel<String> castListModel,
                             JComboBox<CheckComboItem> genreComboBox,
                             JTextArea descriptionArea,
                             JTextField durationField,
                             JTextField releaseYearField) {
        String title = titleField.getText().trim();
        List<String> directors = Collections.list(directorsListModel.elements());
        List<String> cast = Collections.list(castListModel.elements());

        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < genreComboBox.getItemCount(); i++) {
            CheckComboItem item = genreComboBox.getItemAt(i);
            if (item.isSelected()) {
                genres.add(Genre.valueOf(item.toString()));
            }
        }

        String description = descriptionArea.getText().trim();
        String duration = durationField.getText().trim();
        String releaseYear = releaseYearField.getText().trim();

        if (title.isEmpty() || directors.isEmpty() || cast.isEmpty() || genres.isEmpty()
                || description.isEmpty() || duration.isEmpty() || releaseYear.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please complete all fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int durationInt;
        int releaseYearInt;
        try {
            durationInt = Integer.parseInt(duration);
            releaseYearInt = Integer.parseInt(releaseYear);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for duration and release year.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Movie newMovie = new Movie(title, directors, cast, genres, description, durationInt, releaseYearInt);

        ((Staff) IMDB.getCurrentUser()).addProductionSystem(newMovie);

        titleField.setText("");
        directorsListModel.clear();
        castListModel.clear();
        descriptionArea.setText("");
        durationField.setText("");
        releaseYearField.setText("");

        JOptionPane.showMessageDialog(frame, "Movie added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.ORANGE);
        panel.add(label, gbc);
    }

    private void addButtons(JPanel panel, JList<String> list, DefaultListModel<String> listModel, String itemType, GridBagConstraints gbc) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add " + itemType);
        JButton removeButton = new JButton("Remove " + itemType);
        addButton.addActionListener(e -> {
            String item = JOptionPane.showInputDialog(panel, "Enter " + itemType + ":");
            if (item != null && !item.trim().isEmpty()) {
                listModel.addElement(item);
            }
        });
        removeButton.addActionListener(e -> {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, gbc);
    }

    private JPanel createNewSeriesPanel() {
        JPanel seriesPanel = new JPanel();
        seriesPanel.setLayout(new GridBagLayout());
        seriesPanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        JTextField titleField = new JTextField(30);
        styleTextField(titleField);

        DefaultListModel<String> directorsListModel = new DefaultListModel<>();
        JList<String> directorsList = new JList<>(directorsListModel);
        styleList(directorsList);

        DefaultListModel<String> castListModel = new DefaultListModel<>();
        JList<String> castList = new JList<>(castListModel);
        styleList(castList);

        DefaultComboBoxModel<CheckComboItem> genreModel = new DefaultComboBoxModel<>();
        for (Genre genre : Genre.values()) {
            genreModel.addElement(new CheckComboItem(genre.name(), false));
        }
        JComboBox<CheckComboItem> genreComboBox = new JComboBox<>(genreModel);
        genreComboBox.setRenderer(new CheckComboRenderer());
        genreComboBox.addActionListener(e -> {
            JComboBox<CheckComboItem> combo = (JComboBox<CheckComboItem>) e.getSource();
            CheckComboItem item = (CheckComboItem) combo.getSelectedItem();
            assert item != null;
            item.setSelected(!item.isSelected());
            combo.repaint();
        });

        JTextArea descriptionArea = new JTextArea(5, 30);
        styleTextArea(descriptionArea);

        JTextField releaseYearField = new JTextField(10);
        styleTextField(releaseYearField);

        addLabel(seriesPanel, "Title:", gbc);
        seriesPanel.add(titleField, gbc);

        addLabel(seriesPanel, "Directors:", gbc);
        seriesPanel.add(new JScrollPane(directorsList), gbc);
        addButtons(seriesPanel, directorsList, directorsListModel, "Director", gbc);

        addLabel(seriesPanel, "Cast:", gbc);
        seriesPanel.add(new JScrollPane(castList), gbc);
        addButtons(seriesPanel, castList, castListModel, "Cast", gbc);

        addLabel(seriesPanel, "Genre:", gbc);
        seriesPanel.add(genreComboBox, gbc);

        addLabel(seriesPanel, "Description:", gbc);
        seriesPanel.add(new JScrollPane(descriptionArea), gbc);

        addLabel(seriesPanel, "Release Year:", gbc);
        seriesPanel.add(releaseYearField, gbc);

        JButton addSeasonButton = new JButton("Add Season");
        styleButton(addSeasonButton);
        Series series = new Series("Nothing");
        addSeasonButton.addActionListener(e -> {
            series.setNumberOfSeasons(addSeasonsToSeries(series));
        });
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        seriesPanel.add(addSeasonButton, gbc);

        JButton submitSeriesButton = new JButton("Submit Series");
        styleButton(submitSeriesButton);
        submitSeriesButton.addActionListener(e -> submitSeries(
                titleField, directorsListModel, castListModel, genreComboBox, descriptionArea, releaseYearField, series
        ));
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        seriesPanel.add(submitSeriesButton, gbc);

        return seriesPanel;
    }

    private int addSeasonsToSeries(Series series) {
        int seasonCount = 0;
        while (true) {
            int option = JOptionPane.showConfirmDialog(frame, "Would you like to add a season?", "Add Season", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String seasonName = JOptionPane.showInputDialog(frame, "Enter the season name:");
                if (seasonName != null && !seasonName.trim().isEmpty()) {
                    List<Episode> episodes = addEpisodes();
                    series.getSeasons().put(seasonName, episodes);
                    seasonCount++;
                }
            } else {
                break;
            }
        }
        return seasonCount;
    }

    private List<Episode> addEpisodes() {
        List<Episode> episodes = new ArrayList<>();
        String input = JOptionPane.showInputDialog(frame, "How many episodes?");
        if (input != null) {
            try {
                int episodeCount = Integer.parseInt(input.trim());
                for (int i = 0; i < episodeCount; i++) {
                    String episodeName = JOptionPane.showInputDialog(frame, "Enter the title for episode " + (i + 1) + ":");
                    String episodeDurationStr = JOptionPane.showInputDialog(frame, "Enter the duration (in minutes) for episode " + (i + 1) + ":");
                    if (episodeName != null && episodeDurationStr != null && !episodeName.trim().isEmpty() && !episodeDurationStr.trim().isEmpty()) {
                        int episodeDuration = Integer.parseInt(episodeDurationStr.trim());
                        episodes.add(new Episode(episodeName, episodeDuration));
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for episodes.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
        return episodes;
    }

    private void submitSeries(JTextField titleField,
                              DefaultListModel<String> directorsListModel,
                              DefaultListModel<String> castListModel,
                              JComboBox<CheckComboItem> genreComboBox,
                              JTextArea descriptionArea,
                              JTextField releaseYearField, Series newSeries) {
        String title = titleField.getText().trim();
        List<String> directors = Collections.list(directorsListModel.elements());
        List<String> cast = Collections.list(castListModel.elements());

        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < genreComboBox.getItemCount(); i++) {
            CheckComboItem item = genreComboBox.getItemAt(i);
            if (item.isSelected()) {
                genres.add(Genre.valueOf(item.toString()));
            }
        }

        String description = descriptionArea.getText().trim();
        String releaseYear = releaseYearField.getText().trim();

        if (title.isEmpty() || directors.isEmpty() || cast.isEmpty() || genres.isEmpty()
                || description.isEmpty() || releaseYear.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please complete all fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int releaseYearInt;
        try {
            releaseYearInt = Integer.parseInt(releaseYear);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for duration and release year.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        newSeries.setDescription(description);
        newSeries.setTitle(title);
        newSeries.setReleaseYear(releaseYearInt);
        newSeries.setCast(cast);
        newSeries.setDirectors(directors);
        newSeries.setGenres(genres);
        ((Staff) IMDB.getCurrentUser()).addProductionSystem(newSeries);

        titleField.setText("");
        directorsListModel.clear();
        castListModel.clear();
        descriptionArea.setText("");
        releaseYearField.setText("");

        JOptionPane.showMessageDialog(frame, "Seriesed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createAddWindow() {
        addWindowFrame = new JFrame("Add New Entry");
        addWindowFrame.setSize(500, 650);
        addWindowFrame.setLayout(new BorderLayout());
        addWindowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowFrame.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        String[] types = {"Actor", "Movie", "Series"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);
        typeComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleComboBox(typeComboBox);

        JPanel actorPanel = createActorPanel();
        JPanel moviePanel = createNewMoviePanel();
        JPanel seriesPanel = createNewSeriesPanel();

        CardLayout cardLayout = new CardLayout();
        JPanel cardsPanel = new JPanel(cardLayout);
        cardsPanel.add(actorPanel, "Actor");
        cardsPanel.add(moviePanel, "Movie");
        cardsPanel.add(seriesPanel, "Series");

        typeComboBox.addActionListener(e -> {
            String selectedType = (String) typeComboBox.getSelectedItem();
            cardLayout.show(cardsPanel, selectedType);
        });

        contentPanel.add(typeComboBox);
        contentPanel.add(cardsPanel);

        addWindowFrame.add(contentPanel, BorderLayout.CENTER);

        addWindowFrame.setVisible(true);
    }

    private JPanel createActorPanel() {
        JPanel actorPanel = new JPanel();
        actorPanel.setLayout(new GridBagLayout());
        actorPanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 25));
        styleTextField(nameField);

        biographyArea = new JTextArea();
        biographyArea.setRows(3);
        JScrollPane bioScrollPane = new JScrollPane(biographyArea);
        styleTextArea(biographyArea);

        JList<ActorPerformance> performanceList = new JList<>(performanceListModel);
        performanceList.setVisibleRowCount(4);
        JScrollPane performanceScrollPane = new JScrollPane(performanceList);
        styleList(performanceList);

        JButton addPerformanceButton = new JButton("Add Performance");
        styleButton(addPerformanceButton);
        addPerformanceButton.addActionListener(e -> addPerformance());

        removePerformanceButton = new JButton("Remove Performance");
        removePerformanceButton.addActionListener(e -> removePerformance(performanceList));
        styleButton(removePerformanceButton);
        removePerformanceButton.setVisible(false);

        actorPanel.add(new JLabel("Name:"), gbc);
        actorPanel.add(nameField, gbc);
        actorPanel.add(new JLabel("Biography:"), gbc);
        actorPanel.add(bioScrollPane, gbc);
        actorPanel.add(new JLabel("Performances:"), gbc);
        actorPanel.add(performanceScrollPane, gbc);
        actorPanel.add(addPerformanceButton, gbc);
        actorPanel.add(removePerformanceButton, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> submitActor());
        styleButton(submitButton);

        buttonPanel.add(submitButton);

        actorPanel.add(buttonPanel, gbc);

        return actorPanel;
    }

    private void addPerformance() {
        String title = JOptionPane.showInputDialog(addWindowFrame, "Enter the performance title:");
        if (title != null && !title.isEmpty()) {
            String[] performanceTypes = {"MOVIE", "SERIES"};
            String type = (String) JOptionPane.showInputDialog(addWindowFrame, "Enter the performance type:",
                    "Performance Type", JOptionPane.QUESTION_MESSAGE, null, performanceTypes, performanceTypes[0]);

            if (type != null && (type.equals("MOVIE") || type.equals("SERIES"))) {
                performanceListModel.addElement(new ActorPerformance(title, type));
                removePerformanceButton.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(addWindowFrame, "Invalid type. Please select MOVIE or SERIES.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removePerformance(JList<ActorPerformance> performanceList) {
        int selectedIndex = performanceList.getSelectedIndex();
        if (selectedIndex != -1) {
            performanceListModel.remove(selectedIndex);
            if (performanceListModel.isEmpty()) {
                removePerformanceButton.setVisible(false);
            }
        }
    }

    private void submitActor() {
        if (!nameField.getText().trim().isEmpty() &&
                !biographyArea.getText().trim().isEmpty() &&
                !biographyArea.getText().equals("Type message here...") &&
                performanceListModel.getSize() > 0) {

            JOptionPane.showMessageDialog(addWindowFrame, "Submit successful!");

            List<ActorPerformance> performances = new ArrayList<>();
            for (int i = 0; i < performanceListModel.getSize(); i++) {
                performances.add(performanceListModel.get(i));
            }

            ((Staff) IMDB.getCurrentUser()).addActorSystem(new Actor(nameField.getText(), performances, biographyArea.getText()));

            nameField.setText("");
            biographyArea.setText("");
            performanceListModel.clear();

            addWindowFrame.dispose();

        } else {
            JOptionPane.showMessageDialog(addWindowFrame, "Please complete all fields and add at least one performance.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void styleTextField(JTextField textField) {
        textField.setBackground(Color.DARK_GRAY);
        textField.setForeground(Color.ORANGE);
        textField.setCaretColor(Color.ORANGE);
        textField.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
    }

    private void styleList(JList<?> list) {
        list.setBackground(Color.BLACK);
        list.setForeground(Color.ORANGE);
        list.setFixedCellHeight(20);
        list.setSelectionBackground(new Color(128, 128, 0));
        list.setSelectionForeground(Color.WHITE);
        list.setBorder(BorderFactory.createLineBorder(Color.ORANGE));

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                label.setBackground(Color.BLACK);
                label.setForeground(isSelected ? Color.WHITE : Color.ORANGE);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.ORANGE));
                return label;
            }
        });
    }

    private void modifyDatabaseWindow() {
        typeComboBox = new JComboBox<>(new String[]{"REGULAR", "CONTRIBUTOR", "ADMIN"});
        nameField = new JTextField(20);
        countryField = new JTextField(20);
        emailField = new JTextField(20);
        ageSpinner = new JSpinner(new SpinnerNumberModel(18, 13, 120, 1));
        genderComboBox = new JComboBox<>(new String[]{"F", "M", "N"});
        try {
            birthdayField = new JFormattedTextField(new MaskFormatter("####-##-##"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JDialog contributionsDialog = new JDialog(frame, "My Contributions", true);
        contributionsDialog.setSize(500, 500);
        contributionsDialog.setLayout(new BorderLayout());
        contributionsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        contributionsDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.BLACK);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        ((Staff<?>) IMDB.getCurrentUser()).getPersonalContributions().forEach(contribution -> {
            if (contribution instanceof Actor) {
                listModel.addElement(((Actor) contribution).getName());
            } else if (contribution instanceof Production) {
                listModel.addElement(((Production) contribution).getTitle());
            }
        });

        JList<String> contributionsList = new JList<>(listModel);
        contributionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contributionsList.setBackground(Color.BLACK);
        contributionsList.setForeground(Color.ORANGE);

        JScrollPane listScroller = new JScrollPane(contributionsList);
        listScroller.setBackground(Color.BLACK);
        listScroller.setForeground(Color.ORANGE);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(Color.BLACK);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            contributionsDialog.dispose();
            createAddWindow();
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            int selectedIndex = contributionsList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedContribution = contributionsList.getSelectedValue();

                if (IMDB.getInstance().searchActor(selectedContribution) != null) {
                    ((Staff) IMDB.getCurrentUser()).removeActorSystem(selectedContribution);
                } else {
                    ((Staff) IMDB.getCurrentUser()).removeProductionSystem(selectedContribution);
                }

                listModel.remove(selectedIndex);
            }
        });

        removeButton.setEnabled(false);

        contributionsList.addListSelectionListener(e -> {
            boolean selectionExists = !contributionsList.isSelectionEmpty();
            removeButton.setEnabled(selectionExists);
        });

        styleButton(addButton);
        styleButton(removeButton);

        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);

        contentPanel.add(buttonsPanel, BorderLayout.NORTH);
        contentPanel.add(listScroller, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.addActionListener(e -> contributionsDialog.dispose());
        contentPanel.add(closeButton, BorderLayout.SOUTH);

        contributionsDialog.add(contentPanel);

        contributionsDialog.setLocationRelativeTo(null);

        contributionsDialog.setVisible(true);
    }

    private JDialog dialog;
    private JList<String> userList;
    private DefaultListModel listModel;

    private void createModifyUsersWindow() {
        JFrame modifyUsersFrame = new JFrame("Manage Users");
        dialog = new JDialog(modifyUsersFrame, "Manage Users", true);
        dialog.setSize(500, 500);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setBackground(Color.BLACK);
        userList.setForeground(Color.ORANGE);
        userList.setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane listScrollPane = new JScrollPane(userList);
        dialog.add(listScrollPane, BorderLayout.CENTER);

        removeUserButton = new JButton("Remove");
        removeUserButton.setEnabled(false);
        removeUserButton.setBackground(Color.ORANGE);
        removeUserButton.setForeground(Color.BLACK);
        removeUserButton.addActionListener(e -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex != -1) {
                ((Admin<?>) IMDB.getCurrentUser()).deleteUser(IMDB.getInstance().getUsers().get(selectedIndex));
                listModel.remove(selectedIndex);
            }
        });

        userList.addListSelectionListener(e -> removeUserButton.setEnabled(!userList.isSelectionEmpty()));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(removeUserButton);

        JButton addUserButton = new JButton("Add New User");
        addUserButton.setBackground(Color.ORANGE);
        addUserButton.setForeground(Color.BLACK);
        addUserButton.addActionListener(e -> {
            createAddUserWindow();
            refreshUserList();
        });
        buttonPanel.add(addUserButton);

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(Color.ORANGE);
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        populateUserList();

        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        displayUserInfo(IMDB.getInstance().getUsers().get(index));
                    }
                }
            }
        });

        dialog.setVisible(true);
    }

    private void displayUserInfo(User user) {
        JDialog infoDialog = new JDialog(dialog, "User Information", true);
        infoDialog.setLayout(new GridLayout(0, 1));
        infoDialog.setSize(500, 500);
        infoDialog.setLocationRelativeTo(dialog);
        infoDialog.getContentPane().setBackground(Color.BLACK);

        infoDialog.add(createStyledLabel("Name: " + user.getInfo().getName()));
        infoDialog.add(createStyledLabel("Country: " + user.getInfo().getCountry()));
        infoDialog.add(createStyledLabel("Birthdate: " + user.getInfo().getBirthDate().toString()));
        infoDialog.add(createStyledLabel("Email: " + user.getInfo().getCredentials().getEmail()));
        infoDialog.add(createStyledLabel("Password: " + user.getInfo().getCredentials().getPassword()));
        infoDialog.add(createStyledLabel("Username: " + user.getUsername()));
        infoDialog.add(createStyledLabel("Age: " + user.getInfo().getAge()));
        infoDialog.add(createStyledLabel("Account type: " + user.getAccountType().toString()));
        if (user.getExperience() != null)
            infoDialog.add(createStyledLabel("Experience: " + user.getExperience()));

        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.addActionListener(e -> infoDialog.dispose());
        infoDialog.add(closeButton);

        infoDialog.setVisible(true);
    }


    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.ORANGE);
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private void refreshUserList() {
        listModel.clear();
        for (User<?> user : IMDB.getInstance().getUsers()) {
            listModel.addElement(user.getUsername());
        }
    }

    private void populateUserList() {
        for (User<?> user : IMDB.getInstance().getUsers()) {
            listModel.addElement(user.getUsername());
        }
    }

    private void createAddUserWindow() {
        JDialog detailDialog = new JDialog(dialog, "Create a new user", true);
        detailDialog.getContentPane().setBackground(Color.BLACK);
        detailDialog.setLayout(new GridBagLayout());
        detailDialog.setSize(500, 500);
        detailDialog.setLocationRelativeTo(null);

        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridwidth = 1;
        gbcLabel.gridheight = 1;
        gbcLabel.gridx = 0;
        gbcLabel.gridy = GridBagConstraints.RELATIVE;
        gbcLabel.anchor = GridBagConstraints.EAST;
        gbcLabel.insets = new Insets(2, 2, 2, 2);

        GridBagConstraints gbcComponent = new GridBagConstraints();
        gbcComponent.gridwidth = 1;
        gbcComponent.gridheight = 1;
        gbcComponent.gridx = 1;
        gbcComponent.gridy = GridBagConstraints.RELATIVE;
        gbcComponent.anchor = GridBagConstraints.WEST;
        gbcComponent.insets = new Insets(2, 2, 2, 2);

        JLabel labelType = new JLabel("Type:");
        labelType.setForeground(Color.ORANGE);
        detailDialog.add(labelType, gbcLabel);
        typeComboBox.setBackground(Color.BLACK);
        typeComboBox.setForeground(Color.ORANGE);
        detailDialog.add(typeComboBox, gbcComponent);

        JLabel labelName = new JLabel("Name:");
        labelName.setForeground(Color.ORANGE);
        detailDialog.add(labelName, gbcLabel);
        nameField.setBackground(Color.BLACK);
        nameField.setForeground(Color.ORANGE);
        detailDialog.add(nameField, gbcComponent);

        JLabel labelCountry = new JLabel("Country:");
        labelCountry.setForeground(Color.ORANGE);
        detailDialog.add(labelCountry, gbcLabel);
        countryField.setBackground(Color.BLACK);
        countryField.setForeground(Color.ORANGE);
        detailDialog.add(countryField, gbcComponent);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setForeground(Color.ORANGE);
        detailDialog.add(labelEmail, gbcLabel);
        emailField.setBackground(Color.BLACK);
        emailField.setForeground(Color.ORANGE);
        detailDialog.add(emailField, gbcComponent);

        JLabel labelAge = new JLabel("Age:");
        labelAge.setForeground(Color.ORANGE);
        detailDialog.add(labelAge, gbcLabel);
        JComponent editor = ageSpinner.getEditor();
        editor.setBackground(Color.BLACK);
        editor.setForeground(Color.ORANGE);
        detailDialog.add(ageSpinner, gbcComponent);

        JLabel labelGender = new JLabel("Gender:");
        labelGender.setForeground(Color.ORANGE);
        detailDialog.add(labelGender, gbcLabel);
        genderComboBox.setBackground(Color.BLACK);
        genderComboBox.setForeground(Color.ORANGE);
        detailDialog.add(genderComboBox, gbcComponent);

        JLabel labelBirthday = new JLabel("Birthday (YYYY-MM-DD):");
        labelBirthday.setForeground(Color.ORANGE);
        detailDialog.add(labelBirthday, gbcLabel);
        birthdayField.setBackground(Color.BLACK);
        birthdayField.setForeground(Color.ORANGE);
        detailDialog.add(birthdayField, gbcComponent);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.BLACK);

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.ORANGE);
        submitButton.setForeground(Color.BLACK);
        buttonPanel.add(submitButton);

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(Color.ORANGE);
        closeButton.setForeground(Color.BLACK);
        buttonPanel.add(closeButton);

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridwidth = 2;
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 8;
        gbcButtons.anchor = GridBagConstraints.CENTER;

        detailDialog.add(buttonPanel, gbcButtons);

        submitButton.addActionListener(e -> {
            String type = (String) typeComboBox.getSelectedItem();
            String name = nameField.getText().trim();
            String country = countryField.getText().trim();
            String email = emailField.getText().trim();
            Integer age = (Integer) ageSpinner.getValue();
            String gender = (String) genderComboBox.getSelectedItem();
            String birthday = birthdayField.getText().trim();

            if (name.isEmpty() || country.isEmpty() || email.isEmpty() || Objects.requireNonNull(gender).isEmpty() || birthday.isEmpty()) {
                JOptionPane.showMessageDialog(detailDialog, "All fields must be completed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(detailDialog, "Invalid date format. Please enter the date in the format YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Credentials.isValidEmail(email)) {
                JOptionPane.showMessageDialog(detailDialog, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String username = User.generateUsername(name);
            AccountType accountType;
            try {
                accountType = AccountType.valueOf(type);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(detailDialog, "Account type does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User.Information info = new User.Information.Builder()
                    .name(name)
                    .credentials(new Credentials(email))
                    .country(country)
                    .age(age)
                    .gender(gender.charAt(0))
                    .birthDate(birthDate)
                    .build();

            User<?> newUser = UserFactory.createUser(accountType, username, info, 0);
            ((Admin<?>) IMDB.getCurrentUser()).addUser(newUser);
            JOptionPane.showMessageDialog(detailDialog, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            detailDialog.dispose();
        });

        closeButton.addActionListener(e -> detailDialog.dispose());

        detailDialog.setVisible(true);
    }

    private void createViewTODORequestsPanel() {
        JDialog todoRequestsDialog = new JDialog(frame, "TODO Requests", true);
        todoRequestsDialog.setLayout(new BorderLayout());
        todoRequestsDialog.setSize(500, 500);
        todoRequestsDialog.setLocationRelativeTo(frame);
        todoRequestsDialog.getContentPane().setBackground(Color.BLACK);

        DefaultListModel<Request> requestsListModel = new DefaultListModel<>();
        JList<Request> requestsList = new JList<>(requestsListModel);
        requestsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsList.setBackground(Color.BLACK);
        requestsList.setForeground(Color.ORANGE);
        requestsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(((Request) value).getDescription());
                if (isSelected) {
                    setBackground(Color.ORANGE.darker());
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.BLACK);
                    setForeground(Color.ORANGE);
                }
                return this;
            }
        });

        for (Request request : ((Staff<?>) IMDB.getCurrentUser()).getReceivedRequests()) {
            if (request.getStatus().equals(RequestStatus.ONGOING))
                requestsListModel.addElement(request);
        }

        if (IMDB.getCurrentUser().getAccountType().equals(AccountType.ADMIN)) {
            for (Request request : RequestsHolder.getRequests()) {
                if (request.getStatus().equals(RequestStatus.ONGOING))
                    requestsListModel.addElement(request);
            }
        }

        JScrollPane listScroller = new JScrollPane(requestsList);
        listScroller.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        todoRequestsDialog.add(listScroller, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.addActionListener(e -> todoRequestsDialog.dispose());

        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closeButtonPanel.setBackground(Color.BLACK);
        closeButtonPanel.add(closeButton);
        todoRequestsDialog.add(closeButtonPanel, BorderLayout.SOUTH);

        requestsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Request selectedRequest = requestsList.getSelectedValue();
                if (selectedRequest != null) {
                    showRequestDetails(selectedRequest, requestsListModel);
                }
            }
        });

        todoRequestsDialog.setVisible(true);
    }


    private void showRequestDetails(Request request, DefaultListModel<Request> requestsListModel) {
        JDialog detailsDialog = new JDialog(frame, "Request Details", true);
        detailsDialog.setLayout(new BorderLayout());
        detailsDialog.setSize(500, 500);
        detailsDialog.setLocationRelativeTo(frame);
        detailsDialog.getContentPane().setBackground(Color.BLACK);

        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBackground(Color.BLACK);
        addRequestInfoToPanel(infoPanel, "Type:", request.getType().toString());
        addRequestInfoToPanel(infoPanel, "Created Date:", request.getCreationDate().toString());
        addRequestInfoToPanel(infoPanel, "Username:", request.getCreatorUsername());
        addRequestInfoToPanel(infoPanel, "To:", request.getResolverUsername());
        addRequestInfoToPanel(infoPanel, "Name/Title:", request.getNameOrTitle());
        addRequestInfoToPanel(infoPanel, "Description:", request.getDescription());
        addRequestInfoToPanel(infoPanel, "Status:", request.getRequestStatusAsString(request));

        detailsDialog.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.BLACK);

        JButton solvedButton = new JButton("Mark as Solved");
        styleButton(solvedButton);
        solvedButton.addActionListener(e -> {
            String resolutionDetails = JOptionPane.showInputDialog(detailsDialog, "Enter resolution details:", "Resolve Request", JOptionPane.PLAIN_MESSAGE);
            if (resolutionDetails != null && !resolutionDetails.trim().isEmpty()) {
                ((Staff) IMDB.getCurrentUser()).resolveRequest(request, resolutionDetails);
                refreshRequestsList(requestsListModel);
                detailsDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(detailsDialog, "You must enter resolution details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton deniedButton = new JButton("Mark as Denied");
        styleButton(deniedButton);

        deniedButton.addActionListener(e -> {
            String resolutionDetails = JOptionPane.showInputDialog(detailsDialog, "Enter resolution details:", "Deny Request", JOptionPane.PLAIN_MESSAGE);
            if (resolutionDetails != null && !resolutionDetails.trim().isEmpty()) {
                ((Staff) IMDB.getCurrentUser()).denyRequest(request, resolutionDetails);
                refreshRequestsList(requestsListModel);
                detailsDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(detailsDialog, "You must enter resolution details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.addActionListener(e -> detailsDialog.dispose());

        buttonPanel.add(solvedButton);
        buttonPanel.add(deniedButton);
        buttonPanel.add(closeButton);
        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        detailsDialog.setVisible(true);
    }

    private void refreshRequestsList(DefaultListModel<Request> model) {
        model.clear();
        for (Request req : ((Staff<?>) IMDB.getCurrentUser()).getReceivedRequests()) {
            if (req.getStatus().equals(RequestStatus.ONGOING)) {
                model.addElement(req);
            }
        }

        if (IMDB.getCurrentUser().getAccountType().equals(AccountType.ADMIN)) {
            for (Request req : RequestsHolder.getRequests()) {
                if (req.getStatus().equals(RequestStatus.ONGOING)) {
                    model.addElement(req);
                }
            }
        }
    }

    private void addRequestInfoToPanel(JPanel panel, String labelText, String value) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.ORANGE);
        panel.add(label);

        JTextField infoField = new JTextField(value);
        infoField.setBackground(Color.BLACK);
        infoField.setForeground(Color.ORANGE);
        infoField.setEditable(false);
        panel.add(infoField);
    }


    private void createSlidingMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.BLACK);
        menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension buttonSize = new Dimension(250, 50);

        menuPanel.add(Box.createVerticalGlue());

        viewToDoRequests = new JButton("View TODO requests");
        styleButton(viewToDoRequests);
        viewToDoRequests.setMaximumSize(buttonSize);
        viewToDoRequests.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewToDoRequests.addActionListener(e -> createViewTODORequestsPanel());
        menuPanel.add(viewToDoRequests);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        modifyDatabaseButton = new JButton("Modify Database");
        styleButton(modifyDatabaseButton);
        modifyDatabaseButton.setMaximumSize(buttonSize);
        modifyDatabaseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        modifyDatabaseButton.addActionListener(e -> modifyDatabaseWindow());
        menuPanel.add(modifyDatabaseButton);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        modifyUserButton = new JButton("Modify Users");
        styleButton(modifyUserButton);
        modifyUserButton.setMaximumSize(buttonSize);
        modifyUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        modifyUserButton.addActionListener(e -> createModifyUsersWindow());
        menuPanel.add(modifyUserButton);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        viewMyRequestsButton = new JButton("View My Requests");
        styleButton(viewMyRequestsButton);
        viewMyRequestsButton.setMaximumSize(buttonSize);
        viewMyRequestsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewMyRequestsButton.addActionListener(e -> createViewMyRequestsWindow());
        menuPanel.add(viewMyRequestsButton);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        createRequestButton = new JButton("Create a Request");
        styleButton(createRequestButton);
        createRequestButton.setMaximumSize(buttonSize);
        createRequestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createRequestButton.addActionListener(e -> createRequestPanel());
        menuPanel.add(createRequestButton);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton logoutButton = createLogoutButton();
        styleButton(logoutButton);
        logoutButton.setMaximumSize(buttonSize);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(logoutButton);

        menuPanel.add(Box.createVerticalGlue());

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.setMaximumSize(buttonSize);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            toggleMenuVisibility();
            cardLayout.show(cardPanel, MAIN_PANEL);
        });
        menuPanel.add(backButton);

        menuPanel.add(Box.createVerticalGlue());

        menuPanel.setBounds(0, -frame.getHeight(), frame.getWidth(), frame.getHeight());
        menuPanel.setVisible(false);

        frame.getLayeredPane().add(menuPanel, JLayeredPane.POPUP_LAYER);
    }

    private void toggleMenuVisibility() {
        if (!isAnimating) {

            User<?> currentUser = IMDB.getCurrentUser();
            if (currentUser instanceof Regular) {
                viewMyRequestsButton.setVisible(true);
                createRequestButton.setVisible(true);
                modifyDatabaseButton.setVisible(false);
                modifyUserButton.setVisible(false);
                viewToDoRequests.setVisible(false);
            } else if (currentUser instanceof Contributor) {
                viewMyRequestsButton.setVisible(true);
                createRequestButton.setVisible(true);
                modifyDatabaseButton.setVisible(true);
                modifyUserButton.setVisible(false);
                viewToDoRequests.setVisible(true);
            } else {
                viewMyRequestsButton.setVisible(false);
                createRequestButton.setVisible(false);
                modifyDatabaseButton.setVisible(true);
                modifyUserButton.setVisible(true);
                viewToDoRequests.setVisible(true);
            }

            updateTitleDisplayForActors();
            updateTitleDisplayForProductions();

            if (isMenuVisible) {
                slideMenuPanel(0, -menuPanel.getHeight());
            } else {
                slideMenuPanel(-menuPanel.getHeight(), 0);
                menuPanel.setVisible(true);
            }
            isMenuVisible = !isMenuVisible;
        }
    }

    private void slideMenuPanel(int start, int end) {
        isAnimating = true;
        menuPanel.setSize(frame.getWidth(), frame.getHeight());
        menuPanel.setLocation(0, start);

        Timer timer = new Timer(1, new ActionListener() {
            int yPosition = start;

            public void actionPerformed(ActionEvent e) {
                if ((end > start && yPosition < end) || (end < start && yPosition > end)) {
                    yPosition += (end > start) ? 20 : -20;
                    menuPanel.setLocation(0, yPosition);
                } else {
                    ((Timer) e.getSource()).stop();
                    isAnimating = false;
                    menuPanel.setVisible(end == 0);
                }
            }
        });
        timer.start();
    }


    private JButton createLogoutButton() {
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);

        logoutButton.addActionListener(e -> {
            IMDB.setLoggedIn(false);
            IMDB.setCurrentUser(null);

            cardLayout.show(cardPanel, "StartBeforeLogin");
            toggleMenuVisibility();
        });

        return logoutButton;
    }


    public void switchToAfterLoginStartPanel() {
        JPanel startPanelAfterLogin = createStartPanelAfterLogin();
        cardPanel.add(startPanelAfterLogin, "MainPanel");
        cardLayout.show(cardPanel, MAIN_PANEL);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Email:");
        usernameLabel.setForeground(Color.WHITE);
        JTextField emailField = new JTextField(15);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setMargin(new Insets(2, 5, 2, 2));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setMargin(new Insets(2, 5, 2, 2));

        JButton loginButton = new JButton("Login");
        styleButton(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            IMDB imdb = IMDB.getInstance();
            User<?> user = imdb.getUserWithEmail(email);

            if (user != null && user.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                IMDB.setLoggedIn(true);
                IMDB.setCurrentUser(user);
                emailField.setText("");
                passwordField.setText("");
                switchToAfterLoginStartPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Login failed! Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                emailField.setText("");
                passwordField.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        return panel;
    }
}

class CheckComboItem {
    private String label;
    private boolean isSelected;

    public CheckComboItem(String label, boolean isSelected) {
        this.label = label;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String toString() {
        return label;
    }
}

class CheckComboRenderer implements ListCellRenderer<CheckComboItem> {
    private JCheckBox checkBox;

    public CheckComboRenderer() {
        checkBox = new JCheckBox();
    }

    public Component getListCellRendererComponent(JList<? extends CheckComboItem> list, CheckComboItem value, int index, boolean isSelected, boolean cellHasFocus) {
        checkBox.setText(value.toString());
        checkBox.setSelected(value.isSelected());
        return checkBox;
    }
}