VERSION=${VERSION:-4.12}
javac -cp .:junit-${VERSION}.jar "$1.java"
java -cp .:junit-${VERSION}.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore "$1"
rm *.class
