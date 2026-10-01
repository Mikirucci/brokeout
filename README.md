# 🎮 Breakout Game in Java: Guida alla Logica e all'Architettura

La realizzazione di **Breakout** in Java si basa sull'Architettura Orientata agli Oggetti (**OOP**) e su tre pilastri fondamentali dei videogiochi 2D:

1. **Il Ciclo di Gioco (Game Loop):** Un timer a tempo costante (~60 FPS) che aggiorna le posizioni e ridisegna la grafica.
2. **L'Incapsulamento delle Entità:** Ogni elemento (Racchetta, Pallina, Mattoncino) è un oggetto indipendente con le proprie coordinate `(x, y)`, velocità e rendering.
3. **Il Rilevamento delle Collisioni (AABB):** Il calcolo delle intersezioni tra i rettangoli invisibili attorno ad ogni oggetto visibile.

---

## 📁 Struttura e Ruolo dei File

* **`Main.java`** $\rightarrow$ Configura e avvia la finestra principale (`JFrame`).
* **`Brick.java`** $\rightarrow$ Rappresenta il singolo mattoncino e gestisce il suo stato (integro o distrutto).
* **`Paddle.java`** $\rightarrow$ Gestisce il movimento orizzontale della racchetta e l'input da tastiera.
* **`Ball.java`** $\rightarrow$ Calcola la fisica del movimento orizzontale/verticale e i rimbalzi sulle pareti.
* **`GamePanel.java`** $\rightarrow$ Il "cervello" del gioco: esegue il Game Loop, calcola le collisioni tra gli oggetti e gestisce lo stato di gioco (Punteggio, Game Over, Vittoria).

---

## 🔍 Spiegazione Dettagliata per Singolo File

---

### 1. `Main.java` — La Finestra di Gioco

Ha il solo compito di inizializzare la finestra grafica di sistema usando **`JFrame`**.

```java
JFrame frame = new JFrame("Breakout Game OOP");
GamePanel panel = new GamePanel();

frame.add(panel);
frame.setSize(600, 400);
frame.setResizable(false); // Evita che l'utente alteri il sistema di coordinate
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Chiude il processo Java
frame.setLocationRelativeTo(null); // Centra la finestra sullo schermo
frame.setVisible(true);

```

* **Perché `setResizable(false)`?** Se la finestra potesse essere ridimensionata, le coordinate assolute in pixel dei mattoncini e della racchetta si sfaserebbero rispetto ai bordi.

---

### 2. `Brick.java` — Il Mattoncino e lo Stato "Distrutto"

Il mattoncino non ha movimento, ma mantiene uno stato fondamentale: il booleano **`destroyed`**.

```java
public void draw(Graphics g) {
    if (!destroyed) { // Disegna il mattoncino SOLO se è ancora integro
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK); // Bordo per separare i mattoncini adiacenti
        g.drawRect(x, y, width, height);
    }
}

```

* **Come funziona:** Quando la pallina impatta un mattoncino, il gioco imposta `destroyed = true`. Nel frame successivo, il metodo `draw()` salta quel mattoncino, facendolo sparire dallo schermo.

---

### 3. `Paddle.java` — Movimento e Input Tastiera

La racchetta si muove solo lungo l'asse orizzontale $X$. La sua velocità è definita da **`dx`** (delta X):

* `dx = -7`: la racchetta si sposta a sinistra di 7 pixel a frame.
* `dx = 7`: la racchetta si sposta a destra di 7 pixel a frame.
* `dx = 0`: la racchetta rimane ferma.

```java
public void update() {
    x += dx; // Aggiorna la posizione orizzontale

    // Controllo dei bordi dello schermo (Evita che la racchetta esca dalla finestra)
    if (x < 0) x = 0;
    if (x > 600 - width - 15) x = 600 - width - 15;
}

public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) dx = -7;
    if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) dx = 7;
}

public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || 
        key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
        dx = 0; // Quando il tasto viene rilasciato, si azzera la velocità
    }
}

```

---

### 4. `Ball.java` — Fisica e Vettori di Velocità

La pallina si muove sia in $X$ che in $Y$ tramite le componenti di velocità `dx` e `dy`.

```java
public void update() {
    x += dx;
    y += dy;

    // Rimbalzo sui muri orizzontali (Sinistro e Destro)
    if (x <= 0 || x >= 600 - size - 15) {
        dx = -dx; // Inverte il verso della velocità orizzontale
    }

    // Rimbalzo sul soffitto
    if (y <= 0) {
        dy = -dy; // Inverte il verso della velocità verticale
    }
}

public void reverseY() {
    dy = -dy; // Metodo pubblico invocato in caso di impatto con racchetta o mattoncini
}

```

* **La logica dell'inversione:** Multiplicare per $-1$ (o applicare il segno meno `-dx`) inverte la direzione. Se la pallina si muove verso destra con `dx = 3` e tocca il muro, `dx` diventa `-3`, facendo muovere la pallina verso sinistra.

---

### 5. `GamePanel.java` — Il Regista del Gioco

Unisce tutti gli oggetti e li gestisce all'interno del **Game Loop**.

#### A) Il Game Loop (~60 FPS)

Usa un `javax.swing.Timer` impostato a $16\text{ ms}$ ($\approx 60$ fotogrammi al secondo). Ogni $16\text{ ms}$ viene eseguito il metodo `actionPerformed`:

```java
@Override
public void actionPerformed(ActionEvent e) {
    if (!isGameOver && !isGameWon) {
        paddle.update();  // 1. Sposta la racchetta
        ball.update();    // 2. Sposta la pallina e verifica i rimbalzi sui muri
        checkCollisions(); // 3. Rileva le collisioni fisiche tra gli oggetti
    }
    repaint(); // 4. Forza Java a ridisegnare lo schermo (invoca paintComponent)
}

```

---

#### B) Gestione delle Collisioni con `Rectangle.intersects()`

Per verificare le collisioni tra due oggetti 2D (es. Pallina e Mattoncino), si genera attorno a ciascuno di essi un rettangolo delimitatore invisibile (**Bounding Box**) usando `java.awt.Rectangle`.

```java
// Collisione Pallina vs Racchetta
if (ball.getBounds().intersects(paddle.getBounds())) {
    ball.reverseY(); // Inverte la traiettoria della pallina verso l'alto
}

// Collisione Pallina vs Mattoncini
for (int r = 0; r < ROWS; r++) {
    for (int c = 0; c < COLS; c++) {
        Brick brick = bricks[r][c];

        // Controlla la collisione SOLO se il mattoncino NON è già stato distrutto
        if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
            brick.setDestroyed(true); // Distruggi il mattoncino
            ball.reverseY();          // Fa rimbalzare la pallina
            score += 10;              // Incrementa il punteggio
        }
    }
}

```

---

#### C) Condizioni di Vittoria e Sconfitta

* **Game Over:** Si verifica se la coordinata Y della pallina supera l'altezza della racchetta (`ball.getY() > 400`).
* **Vittoria:** Viene eseguito una scansione della matrice `bricks[][]`: se tutti i mattoncini hanno `isDestroyed() == true`, viene attivata la schermata di vittoria.

```java
private boolean checkWinCondition() {
    for (int r = 0; r < ROWS; r++) {
        for (int c = 0; c < COLS; c++) {
            if (!bricks[r][c].isDestroyed()) {
                return false; // Esiste ancora almeno un mattoncino integro
            }
        }
    }
    return true; // Tutti i mattoncini sono stati distrutti!
}

```

---

## 📊 Sintesi delle Idee 

1. **Perché usare l'OOP?** Ogni classe ha una sola responsabilità (`Ball` muove la pallina, `Paddle` legge i tasti, `Brick` gestisce la visibilità, `GamePanel` controlla il gioco).
2. **Il trucco di `intersects()`:** Non serve inventare formule matematiche complesse per le collisioni; la libreria standard di Java confronta i rettangoli per noi.
3. **Game Loop a 60 FPS:** Il timer scatta ogni $16\text{ ms}$, alternando la fase di calcolo (*UPDATE*) alla fase di rendering grafico (*RENDER* con `repaint()`).
