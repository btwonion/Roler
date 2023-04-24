# Roler
A Discord Bot using [Kord](https://github.com/kordlib/kord) and [Kotlin](https://kotlinlang.org) that adds a simple embed where you can pick roles.
To deploy I'm using Docker.
<br>This bot is used on my [Developer Discord](https://discord.gg/z5xBpunxH3).

![example.png](assets/example.png)

## Self Hosting
You can use this bot by yourself too. Simply pull the [latest docker image](https://github.com/btwonion/Roler/pkgs/container/roler) and create docker-compose.yml.
<br>Additionally you have to create an .env file that works as a config. Below you can see an example file. To create a role you have to add the role_id and role_emoji properties.

<details>
<summary>.env</summary>

```properties
BOT_TOKEN=<YOURBOTTOKEN>
GUILD_ID=<YOURGUILDID>
MONGO_USERNAME=onion
MONGO_ADDRESS=123.456.789.78
MONGO_DATABASE=onion
MONGO_PORT=27017
MONGO_PASSWORD=MyGreatPassword123!
```

</details>

<details>
<summary>docker-compose.yml</summary>

```yaml
version: "3"

services:
  bot:
    image: ghcr.io/btwonion/roler:master
    env_file:
      - /home/onion/storage/roler-config.env
    restart: unless-stopped
    container_name: roler
```

</details>

### Other
If you need help with any of my mods just join my [discord server](https://nyon.dev/discord)
