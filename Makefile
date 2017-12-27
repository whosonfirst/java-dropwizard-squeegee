build:
	mvn clean install

docker-build:
	docker build -t squeegee .

docker-run:
	@make docker-build
	docker run -it -p 6161:8080 squeegee
