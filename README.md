## 🚀 Esercizi Richiesti (Task degli Studenti)

Il progetto base, che si trova al seguente indirizzo https://github.com/Siral1980/fumetteria, contiene la struttura per la gestione di una fumetteria

Nella cartella Resources è presente un file data.sql che genererà automaticamente i dati iniziali nel vostro database.

**Obiettivo:** 
Creare un branch con il formato nomecognome in cui caricare il proprio codice.  

Eseguire i task descritti qui di seguito 

1. **Setup & Fix:** Risolvere tutti i problemi di avvio dell'applicazione.
2. **Abilitare Swagger:** al fine di non usare programmi esterni, abilitare Swagger per l'uso dell'interfaccia grafica per il test dei metoodi.
3. **Add Comic:** implementare un metodo che permetta l'aggiunta di un nuovo fumetto. N.B. il fumetto deve essere creato con 0 come valore di default nel campo **quantity**. Un altro metodo si occuperà di modificare la quantità 
4. **Find Comic:** implementare un metodo che permetta la ricerca del fumetto tramite il titolo. N.B. il titolo in questa colonna è stato segnato come univoco tramite annotation 
5. **Stock Comic:** implementare un metodo che permetta l'aggiunta in magazzino del fumetto. Potete passare i parametri come meglio vi aggrada, non ci sono vincoli
6. **Sell Comic:** implementare un metodo che permetta la vendita del fumetto. Ovviamente bisogna prima controllare che ci siano copie disponibili e modificare le copie disponibili in base a quanti fumetti vengono venduti.
7. **Update Comic:** implementare un metodo che permetta l'aggiornamento del fumetto, senza poter cambiare id o quantità (questo compito va al metodo stock comic o sell comic)
8. **Find By Filter:** implementare un metodo che permetta la ricerca tramite una stringa, anche parziale, sui campi autore e titolo. Deve poter restituire 0 o pìù elementi.
9. **Out of Stock Toogle:** aggiungere un campo boolean al model di Comic dal nome outOfStock, con impostazione base true (quando viene creato un nuovo fumetto, la sua quantità è 0, quindi ha senso che questa impostazione sia settata a true). Implementare un metodo che cicli tutto il vostro database e faccia un'operazione di "toogle" sul campo outOfStock basandosi sulla quantità. Se è maggiore di 0 va settato a false.
10. **Find Low Stock:** implementare un metodo che permetta la ricerca di tutti i fumetti "out of stock". La lista deve contenere solo i nomi dei fumetti e nessun altro dato.
11. **BONUS TASK** - **Sell Comic History:** nel model e repository trovate i componenti relativi alla vendita dei fumetti. Implementare un metodo **sellComicHistory** che crei un record nell'entity **Sell** in cui vengono indicati:
     
    a. Fumetto venduto
    
    b. Quantità venduta
    
    c. Data e orario della vendita
    
    d. Prezzo totale della vendita
    
    Implementare anche due metodi nel SellService che permettano di cercare una vendita:
    
    a. In un range di date
    
    b. Superiore ad un determinato importo
    

Tutti i suddetti metodi devono essere comprensivi di service e controller. 


## 🛠️ Note per la valutazione
* Verrà valutata la corretta separazione delle responsabilità nei livelli dell'architettura (Controller -> Service -> Repository).
* L'uso corretto dei codici di stato HTTP (es. `201 Created` per le creazioni, `400 Bad Request` per dati non validi, `404 Not Found` se una risorsa non esiste).
* La pulizia del codice e il rispetto delle convenzioni di naming di Java/Spring.
* La creazione di un GeneralExceptionHandler è gradito

Il progetto, oltre ad essere pushato sul vostro branch nel repository, dovrà essere compresso in un file.zip e caricato sul drive della scuola al seguente indirizzo https://drive.google.com/drive/folders/1K1wjzjyUM9oIlX_G3kRqdck2qViwtDou?usp=drive_link

 