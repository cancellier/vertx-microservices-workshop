cd quote-generator
mvn clean package
java -jar target/quote-generator-1.0-SNAPSHOT-fat.jar start -id quote-generator --redirect-output
cd ..

cd portfolio-service
mvn clean package
java -jar target/portfolio-service-1.0-SNAPSHOT-fat.jar start -id portfolio-service --redirect-output
cd ..

cd compulsive-traders
mvn clean package
java -jar target/compulsive-traders-1.0-SNAPSHOT-fat.jar start -id compulsive-traders --redirect-output
cd ..

cd audit-service
mvn clean package
java -jar target/audit-service-1.0-SNAPSHOT-fat.jar start -id audit-service --redirect-output
cd ..

cd trader-dashboard
mvn clean package
java -jar target/trader-dashboard-1.0-SNAPSHOT-fat.jar start -id trader-dashboard --redirect-output
cd ..