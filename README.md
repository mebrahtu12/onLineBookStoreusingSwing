# Online Book Store

A Java Swing bookstore app demonstrating design patterns with a modern, organized codebase.

## Project structure

```
src/bookstore/
  Main.java                 # Entry point
  model/                    # Book, Category (Composite), BookComponent
  patterns/
    mediator/                 # BookstoreMediator
    cart/                     # ShoppingCart
    memento/                  # CartMemento
    inventory/                # Inventory
    chain/                    # Payment chain of responsibility
    state/                    # Order lifecycle states
    proxy/                    # User login proxy
    discount/                 # Promo discounts
  service/                  # Catalog seed data, order history
  ui/                       # Themed GUI
data/
  orders.txt                # Saved order history
build/                      # Compiled classes (generated)
```

## Run

**Windows**

```bat
compile.bat
run.bat
```

**Manual**

```bat
javac -d build -encoding UTF-8 src/bookstore/**/*.java
java -cp build bookstore.Main
```

## Features

- **Register** then **Log In** (accounts saved in `data/users.txt`)
- Browse catalog with genre-colored book covers (Composite pattern)
- Search books by title or genre
- Shopping cart with add/remove
- Save & restore cart (Memento)
- Checkout requires login (Proxy)
- Payment chain: Wallet → Credit Card → PayPal
- Order state progression: Pending → Confirmed → Shipped → Delivered
- Order history saved to `data/orders.txt`

## Design patterns

| Pattern | Usage |
|---------|--------|
| Mediator | Coordinates cart, inventory, payments |
| Composite | Categories contain books and subcategories |
| Prototype | `Book.clone()` for cart copies |
| Memento | Save/restore cart snapshots |
| State | Order status transitions |
| Chain of Responsibility | Payment handlers |
| Proxy | Gated checkout behind login |
| Strategy | Fixed and percentage discounts |
