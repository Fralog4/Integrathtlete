**Things to add**:

- Language choosing between English - Italian - others
- Color font at the beginning it's white on white
- Logos
- If some info are empty -> reject the the module at the beginning:
  - do we actually need to know the infos?
- Sport section is pretty useless get rid of it
- Switch between dark and light mode
- microservice ebay for product listing


Ecco la **TODO List** proposta. Dimmi se sei d'accordo o se vuoi spostare qualcosa.

### 🟥 Priorità 1: Architettura e Stabilità (Le fondamenta)
Questi sono i punti critici per evitare crash e scrivere codice pulito da subito.

1.  **Spostare il caricamento JSON:** Togliere `JsonHelper.load...` dal `MainActivity` (Main Thread) e spostarlo in una Coroutine nel ViewModel per evitare blocchi all'avvio.
2.  [cite_start]**Introdurre Hilt (Dependency Injection):** Configurare Hilt per eliminare tutte quelle `UserProfileViewModelFactory` manuali e passare i repository automaticamente[cite: 13, 84, 85].
3.  [cite_start]**Spostare la logica nei ViewModel:** Spostare il filtro `supplements.filter { ... }` dalla UI (`SupplementSuggestionsScreen`) al ViewModel[cite: 81].

### 🟨 Priorità 2: Code Quality & Refactoring
Una volta che l'architettura è solida, puliamo il codice esistente.

4.  [cite_start]**Theming & Colori:** Sostituire i colori hardcoded (es. `Color(0xFF007AFF)`) con i colori del tema (`MaterialTheme.colorScheme`) definiti in `Color.kt` per supportare bene la Dark Mode[cite: 10, 86, 104].
5.  **Stringhe & Localizzazione:** Spostare le stringhe ("Profilo Utente", ecc.) da codice a `strings.xml`. [cite_start]Questo prepara l'app per il supporto multi-lingua (IT/EN) che volevi[cite: 4, 19, 86].
6.  [cite_start]**Navigazione Efficiente:** Modificare la navigazione per passare solo l'ID dell'integratore (`supplementId`) invece di tutto l'oggetto, e far recuperare i dati al ViewModel di destinazione[cite: 78].

### 🟩 Priorità 3: Nuove Feature & Persistenza
Qui aggiungiamo valore e funzionalità vere.

7.  **Persistenza Preferiti (Room Database):** Attualmente i preferiti sono salvati solo in memoria (`MutableStateFlow`). Se chiudi l'app, li perdi. Dobbiamo implementare un database locale (Room) per salvarli davvero.
8.  [cite_start]**Gestione Immagini/Loghi:** Aggiungere il supporto per caricare immagini (Loghi) come menzionato nel tuo README[cite: 86].
9.  [cite_start]**Toggle Dark/Light Mode:** Aggiungere lo switch nelle impostazioni[cite: 86].

---

### 🚀 Da dove iniziamo?

Ti consiglio vivamente di partire dal punto **1 (JSON asincrono)** o **2 (Hilt)**, perché toccano la struttura portante. Se invece vuoi qualcosa di più "visivo" e leggero, possiamo partire dal **4 (Colori e Tema)**.

**Scegli tu un numero e ti guido passo passo!**