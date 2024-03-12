


echo "Building app..."
./mvnw clean package

echo "Deploy files to server..."
scp -r -i ~/FamsNew target/beco.jar root@68.183.178.176:/var/www/famsBE

ssh -i ~/FamsNew root@68.183.178.176 <<EOF
pid=\$(sudo lsof -t -i :8080)

if [ -z "\$pid" ]; then
    echo "Start server..."
else
    echo "Restart server..."
    sudo kill -9 "\$pid"
fi
cd /var/www/famsBE
java -jar beco.jar
EOF
exit
echo "Done!"