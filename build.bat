echo "Limpiando projecto Mauven antiguo..."
call mvn clean
echo "Compilando nuevo projecto Mauven..."
call mvn compile
echo "Empaquetando el nuevo projecto Mauven..."
call mvn package -DskipTests
echo "El projecto esta listo para ser ejecutado con SpringBoot."