echo "Limpiando proyecto Mauven antiguo..."
call mvn clean
echo "Compilando nuevo proyecto Mauven..."
call mvn compile
echo "Empaquetando el nuevo proyecto Mauven..."
call mvn package -DskipTests
echo "El proyecto esta listo para ser ejecutado con SpringBoot."