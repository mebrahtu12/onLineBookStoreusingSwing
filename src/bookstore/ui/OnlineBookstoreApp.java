package bookstore.ui;

import bookstore.model.Book;
import bookstore.model.Category;
import bookstore.patterns.cart.ShoppingCart;
import bookstore.patterns.chain.CreditCardPayment;
import bookstore.patterns.chain.PayPalPayment;
import bookstore.patterns.chain.PaymentSystem;
import bookstore.patterns.chain.WalletPayment;
import bookstore.patterns.inventory.Inventory;
import bookstore.patterns.mediator.BookstoreMediator;
import bookstore.patterns.memento.CartMemento;
import bookstore.patterns.proxy.UserProxy;
import bookstore.patterns.state.Order;
import bookstore.service.CatalogData;
import bookstore.service.OrderHistoryService;
import bookstore.service.UserRegistry;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class OnlineBookstoreApp extends JFrame {
    private BookstoreMediator mediator;
    private Inventory inventory;
    private ShoppingCart cart;
    private PaymentSystem paymentSystem;
    private UserProxy user;
    private UserRegistry userRegistry;
    private CartMemento savedCart;
    private OrderHistoryService orderHistory;

    private DefaultListModel<Category> genreModel;
    private DefaultListModel<Book> bookModel;
    private DefaultListModel<Book> cartModel;
    private JList<Category> genreList;
    private JList<Book> bookList;
    private JList<Book> cartList;
    private JLabel booksHeaderLabel;
    private JLabel cartSubtotalLabel;
    private JLabel cartTotalLabel;
    private JLabel userStatusLabel;
    private JTextField searchField;
    private Category selectedGenre;
    private StyledButton btnLogout;

    public OnlineBookstoreApp() {
        super("Online Book Store");
        Theme.applyGlobal();
        setupBackend();
        setupGUI();
        refreshInventory();
        refreshCart();
        showWelcomeMessage();
    }

    private void setupBackend() {
        mediator = new BookstoreMediator();

        var wallet = new WalletPayment(50.0);
        var card = new CreditCardPayment(200.0);
        var paypal = new PayPalPayment(500.0);
        wallet.setNext(card);
        card.setNext(paypal);

        inventory = new Inventory(mediator);
        cart = new ShoppingCart(mediator);
        paymentSystem = new PaymentSystem(mediator, wallet);
        mediator.setColleagues(cart, inventory, paymentSystem);

        CatalogData.loadSampleCatalog(inventory);
        user = new UserProxy("Guest", false);

        Path dataDir = Path.of("data");
        try {
            java.nio.file.Files.createDirectories(dataDir);
        } catch (Exception ignored) {
        }
        userRegistry = new UserRegistry(dataDir.resolve("users.txt"));
        orderHistory = new OrderHistoryService(dataDir.resolve("orders.txt"));
    }

    private void setupGUI() {
        setSize(1050, 760);
        setMinimumSize(new Dimension(900, 640));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout(0, 0));

        add(Theme.createHeader("Online Book Store",
                "Register an account, then log in to browse and checkout"),
                BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 0));
        center.setBackground(Theme.BACKGROUND);
        center.setBorder(Theme.padded(12));

        genreModel = new DefaultListModel<>();
        genreList = new JList<>(genreModel);
        genreList.setCellRenderer(new GenreListCellRenderer());
        genreList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        genreList.setFixedCellHeight(BookListCellRenderer.ROW_HEIGHT);
        genreList.setBorder(Theme.titled("Genres"));

        bookModel = new DefaultListModel<>();
        bookList = new JList<>(bookModel);
        bookList.setCellRenderer(new BookListCellRenderer());
        bookList.putClientProperty("cartView", false);
        bookList.setFixedCellHeight(BookListCellRenderer.ROW_HEIGHT);

        booksHeaderLabel = new JLabel("Select a genre to view books");
        booksHeaderLabel.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD));
        booksHeaderLabel.setForeground(Theme.PRIMARY_DARK);
        booksHeaderLabel.setBorder(BorderFactory.createEmptyBorder(4, 2, 6, 2));

        searchField = new JTextField();
        searchField.setFont(Theme.BODY_FONT);
        searchField.setForeground(Theme.TEXT);
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                Theme.padded(6)));
        searchField.setToolTipText("Search by title or genre");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterInventory();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterInventory();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterInventory();
            }
        });

        JPanel booksPanel = new JPanel(new BorderLayout(0, 4));
        booksPanel.setBackground(Theme.PANEL);
        booksPanel.add(booksHeaderLabel, BorderLayout.NORTH);
        JScrollPane bookScroll = new JScrollPane(bookList);
        bookScroll.setBorder(Theme.titled("Books"));
        booksPanel.add(bookScroll, BorderLayout.CENTER);

        JSplitPane catalogSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(genreList), booksPanel);
        catalogSplit.setDividerLocation(200);
        catalogSplit.setResizeWeight(0.32);
        catalogSplit.setBorder(null);

        JPanel inventoryPanel = new JPanel(new BorderLayout(0, 8));
        inventoryPanel.setBackground(Theme.PANEL);
        inventoryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                Theme.padded(10)));
        inventoryPanel.add(searchField, BorderLayout.NORTH);
        inventoryPanel.add(catalogSplit, BorderLayout.CENTER);

        genreList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onGenreSelected();
            }
        });

        cartModel = new DefaultListModel<>();
        cartList = new JList<>(cartModel);
        cartList.setCellRenderer(new BookListCellRenderer());
        cartList.putClientProperty("cartView", true);
        cartList.setBorder(Theme.titled("Your Cart"));
        cartList.setFixedCellHeight(BookListCellRenderer.ROW_HEIGHT);

        cartSubtotalLabel = new JLabel("Subtotal: $0.00");
        cartTotalLabel = new JLabel("Total: $0.00");
        cartSubtotalLabel.setFont(Theme.BODY_FONT);
        cartSubtotalLabel.setForeground(Theme.TEXT_MUTED);
        cartSubtotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        cartSubtotalLabel.setBorder(BorderFactory.createEmptyBorder(4, 5, 0, 5));

        cartTotalLabel.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        cartTotalLabel.setForeground(Theme.PRIMARY);
        cartTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        cartTotalLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 8, 5));

        JPanel totalsPanel = new JPanel(new GridLayout(2, 1));
        totalsPanel.setOpaque(false);
        totalsPanel.add(cartSubtotalLabel);
        totalsPanel.add(cartTotalLabel);

        JPanel cartPanel = new JPanel(new BorderLayout(0, 8));
        cartPanel.setBackground(Theme.PANEL);
        cartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                Theme.padded(10)));
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        cartPanel.add(totalsPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inventoryPanel, cartPanel);
        splitPane.setDividerLocation(540);
        splitPane.setResizeWeight(0.55);
        splitPane.setBorder(null);
        splitPane.setBackground(Theme.BACKGROUND);
        center.add(splitPane, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setBackground(Theme.BACKGROUND);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        userStatusLabel = new JLabel("Not logged in — register first, then log in");
        userStatusLabel.setFont(Theme.SUBTITLE_FONT);
        userStatusLabel.setForeground(Theme.TEXT_MUTED);
        userStatusLabel.setBorder(Theme.padded(4));
        bottom.add(userStatusLabel, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.setBackground(Theme.BACKGROUND);

        StyledButton btnAdd = Theme.primaryButton("Add to Cart");
        StyledButton btnRemove = Theme.secondaryButton("Remove from Cart");
        StyledButton btnSave = Theme.secondaryButton("Save Cart");
        StyledButton btnRestore = Theme.secondaryButton("Restore Cart");
        StyledButton btnRegister = Theme.secondaryButton("Register");
        StyledButton btnLogin = Theme.primaryButton("Log In");
        btnLogout = Theme.secondaryButton("Log Out");
        StyledButton btnCheckout = Theme.primaryButton("Checkout");
        StyledButton btnHistory = Theme.secondaryButton("Order History");

        buttons.add(btnAdd);
        buttons.add(btnRemove);
        buttons.add(btnSave);
        buttons.add(btnRestore);
        buttons.add(btnRegister);
        buttons.add(btnLogin);
        buttons.add(btnCheckout);
        buttons.add(btnHistory);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        logoutPanel.setBackground(Theme.BACKGROUND);
        logoutPanel.add(btnLogout);

        JPanel buttonRow = new JPanel(new BorderLayout());
        buttonRow.setBackground(Theme.BACKGROUND);
        buttonRow.add(buttons, BorderLayout.CENTER);
        buttonRow.add(logoutPanel, BorderLayout.EAST);

        bottom.add(buttonRow, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addSelectedToCart());
        btnRemove.addActionListener(e -> removeSelectedFromCart());
        btnSave.addActionListener(e -> {
            savedCart = cart.save();
            showInfo("Cart saved. You can restore it later.");
        });
        btnRestore.addActionListener(e -> restoreSavedCart());
        btnRegister.addActionListener(e -> promptRegister());
        btnLogin.addActionListener(e -> promptLogin());
        btnLogout.addActionListener(e -> {
            System.exit(0);
        });
        btnCheckout.addActionListener(e -> checkout());
        btnHistory.addActionListener(e -> showOrderHistory());

        updateUserStatus();
    }

    private void showWelcomeMessage() {
        WelcomeDialog.show(this, () -> {
        }, this::promptLogin);
    }

    private void promptRegister() {
        AuthDialog.showRegister(this, userRegistry);
    }

    private void promptLogin() {
        String username = AuthDialog.showLogin(this, userRegistry);
        if (username != null) {
            user = new UserProxy(username, false);
            user.login();
            updateUserStatus();
            showInfo("Welcome back, " + username + "!");
        }
    }

    private void addSelectedToCart() {
        Book selected = bookList.getSelectedValue();
        if (selected == null) {
            showWarning("Select a book from the list below the genre.");
            return;
        }
        if (!selected.isInStock()) {
            showWarning("Sorry, \"" + selected.getTitle() + "\" is out of stock.");
            return;
        }
        selected.decrementStock();
        cart.addBook(selected);
        refreshCart();
        refreshBookList();
        showInfo(selected.getTitle() + " added to your cart. ("
                + selected.getStock() + " left in store)");
    }

    private void removeSelectedFromCart() {
        int index = cartList.getSelectedIndex();
        if (index < 0) {
            showWarning("Select a book in your cart to remove.");
            return;
        }
        Book removed = cartModel.get(index);
        cart.removeAt(index);
        Book inventoryBook = inventory.findBook(removed.getTitle(), removed.getCategoryName());
        if (inventoryBook != null) {
            inventoryBook.incrementStock();
        }
        refreshCart();
        refreshBookList();
        showInfo(removed.getTitle() + " removed from cart and returned to stock.");
    }

    private void restoreSavedCart() {
        if (savedCart == null) {
            showWarning("No saved cart found.");
            return;
        }
        returnCartItemsToStock();
        cart.clearCart();

        int restored = 0;
        int skipped = 0;
        for (Book saved : savedCart.getSavedBooks()) {
            Book inv = inventory.findBook(saved.getTitle(), saved.getCategoryName());
            if (inv == null || !inv.isInStock()) {
                skipped++;
                continue;
            }
            inv.decrementStock();
            cart.addBook(inv);
            restored++;
        }

        refreshCart();
        refreshBookList();
        if (skipped > 0) {
            showWarning("Restored " + restored + " item(s). " + skipped
                    + " item(s) skipped (not enough stock).");
        } else {
            showInfo("Cart restored with " + restored + " item(s).");
        }
    }

    private void returnCartItemsToStock() {
        for (Book item : cart.getBooks()) {
            Book inv = inventory.findBook(item.getTitle(), item.getCategoryName());
            if (inv != null) {
                inv.incrementStock();
            }
        }
    }

    private void refreshBookList() {
        if (selectedGenre != null) {
            populateBooksForGenre(selectedGenre);
        }
        bookList.repaint();
    }

    private void checkout() {
        if (!user.isLoggedIn()) {
            showWarning("Please register and log in before checkout.");
            return;
        }
        if (cart.getBooks().isEmpty()) {
            showWarning("Your cart is empty.");
            return;
        }

        double total = cart.getTotalAmount();
        Order order = new Order(mediator);
        mediator.setCurrentOrder(order);

        if (!user.placeOrder(order)) {
            showWarning("Could not place order. Please log in again.");
            return;
        }

        if (!paymentSystem.processPayment(total)) {
            showWarning("Payment failed. Order total exceeds available payment limits.");
            return;
        }

        StringBuilder status = new StringBuilder("Order confirmed!\n\n");
        status.append("Customer: ").append(user.getName()).append("\n");
        status.append("Payment via: ").append(paymentSystem.getLastMethodUsed()).append("\n");
        status.append("Status: ").append(order.getStatusLine()).append("\n");
        order.nextState();
        status.append("→ ").append(order.getStatusLine()).append("\n");
        order.nextState();
        status.append("→ ").append(order.getStatusLine()).append("\n");

        try {
            orderHistory.appendOrder(cart.getBooks(), total, paymentSystem.getLastMethodUsed());
        } catch (Exception ex) {
            status.append("\nNote: order saved locally with a warning.\n");
        }

        status.append(String.format("\nTotal paid: $%.2f", total));
        showExitDialog(status, "Checkout Complete");

        cart.clearCart();
        savedCart = null;
        refreshCart();
        refreshBookList();
    }

    private void showOrderHistory() {
        JTextArea area = new JTextArea(orderHistory.readHistory());
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setBackground(Theme.PANEL);
        area.setForeground(Theme.TEXT);
        area.setBorder(Theme.padded(8));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(520, 360));
        showExitDialog(scroll, "Order History");
    }

    private void refreshInventory() {
        genreModel.clear();
        for (Category genre : inventory.getTopLevelGenres()) {
            genreModel.addElement(genre);
        }
        if (!genreModel.isEmpty()) {
            genreList.setSelectedIndex(0);
            onGenreSelected();
        } else {
            bookModel.clear();
            booksHeaderLabel.setText("Select a genre to view books");
        }
    }

    private void onGenreSelected() {
        selectedGenre = genreList.getSelectedValue();
        if (selectedGenre == null) {
            bookModel.clear();
            booksHeaderLabel.setText("Select a genre to view books");
            return;
        }
        booksHeaderLabel.setText("Books in: " + selectedGenre.getName());
        populateBooksForGenre(selectedGenre);
    }

    private void populateBooksForGenre(Category genre) {
        bookModel.clear();
        String query = searchField.getText().trim().toLowerCase();
        for (Book book : inventory.getBooksInCategory(genre)) {
            if (query.isEmpty()
                    || book.getTitle().toLowerCase().contains(query)
                    || book.getCategoryName().toLowerCase().contains(query)) {
                bookModel.addElement(book);
            }
        }
    }

    private void filterInventory() {
        if (selectedGenre != null) {
            populateBooksForGenre(selectedGenre);
        } else if (!searchField.getText().trim().isEmpty()) {
            bookModel.clear();
            String query = searchField.getText().trim().toLowerCase();
            for (Book book : inventory.getAllBooks()) {
                if (book.getTitle().toLowerCase().contains(query)
                        || book.getCategoryName().toLowerCase().contains(query)) {
                    bookModel.addElement(book);
                }
            }
            booksHeaderLabel.setText("Search results");
        }
    }

    private void refreshCart() {
        cartModel.clear();
        for (Book book : cart.getBooks()) {
            cartModel.addElement(book);
        }
        cartSubtotalLabel.setText(String.format("Subtotal: $%.2f", cart.getSubtotal()));
        cartTotalLabel.setText(String.format("Total: $%.2f", cart.getTotalAmount()));
    }

    private void updateUserStatus() {
        if (user.isLoggedIn()) {
            userStatusLabel.setText("Logged in as " + user.getName());
            userStatusLabel.setForeground(Theme.PRIMARY);
        } else {
            userStatusLabel.setText("Not logged in — register first, then log in");
            userStatusLabel.setForeground(Theme.TEXT_MUTED);
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Online Book Store", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showExitDialog(Object message, String title) {
        int result = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Online Book Store", JOptionPane.WARNING_MESSAGE);
    }
}
