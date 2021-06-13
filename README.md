# marvel-characters
Getting details of Marvel Characters

# Steps to run application (Windows)

1. Unzip folder characters.zip
2. Import into eclipse as Maven project
3. application.properties has URL for Marvel Characters (http://gateway.marvel.com/v1/public/characters)
4. Download lombok.jar from maven repository
5. Run lombok.jar using command $java -jar lombok.jar
6. It will detect installed IDE automatically and install plugin there
7. Restart Eclipse
8. Generate public key and private key from http://developer.marvel.com
8. Download HashCorp Vault from https://www.vaultproject.io/downloads for securing API keys
9. Unzip folder
10. Run vault.exe in command prompt
11. Start vault server using below command
	$vault server --dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"
12. Create secure key for public key and private key using below commands
	$set VAULT_ADDR="http://127.0.0.1:8200"
	$vault kv put secret/characters marvel.publickey={publicKey} marvel.privatekey={privateKey}
13. Check created key using 
	$vault kv get secret/characters
14. Run below command to build and run tests
	$mvn clean install
15. Run CharactersApplication.java as "Java Application"
16. Once application is up you can test the service by hitting below URLs
	http://localhost:8080/characters/ (for getting all the characters list)
	http://localhost:8080/characters/{characterId}	(for getting details of characte using given ID)
