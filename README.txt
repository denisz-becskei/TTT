TicTacToe & TicTacToe Replay

A TicTacToe applikáció a klasszikus Amőba játék megvalósítása. Az applikáció elindításakor beállítható a táblaméret, továbbá két számláló. Az első számláló minden lépés közötti maximális időtartamot állítsa be. Amennyiben ez az idő alatt nem lép a játékos, a másik játékos automatikusan nyer. A második számláló pedig a maximális meccsidőtartamot állítsa be. Ha ez lejárt, a meccsnek vége és mindkét játékos veszít. Kiválasztható még a játékmód az egyéni "Flip-Flop" objektummal, amelyet a sötét négyzetre való kattintással tudunk változtatni: a kétjátékos-mód egy felhasználó vs. felhasználó játékmódot valósít meg, az egyjátékos-móddal pedig a felhasználó a számítógépes ellenfél ellen játszik. 
A kezdőlapon lévő bemeneti mezők értékei limitálva vannak, amiket hogyha a felhasználói bemenet túllép, akkor automatikusan alapértelmezetten a minimum értékekkel inicializálja a játékot, valamint ezek és a webes appikáció numerikus input mezőin is átfuttatunk egy ellenőrzést, hogy az értékek valóban numerikusak e.
Egy meccs megnyerhető, ha 5 ugyanolyan szimbólum kijön horizontálisan vagy vertikálisan egymás mellett, vagy diagonálisan. A meccs végén, ha nyertes születik, a meccs elmenthető az adatbázisba.

A TicTacToe Replay egy Tomcat szerveren futó applikáció. Ezen oldal kezdőlapján (index.jsp) ki van listázva az összes adatbázisba elmentett party. Itt ezek a meccsek visszajátszhatók az "Újrajátszás" gombra kattintva. A következő képernyőn a lépések között előre és hátra is tudunk lépni, valamint még az első lépés előtt el tudunk indítani egy automatát, amely automatikusan fog előrelépni, amíg a meccs végére nem érünk. A kezdőlapon még kilehet egy-egy elmentett party-t törölni.

A két applikáció egy közös SQLite adatbázist használ, aminek az útvonala a "TicTacToe/src/main/resources/database.sqlite". Ezen kívül az adatbázis elérési útvonalát átkell állítani az application.properties fájlba, hiszen ez egy hard-coded útvonal, azaz rendszerről-rendszerre váltakozó.
Az adatbázis egyetlen táblából áll. Ide kerülnek elmentésre a meccsek a következő módon:
- Ha a felhasználó el kívánja menteni a meccset, generálódik egy egyedi random betűkből és számokból álló string, ami lesz a meccs azonosítója.
- Egy meccset úgy lehet a lehető legjobban rekonstruálni, hogy elmentjük, melyik mező mikor került aktiválásra, ezért ezt a programban számontartjuk egy Integer-ekből álló kétdimenziós tömbben, majd ezeket a számokat összekonkatanáljuk egy stringgé, majd elmentjük az adatbázisba.
- Ahhoz, hogy pontosan rekonstruálni tudjuk a táblát, valamint a lépések sorrendjét tudnunk kell még a tábla méretét, ezért ezt is lementjük.
- Végezetül eltároljuk azt, hogy mikor került a meccs lejátszásra, amit majd meg tudunk jeleníteni a webes felületen.

