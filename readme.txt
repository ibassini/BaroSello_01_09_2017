1. Aprire prompt dei comandi;
2. Posizionarsi sulla cartella dei sorgenti: C:\TestNew\BaroSello\src
3. set classpath=C:\TestNew\BaroSello\src\jline-1.0.jar;C:\TestNew\BaroSello\src\jansi-1.16.jar;%classpath%
4. javac BaroSello.java
5. (per l'help del test >>) java -cp jansi-1.16.jar;. BaroSello
NB: Utilizzato jdk 1.8