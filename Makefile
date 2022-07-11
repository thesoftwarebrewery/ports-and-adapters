.PHONY: *

environment-up:
	@docker compose up --force-recreate --remove-orphans -V

test:
	@./mvnw verify
