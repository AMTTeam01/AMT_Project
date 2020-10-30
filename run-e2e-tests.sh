cd e2e/
npm install
npx codeceptjs run --steps

cd ..

# Restart web app and database
docker-compose down
rm -rf ./volumes/db
docker-compose build
docker-compose up -d db
