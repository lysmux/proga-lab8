DC = docker compose --env-file .env

############ MIGRATIONS #############
.PHONY: migrate
migrate:
	$(DC) run --rm liquibase
############ MIGRATIONS #############