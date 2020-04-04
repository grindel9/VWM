# VWM
semestralni prace VWM

API:
1.) API komunikuje pouze s FE (Angular), samotna aplikace pro vytezovani Deep Webu do nej vubec nema pristup. 
2.) je nutne stahnout si apache-tomcat-9.0.33 a nainstalovat si ho aby na nem bylo mozne spoustet .war ktere nam vznikne po scompilovani programu.
3.) API ma vystavene endpointy pro vsechny entity (lowecase):
	a.) pokud url obsahuje jen nazev entity ocekava se ze se do prohlizece posle GET a API nam vrati vsechny zaznamy z 		tabulky v .json formatu.
	b.) pokud url obsahuje i /id za nazvem entity vrati .json zaznam z tabulky odpovidajici id daneho zaznamu.
4.) API komunikuje s MySQL databazi ze ktere ziskava data. Konfigurace pripojeni k databazi je ve tride Connector, ale nemelo by byt potreba ji modifikovat.
