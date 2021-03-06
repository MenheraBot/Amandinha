<p align="center">
  <a href="https://github.com/MenheraBot/Amandinha">
    <img src="https://cdn.discordapp.com/avatars/757295289630720031/bd6d5157048e812a85b650b3f2adc053.png?size=512" alt="Logo" width="160" height="160">
  </a>

  <h3 align="center">๐ฆ Amandinha ๐ฆ</h3>

  <p align="center">
    A private bot made with Javaโ๏ธ to help MenheraBot's Support Server
    <br />
    <a href="https://discord.gg/fZMdQbA"><strong>MenheraBot Support Server ยป</strong></a>
    <br />
    <br />
  </p>
</p>

## ๐จโ๐ป | Contributing

You may contribute to this project by opening an issue or creating a pull request on GitHub. Fell free to help the actual code, or just give me advice on how to improve the code.

## ๐ฅ | Running

To run Amandinha, you need to have [Docker](https://www.docker.com/) in your machine. You have two options of installation, follow the one that applies to you.

### ๐ฎ | Building the Image

> If you want to build the image yourself, you can do it by following these steps:

1. ๐งน Clone the repository

```bash
git clone https://github.com/MenheraBot/Amandinha.git
```

2. ๐ป Building the Image

```bash
docker build . --tag amandinha
```

3. ๐โโ๏ธ Running a Container

```bash
docker run --name AmandinhaBot -e "TOKEN=ReplaceWithToken" -d -t amandinha
```

> Obs: you need to add the Bot Token in `ReplaceWithToken`. Don't have a token? Follow [Discord's Tutorial](https://discord.com/developers/docs/getting-started).

That's It! Amandinha is now running in a container OwO.

### ๐ | Downloading the Image

> If you don't really want all the source code, and just want to execute the bot, you can just donwload the image from the Container Registry.

1. ๐ฅ Download the image

```bash
docker pull ghcr.io/menherabot/amandinha:latest
```

> You need to be [logged in](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry#authenticating-to-the-container-registry)

2. ๐โโ๏ธ Running a Container

```bash
docker run --name AmandinhaBot -e "TOKEN=ReplaceWithToken" -d -t ghcr.io/menherabot/amandinha:latest
```

> Obs: you need to add the Bot Token in `ReplaceWithToken`. Don't have a token? Follow [Discord's Tutorial](https://discord.com/developers/docs/getting-started).

Less go! Amandinha is now running in a container OwO.

## ๐จ | Made With

- [JDA](https://github.com/DV8FromTheWorld/JDA)

## โ๏ธ | License

Distributed under the MIT License. See `LICENSE` for more information.

## ๐ง | Contact

Discord: **Luxanna#5757**

Twitter: **[@Luxanna_Dev](https://twitter.com/Luxanna_Dev)**

---

MenheraBot was made with โค๏ธ by Luxanna.
