# Meta - Discord Bot

### Requirements
* [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [PostgreSQL](https://www.postgresql.org/download/)

### Initial Setup
1. Clone the repository: `git clone https://github.com/leon-perkovic/meta-discord-bot.git`
2. Run `./gradlew clean build` in **/meta-discord-bot** folder
3. Set Active Profile to **bootrun*
4. Create new Application on Discord's Developers site
5. Invite bot to the server

### Postgres Setup
1. Create new role with desired name and password
2. Create new database with desired name and set created role as the owner

### Application bootrun properties
1. Move to **/src/main/resources** folder
2. Open **application-bootrun.properties** file
3. Set *spring.datasource.url* to **jdbc:postgresql://localhost:5432/<your_database_name>**
4. Set *spring.datasource.username* to username of your new role
5. Set *spring.datasource.password* to password of your new role

### Application properties
1. Move to **/src/main/resources** folder
2. Open **application.properties** file
3. Set *token* to your bot's token
4. Set *serverId* to ID of your server
5. Set *announcementChannel* to default channel that will be used for bot announcements
6. Set all *roles* to role names on your discord server
